package nalance.backend.domain.member.service;

import static  nalance.backend.domain.member.dto.MemberDTO.MemberResponse.*;

public interface MemberQueryService {
    MemberProfileResponse getMemberProfile();
    // Long getMemberIdFromSecurityContext();
}
