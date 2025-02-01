package nalance.backend.domain.member.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.category.service.CategoryCommandService;
import nalance.backend.domain.member.dto.MemberDTO;
import nalance.backend.domain.member.entity.Member;
import nalance.backend.domain.member.entity.RefreshToken;
import nalance.backend.domain.member.repository.MemberRepository;
import nalance.backend.domain.member.repository.RefreshTokenRepository;
import nalance.backend.domain.member.service.MemberCommandService;
import nalance.backend.domain.terms.dto.MemberAgreeDTO;
import nalance.backend.domain.terms.entity.MemberAgree;
import nalance.backend.domain.terms.entity.Terms;
import nalance.backend.domain.terms.repository.MemberAgreeRepository;
import nalance.backend.domain.terms.repository.TermsRepository;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.handler.MemberException;
import nalance.backend.global.error.handler.TermsException;
import nalance.backend.global.jwt.dto.TokenDTO.TokenRequest;
import nalance.backend.global.jwt.dto.TokenDTO.TokenResponse;
import nalance.backend.global.jwt.TokenProvider;
import nalance.backend.global.security.SecurityUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final TermsRepository termsRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MemberAgreeRepository memberAgreeRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CategoryCommandService categoryCommandService;

    @Override
    public void joinMember(MemberDTO.MemberRequest.JoinRequest request) {
        // 1. 이메일 중복 확인
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new MemberException(ErrorStatus.DUPLICATE_EMAIL);
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. Member 엔티티 생성
        Member member = Member.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .isActivated(true) // 기본적으로 활성화된 회원
                .build();

        memberRepository.save(member); // Member 저장

        // 4. 약관 동의 처리
        if (request.getTerms() != null) {
            for (MemberAgreeDTO.AgreeTermsRequest agreeTermsRequest : request.getTerms()) {
                Terms terms = termsRepository.findById(agreeTermsRequest.getTermsId())
                        .orElseThrow(() -> new TermsException(ErrorStatus.TERMS_NOT_FOUND));

                // MemberAgree 엔티티 생성
                MemberAgree memberAgree = MemberAgree.builder()
                        .member(member)
                        .terms(terms)
                        .build();

                memberAgreeRepository.save(memberAgree); // 약관 동의 저장
            }
        }

        // 5. 회원가입 후 카테고리 생성
        if (request.getCategories() != null && !request.getCategories().isEmpty()) {
            categoryCommandService.createManyCategoryForMember(member, request.getCategories());
        }
    }

    @Override
    public TokenResponse login(MemberDTO.MemberRequest.LoginRequest request) {
        // 1. 로그인 ID/PW를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();

        // 2. 실제로 인증 (사용자 비밀번호 체크)
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (Exception e) {
            throw new MemberException(ErrorStatus.INVALID_LOGIN_CREDENTIALS); // 비밀번호 불일치 또는 인증 실패
        }

        // 3. 인증 정보를 기반으로 Member 조회
        Member member = memberRepository.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 4. 탈퇴 여부 확인
        if (!member.getIsActivated()) {
            throw new MemberException(ErrorStatus.DEACTIVATED_MEMBER); // 탈퇴된 회원
        }

        // 5. 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponse tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    @Override
    public TokenResponse reissue(TokenRequest request) {
        if (!tokenProvider.validateToken(request.getRefreshToken())) {
            throw new MemberException(ErrorStatus.INVALID_TOKEN); // 유효하지 않은 refresh token
        }
        Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new MemberException(ErrorStatus.LOGOUT_MEMBER)); // 로그아웃된 멤버

        if (!refreshToken.getValue().equals(request.getRefreshToken())) {
            throw new MemberException(ErrorStatus.MEMBER_NOT_FOUND); // 토큰의 유저 정보가 일치하지 않음
        }

        TokenResponse tokenDto = tokenProvider.generateTokenDto(authentication);

        refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    @Override
    public void updateEmail(MemberDTO.MemberRequest.MemberEmailUpdateRequest request) {
        // 1. 현재 로그인된 회원의 ID 가져오기
        Long memberId = SecurityUtil.getCurrentMemberId();

        // 2. 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 3. 이메일 변경
        member.updateEmail(request.getEmail());
    }

    @Override
    public boolean validatePassword(MemberDTO.MemberRequest.MemberPasswordValidationRequest request) {
        // 현재 로그인된 사용자의 ID 가져오기
        Long memberId = SecurityUtil.getCurrentMemberId();

        // 사용자 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 입력된 비밀번호가 기존 비밀번호와 같은지 비교
        return passwordEncoder.matches(request.getPassword(), member.getPassword());
    }

    @Override
    public void updatePassword(MemberDTO.MemberRequest.MemberPasswordUpdateRequest request) {
        // 1. 비밀번호와 확인 비밀번호가 일치하는지 확인
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new MemberException(ErrorStatus.PASSWORD_MISMATCH);
        }

        // 2. 현재 로그인된 회원의 ID 가져오기
        Long memberId = SecurityUtil.getCurrentMemberId();

        // 3. 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 4. 비밀번호 암호화 후 변경
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        member.updatePassword(encodedPassword);
    }

    @Override
    public void deleteMember() {
        try {
            // 1. 현재 로그인된 회원의 ID 가져오기
            Long memberId = SecurityUtil.getCurrentMemberId();

            // 2. 회원 조회
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

            // 3. 활성화 상태 변경
            member.deactivate();
        } catch (Exception e) {
            throw new MemberException(ErrorStatus.FAIL_DELETE_MEMBER);
        }
    }
}
