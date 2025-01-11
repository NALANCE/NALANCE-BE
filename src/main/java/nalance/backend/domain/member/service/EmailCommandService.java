package nalance.backend.domain.member.service;

import nalance.backend.domain.member.dto.EmailDTO.EmailRequest.*;

public interface EmailCommandService {

    void sendVerificationCodeToEmail(EmailSendVerificationRequest request);

    void verificationEmailCode(VerificationEmailCodeRequest request);

    void sendImageToEmail(EmailImageSendRequest request);
}
