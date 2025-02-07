package nalance.backend.domain.todo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.v3.core.util.Json;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import nalance.backend.global.common.enums.Status;

import java.time.LocalDate;
import java.util.List;

public class TodoDTO {

    public static class TodoRequest{

        @Getter
        public static class TodoCreateRequest {
            private Long categoryId;
            private String todoName;
            private LocalDate date;

            @Pattern(regexp = "^[0-2][0-9]:[0-5][0-9]$", message = "시간 형식이 올바르지 않습니다. HH:mm 형식으로 입력해주세요.")
            private String startTime;
            @Pattern(regexp = "^[0-2][0-9]:[0-5][0-9]$", message = "시간 형식이 올바르지 않습니다. HH:mm 형식으로 입력해주세요.")
            private String endTime;

            private Integer status;
        }

        @Getter
        public static class TodoUpdateRequest {
            private String todoName;

            @Pattern(regexp = "^[0-2][0-9]:[0-5][0-9]$", message = "시간 형식이 올바르지 않습니다. HH:mm 형식으로 입력해주세요.")
            private String startTime;
            @Pattern(regexp = "^[0-2][0-9]:[0-5][0-9]$", message = "시간 형식이 올바르지 않습니다. HH:mm 형식으로 입력해주세요.")
            private String endTime;

            private LocalDate date;
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
            private Long todoId;
            private String todoName;
            private String startTime;
            private String endTime;
            private String formattedDuration;
            private LocalDate date;
            private Status status;
        }
    }
}
