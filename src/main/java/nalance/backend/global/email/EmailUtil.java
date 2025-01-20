package nalance.backend.global.email;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.SecureRandom;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.handler.EmailException;
import nalance.backend.global.redis.RedisUtil;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private static final int CODE_LENGTH = 6;
    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public void sendCodeMessage(String email) throws Exception {
        String code = createVerificationCode();
        MimeMessage message = createCodeMessage(email, code);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new EmailException(ErrorStatus.FAIL_SEND_EMAIL);
        }
        //javaMailSender.send(message);
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

    private MimeMessage createCodeMessage(String email, String code) throws Exception {
        byte[] imageWithCode = createImageWithCode(code);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("NALANCE 회원가입 인증 코드입니다.");
        helper.setFrom(new InternetAddress("${spring.mail.username}", "NALANCE"));

        String htmlContent = "<img src='cid:verificationImage' style='display: block; margin: 0 auto; width: 600px; height: auto;' alt='Verification Code Image'>";
        helper.setText(htmlContent, true);

        helper.addInline("verificationImage", new ByteArrayResource(imageWithCode), "image/png");

        return message;
    }

    private byte[] createImageWithCode(String code) throws IOException {
        String imageUrl = "https://github.com/user-attachments/assets/29691422-d761-40f1-ba8f-c0a222599d5b";
        BufferedImage originalImage;

        // 이미지 로드
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            originalImage = ImageIO.read(inputStream);
        }

        // 새로운 이미지 생성
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage imageWithCode = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = imageWithCode.createGraphics();
        graphics.drawImage(originalImage, 0, 0, null);
        graphics.setColor(Color.BLACK); // 텍스트 색상
        graphics.setFont(new Font("Arial", Font.BOLD, 40));

        // 텍스트를 이미지에 배치
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(code);
        int textHeight = fontMetrics.getHeight();
        int x = (width - textWidth) / 2 - 60;
        int y = (height - textHeight) / 2 + fontMetrics.getAscent();
        graphics.drawString(code, x, y);

        graphics.dispose();

        // 이미지를 바이트 배열로 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(imageWithCode, "png", outputStream);

        return outputStream.toByteArray();
    }


    public void sendImageMessage(String email, MultipartFile file) throws Exception {
        MimeMessage message = createImageMessage(email, file);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new EmailException(ErrorStatus.FAIL_SEND_EMAIL);
        }
        javaMailSender.send(message);
    }

    private MimeMessage createImageMessage(String email, MultipartFile file) throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();
        InternetAddress[] recipients = {new InternetAddress(email)};
        message.setSubject("NALANCE 캡쳐한 사진입니다.");
        message.setRecipients(Message.RecipientType.TO, recipients);

        String htmlContent = """
        <div>
            <p>아래 이미지를 확인해주세요</p>
            <img src='cid:capturedImage' alt='Captured Image' />
        </div>
        """;

        MimeMultipart multipart = new MimeMultipart("related");

        MimeBodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(htmlContent, "text/html; charset=utf-8");
        multipart.addBodyPart(htmlBodyPart);

        MimeBodyPart imageBodyPart = new MimeBodyPart();
        imageBodyPart.setFileName(file.getOriginalFilename());
        imageBodyPart.setContent(file.getBytes(), file.getContentType());
        imageBodyPart.setHeader("Content-ID", "<capturedImage>");
        imageBodyPart.setDisposition(MimeBodyPart.INLINE);
        multipart.addBodyPart(imageBodyPart);

        message.setContent(multipart);

        message.setFrom(new InternetAddress("${spring.mail.username}", "NALANCE"));

        return message;
    }
}
