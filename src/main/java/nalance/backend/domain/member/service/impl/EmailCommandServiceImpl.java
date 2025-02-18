package nalance.backend.domain.member.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.member.dto.EmailDTO.EmailRequest.EmailSendVerificationRequest;
import nalance.backend.domain.member.dto.EmailDTO.EmailRequest.VerificationEmailCodeRequest;
import nalance.backend.domain.member.entity.Member;
import nalance.backend.domain.member.repository.MemberRepository;
import nalance.backend.domain.member.service.EmailCommandService;
import nalance.backend.global.email.EmailUtil;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.handler.EmailException;
import nalance.backend.global.error.handler.MemberException;
import nalance.backend.global.redis.RedisUtil;
import nalance.backend.global.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailCommandServiceImpl implements EmailCommandService {

    private final EmailUtil emailUtil;
    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;

    @Override
    public void sendVerificationCodeToEmail(EmailSendVerificationRequest request) {
        // 이메일 중복 확인
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new MemberException(ErrorStatus.DUPLICATE_EMAIL);
        }

        try {
            emailUtil.sendCodeMessage(request.getEmail());
        } catch (Exception e) {
            throw new EmailException(ErrorStatus.FAIL_SEND_EMAIL);
        }
    }

    @Override
    public void verificationEmailCode(VerificationEmailCodeRequest request) {
        String email = request.getEmail();
        Object redisCode = redisUtil.getEmailVerificationCode(email);
        if (redisCode == null) {
            throw new EmailException(ErrorStatus.NOT_FOUND_EMAIL_CODE);
        }

        boolean isCodeValid = request.getCode().equals(String.valueOf(redisCode));
        if (isCodeValid) {
            redisUtil.deleteEmailVerificationCode(email);
        } else {
            throw new EmailException(ErrorStatus.INCORRECT_EMAIL_CODE);
        }
    }

    @Override
    public void sendImageToEmail(MultipartFile file) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        try {
            emailUtil.sendImageMessage(member.getEmail(), file);
        } catch (Exception e) {
            throw new EmailException(ErrorStatus.FAIL_SEND_EMAIL);
        }
    }
}
