package nalance.backend.global.jwt;

import lombok.Getter;

@Getter
public class TokenRequestDTO {
    private String accessToken;
    private String refreshToken;
}
