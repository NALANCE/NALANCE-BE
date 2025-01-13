package nalance.backend.domain.graph.dto;

import lombok.Builder;
import lombok.Getter;

public class GraphDTO {

    public static class GraphRequest {
        @Getter
        public static class GraphDailyRequest {
            private String date;
        }

        @Getter
        public static class CalendarMonthlyRequest {
            private int year;
            private int month;
        }
    }

    public static class GraphResponse {
        @Getter
        @Builder
        public static class GraphDailyResponse {
            private String date;
            private Object data; // 조회된 데이터
            private double ratio; // 계산된 비율
        }

        @Getter
        @Builder
        public static class CalendarMonthlyResponse {
            private int year;
            private int month;
            private Object data; // 조회된 데이터
        }
    }
}
