package nalance.backend.domain.member.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.member.dto.MemberDTO;
import nalance.backend.domain.member.entity.Member;
import nalance.backend.domain.member.repository.MemberRepository;
import nalance.backend.domain.member.service.MemberQueryService;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.handler.MemberException;
import nalance.backend.global.security.SecurityUtil;
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
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        return MemberDTO.MemberResponse.MemberProfileResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .isActivated(member.getIsActivated())
                .build();
    }
}
