package nalance.backend.domain.member.service;

import nalance.backend.domain.member.dto.EmailDTO.EmailRequest.*;
import org.springframework.web.multipart.MultipartFile;

public interface EmailCommandService {

    void sendVerificationCodeToEmail(EmailSendVerificationRequest request);

    void verificationEmailCode(VerificationEmailCodeRequest request);

    void sendImageToEmail(MultipartFile file);
}
