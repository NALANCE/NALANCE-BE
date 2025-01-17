package nalance.backend.domain.member.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.member.dto.MemberDTO;
import nalance.backend.domain.member.service.MemberQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {
    @Override
    public MemberDTO.MemberResponse.MemberProfileResponse getMemberProfile() {
        // 작성 x
        return null;
    }
}
