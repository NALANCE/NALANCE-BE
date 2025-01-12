package nalance.backend.domain.member.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.member.dto.MemberDTO;
import nalance.backend.domain.member.service.MemberCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {
    @Override
    public void joinMember(MemberDTO.MemberRequest.JoinRequest request) {
        // 작성 x
    }

    @Override
    public MemberDTO.MemberResponse.LoginResponse login(MemberDTO.MemberRequest.LoginRequest request) {
        // 작성 x
        return null;
    }

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
