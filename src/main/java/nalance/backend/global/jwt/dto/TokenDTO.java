package nalance.backend.global.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenDTO {
    @Getter
    public static class TokenRequest {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TokenResponse {
        private String grantType;
        private String accessToken;
        private Long accessTokenExpiresIn;
        private String refreshToken;
    }
}
