package nalance.backend.domain.member.service.impl;

import lombok.RequiredArgsConstructor;
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
    }

    @Override
    public TokenResponse login(MemberDTO.MemberRequest.LoginRequest request) {
        try {
            // 1. 로그인 ID/PW를 기반으로 AuthenticationToken 생성
            UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();

            // 2. 실제로 인증 (사용자 비밀번호 체크)
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenResponse tokenDto = tokenProvider.generateTokenDto(authentication);

            RefreshToken refreshToken = RefreshToken.builder()
                    .key(authentication.getName())
                    .value(tokenDto.getRefreshToken())
                    .build();

            refreshTokenRepository.save(refreshToken);

            return tokenDto;

        } catch (Exception e) {
            throw new MemberException(ErrorStatus.INVALID_LOGIN_CREDENTIALS);
        }
    }

    @Override
    public TokenResponse reissue(TokenRequest request) {
        if (!tokenProvider.validateToken(request.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃된 사용자입니다."));

        if (!refreshToken.getValue().equals(request.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
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
        // 1. 현재 로그인된 회원의 ID 가져오기
        Long memberId = SecurityUtil.getCurrentMemberId();

        // 2. 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        // 3. 활성화 상태 변경
        member.deactivate();
    }
}
