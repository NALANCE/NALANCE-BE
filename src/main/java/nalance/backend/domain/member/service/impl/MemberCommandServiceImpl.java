package nalance.backend.domain.member.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.member.dto.MemberDTO;
import nalance.backend.domain.member.entity.Member;
import nalance.backend.domain.member.repository.MemberRepository;
import nalance.backend.domain.member.service.MemberCommandService;
import nalance.backend.domain.terms.dto.MemberAgreeDTO;
import nalance.backend.domain.terms.entity.MemberAgree;
import nalance.backend.domain.terms.entity.Terms;
import nalance.backend.domain.terms.repository.MemberAgreeRepository;
import nalance.backend.domain.terms.repository.TermsRepository;
import nalance.backend.global.jwt.TokenDTO;
import nalance.backend.global.jwt.TokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public void joinMember(MemberDTO.MemberRequest.JoinRequest request) {
        // 1. 이메일 중복 확인
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
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
                        .orElseThrow(() -> new IllegalArgumentException("약관이 존재하지 않습니다."));

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
    public TokenDTO login(MemberDTO.MemberRequest.LoginRequest request) {
        // 1. 로그인 ID/PW를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();

        // 2. 실제로 인증 (사용자 비밀번호 체크)
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDTO tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. 발급된 토큰 반환
        return tokenDto;
    }

//    @Override
//    public MemberDTO.MemberResponse.LoginResponse login(MemberDTO.MemberRequest.LoginRequest request) {
//        return null;
//    }


    @Override
    public void updateEmail(MemberDTO.MemberRequest.MemberEmailUpdateRequest request) {
        // 작성 x
    }

    @Override
    public void updatePassword(MemberDTO.MemberRequest.MemberPasswordUpdateRequest request) {
        // 작성 x
    }

    @Override
    public void deleteMember() {
        // 작성 x
    }


}
