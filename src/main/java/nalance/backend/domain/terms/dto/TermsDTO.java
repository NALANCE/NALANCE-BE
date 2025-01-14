package nalance.backend.domain.terms.dto;

import lombok.*;
import nalance.backend.global.common.enums.Type;

public class TermsDTO {
    public static class TermsResponse {
        @Builder
        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class TermsDetailResponse {
            private Long termsId;
            private String content;
            private Type type;
        }
    }
}