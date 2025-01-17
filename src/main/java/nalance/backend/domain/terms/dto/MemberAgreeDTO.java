package nalance.backend.domain.terms.dto;

import lombok.*;
import nalance.backend.global.common.enums.Type;

public class MemberAgreeDTO {
    @Getter
    public static class AgreeTermsRequest {
        private Long termsId;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AgreeTermsResponse {
        private Long termsId;
        private String content;
        private Type type;
    }
}
