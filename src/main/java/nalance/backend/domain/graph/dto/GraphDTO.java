package nalance.backend.domain.graph.dto;

import lombok.*;

import java.util.Map;

public class GraphDTO {

    public static class GraphResponse {
        @Getter
        @Builder
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class GraphDailyResponse {
            private String date;
            private Map<String, Double> data; // 조회된 데이터
        }

        @Getter
        @Builder
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class CalendarMonthlyResponse {
            private int year;
            private int month;
            private Map<String, Double> data; // 조회된 데이터
        }
    }
}
