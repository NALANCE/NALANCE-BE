package nalance.backend.domain.member.service;


import nalance.backend.global.jwt.TokenDTO;
import nalance.backend.global.jwt.TokenRequestDTO;

import static  nalance.backend.domain.member.dto.MemberDTO.MemberRequest.*;
import static  nalance.backend.domain.member.dto.MemberDTO.MemberResponse.*;

public interface MemberCommandService {
    void joinMember(JoinRequest request);

    //LoginResponse login(LoginRequest request);
    TokenDTO login(LoginRequest request);

    TokenDTO reissue(TokenRequestDTO request);

    void updateEmail(MemberEmailUpdateRequest request);
    void updatePassword(MemberPasswordUpdateRequest request);
    void deleteMember();
}
