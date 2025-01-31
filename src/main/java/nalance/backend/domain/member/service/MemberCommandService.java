package nalance.backend.domain.member.service;

import nalance.backend.global.jwt.dto.TokenDTO.*;

import static  nalance.backend.domain.member.dto.MemberDTO.MemberRequest.*;

public interface MemberCommandService {
    void joinMember(JoinRequest request);

    TokenResponse login(LoginRequest request);

    TokenResponse reissue(TokenRequest request);

    void updateEmail(MemberEmailUpdateRequest request);
    boolean validatePassword(MemberPasswordValidationRequest request);
    void updatePassword(MemberPasswordUpdateRequest request);
    void deleteMember();
}
