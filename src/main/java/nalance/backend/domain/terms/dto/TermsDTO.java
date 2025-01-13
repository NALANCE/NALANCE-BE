package nalance.backend.domain.terms.dto;

import lombok.*;
import nalance.backend.global.common.enums.Type;

public class TermsDTO {
    @Getter
    public static class TermsCreateRequest {
        private String content;
        private Type type;
    }

    @Getter
    public static class AgreeTermsRequest {
        private boolean isAgreed;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TermsResponse {
        private Long termsId;
        private String content;
        private Type type;
        private boolean isAgreed;
    }
}