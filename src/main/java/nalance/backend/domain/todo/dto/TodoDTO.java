package nalance.backend.domain.todo.dto;

import lombok.*;
import nalance.backend.global.common.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TodoDTO {

    public static class TodoRequest{

        @Getter
        public static class TodoCreateRequest {
            private Long categoryId;
            private String todoName;
            private int duration;
            private LocalDate date;
            private Status status;
        }

        @Getter
        public static class TodoUpdateRequest {
            private String todoName;
            private int duration;
            private LocalDate date;
        }

        @Getter
        public static class TodoQueryRequest {
            private List<LocalDate> dateList;
            private List<Long> categoryIdList;
            private Status status;
        }

    }

    public static class TodoResponse{

        @Builder
        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class TodoPreviewListResponse {
            List<TodoPreviewResponse> todoList;
            Integer listSize;
            Integer totalPage;
            Long totalElements;
            Boolean isFirst;
            Boolean isLast;
        }

        @Builder
        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class TodoPreviewResponse {
            private String todoName;
            private int duration;
            private LocalDate date;
            private Status status;
        }
    }
}
