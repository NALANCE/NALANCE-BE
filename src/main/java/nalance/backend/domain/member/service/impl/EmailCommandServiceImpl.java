package nalance.backend.domain.member.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.member.dto.EmailDTO.EmailRequest.EmailImageSendRequest;
import nalance.backend.domain.member.dto.EmailDTO.EmailRequest.EmailSendVerificationRequest;
import nalance.backend.domain.member.dto.EmailDTO.EmailRequest.VerificationEmailCodeRequest;
import nalance.backend.domain.member.service.EmailCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailCommandServiceImpl implements EmailCommandService {

    @Override
    public void sendVerificationCodeToEmail(EmailSendVerificationRequest request) {
        // TODO 로직
    }

    @Override
    public void verificationEmailCode(VerificationEmailCodeRequest request) {
        // TODO 로직
    }

    @Override
    public void sendImageToEmail(EmailImageSendRequest request) {
        // TODO 로직
    }
}
