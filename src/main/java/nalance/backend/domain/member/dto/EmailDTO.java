package nalance.backend.domain.member.dto;

import lombok.Getter;

public class EmailDTO {

    public static class EmailRequest {

        @Getter
        public static class EmailSendVerificationRequest {
            // TODO validation
            private String email;
        }

        @Getter
        public static class VerificationEmailCodeRequest {
            // TODO validation
            private String email;
            // TODO validation
            private String code;
        }

        @Getter
        public static class EmailImageSendRequest {
            // TODO validation
            private String imageUrl;
        }
    }

}
