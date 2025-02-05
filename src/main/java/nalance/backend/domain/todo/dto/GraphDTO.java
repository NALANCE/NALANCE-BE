package nalance.backend.domain.todo.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

public class GraphDTO {

    public static class GraphResponse {
        @Getter
        @Builder
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class GraphDailyResponse {
            private String date;
            private List<CategoryDataResponse> data;
            private boolean isBalanced; //밸런스 여부 추가
            private String message; //메세지 추가
        }

        @Getter
        @Builder
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class CalendarMonthlyResponse {
            private int year;
            private int month;
            private List<CategoryDataResponse> data; // 조회된 데이터
        }

        @Getter
        @Builder
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class CategoryDataResponse {
            private String category;
            private Double ratio;
            private String color;

        }
    }
}
