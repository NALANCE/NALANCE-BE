package nalance.backend.global.jwt;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;
    private String refreshToken;
}
