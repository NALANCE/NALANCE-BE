package nalance.backend.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import nalance.backend.global.validation.annotation.CheckEmailCode;

public class EmailDTO {

    public static class EmailRequest {

        @Getter
        public static class EmailSendVerificationRequest {
            @NotBlank(message = "이메일은 필수 입력 값입니다.")
            @Email(message = "이메일 형식에 맞지 않습니다.")
            private String email;
        }

        @Getter
        public static class VerificationEmailCodeRequest {
            @NotBlank(message = "이메일은 필수 입력 값입니다.")
            @Email(message = "이메일 형식에 맞지 않습니다.")
            private String email;
            @CheckEmailCode
            private String code;
        }

        @Getter
        public static class EmailImageSendRequest {
            // TODO validation
            private String imageUrl;
        }
    }

}
