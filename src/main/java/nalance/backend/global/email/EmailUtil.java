package nalance.backend.global.email;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.handler.EmailException;
import nalance.backend.global.redis.RedisUtil;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private static final int CODE_LENGTH = 6;
    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public void sendMessage(String email) throws Exception {
        String code = createVerificationCode();
        MimeMessage message = createMessage(email, code);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new EmailException(ErrorStatus.FAIL_SEND_EMAIL);
        }
        javaMailSender.send(message);
        redisUtil.saveEmailVerificationCode(email, code);

    }

    private String createVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CODE_CHARACTERS.charAt(random.nextInt(CODE_CHARACTERS.length())));
        }
        return code.toString();
    }

    private MimeMessage createMessage(String email, String code) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        InternetAddress[] recipients = {new InternetAddress(email)};
        message.setSubject("NALANCE 회원가입 인증 코드입니다.");
        message.setRecipients(Message.RecipientType.TO, recipients);

        String msg = code; // TODO 이메일 보내는거 형식 있는지

        message.setContent(msg, "text/html; charset=utf-8");
        message.setFrom(new InternetAddress("${spring.mail.username}", "NALANCE"));

        return message;
    }
}
