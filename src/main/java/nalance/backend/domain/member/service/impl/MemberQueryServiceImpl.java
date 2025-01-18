package nalance.backend.domain.member.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.member.dto.MemberDTO;
import nalance.backend.domain.member.entity.Member;
import nalance.backend.domain.member.repository.MemberRepository;
import nalance.backend.domain.member.service.MemberQueryService;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.handler.TermsException;
import nalance.backend.global.security.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {
    private final MemberRepository memberRepository;

    @Override
    public MemberDTO.MemberResponse.MemberProfileResponse getMemberProfile() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new TermsException(ErrorStatus.NOT_FOUND_TERMS));
        return MemberDTO.MemberResponse.MemberProfileResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .isActivated(member.getIsActivated())
                .build();
    }

//    @Override
//    public Long getMemberIdFromSecurityContext() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName(); // 이메일 또는 username 가져오기
//
//        // 데이터베이스에서 사용자 조회
//        Member member = memberRepository.findByEmail(username)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//
//        return member.getMemberId(); // 사용자 ID 반환
//    }
}
