package nalance.backend.global.error.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nalance.backend.global.error.code.BaseCode;
import nalance.backend.global.error.code.BaseErrorCode;
import nalance.backend.global.error.code.ErrorReasonDTO;
import nalance.backend.global.error.code.ReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    // Category 관련 에러
    CATEGORY_CREATE_ERROR(HttpStatus.BAD_REQUEST, "CATEGORY4001", "카테고리 생성에 실패했습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "CATEGORY4002", "해당 카테고리가 없습니다."),
    CATEGORY_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "CATEGORY4003", "카테고리명은 필수 입니다."),
    CATEGORY_COLOR_REQUIRED(HttpStatus.BAD_REQUEST, "CATEGORY4004", "카테고리 색상은 필수 입니다."),
    CATEGORY_NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "CATEGORY4005", "해당 카테고리명은 이미 존재합니다."),
    CATEGORY_COLOR_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "CATEGORY4006", "해당 색상은 이미 존재합니다."),
    CATEGORY_COLOR_UNUSABLE(HttpStatus.BAD_REQUEST, "CATEGORY4007", "유효하지 않은 색상입니다."),

    // 페이지 number 없음 에러
    INVALID_PAGE_NUMBER(HttpStatus.BAD_REQUEST,"PAGE4001","올바르지 않은 페이징 번호입니다."),

    // 이메일 관련 에러
    FAIL_SEND_EMAIL(HttpStatus.BAD_REQUEST, "EMAIL4001", "이메일 전송에 실패했습니다."),
    NOT_FOUND_EMAIL_CODE(HttpStatus.NOT_FOUND, "EMAIL4002", "해당 이메일로 전송된 코드가 없습니다."),
    INCORRECT_EMAIL_CODE(HttpStatus.BAD_REQUEST, "EMAIL4003", "인증번호가 일치하지 않습니다."),
    INVALID_EMAIL_CODE(HttpStatus.BAD_REQUEST, "EMAIL4004", "인증번호 형식이 올바르지 않습니다."),

    // 파일 관련 에러
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "FILE4001", "파일 형식이 올바르지 않습니다."),
    NOT_UPLOAD_FILE(HttpStatus.BAD_REQUEST, "FILE4002", "파일이 업로드되지 않았습니다."),
    TOO_BIG_SIZE_FILE(HttpStatus.BAD_REQUEST, "FILE4003", "파일 크기는 5MB를 초과할 수 없습니다."),
    INVALID_MIME_TYPE(HttpStatus.BAD_REQUEST, "FILE4004", "허용되지 않는 MIME 형식입니다. (허용 형식: image/jpeg, image/png)"),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "FILE4005", "잘못된 파일 이름입니다."),

    // Member 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "해당 멤버가 존재하지 않습니다."),
    FAIL_JOIN_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER4002", "회원 가입에 실패했습니다."),
    INCORRECT_EMAIL_PASSWORD(HttpStatus.BAD_REQUEST,"MEMBER4003", "아이디와 비밀번호가 일치하지 않습니다."),
    FAIL_UPDATE_ID(HttpStatus.BAD_REQUEST, "MEMBER4004", "아이디 변경에 실패했습니다."),
    INCORRECT_PASSWORD_CODE(HttpStatus.BAD_REQUEST, "MEMBER4005", "비밀 번호 확인이 일치하지 않습니다."),
    FAIL_UPDATE_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER4006", "비밀번호 변경에 실패했습니다."),
    FAIL_DELETE_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER4007", "회원 탈퇴에 실패했습니다."),

    // Todo 관련 에러
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO4001", "해당 Todo가 존재하지 않습니다."),
    FAIL_CREATE_TODO(HttpStatus.BAD_REQUEST, "TODO4002", "Todo 생성에 실패했습니다."),
    FAIL_DELETE_TODO(HttpStatus.BAD_REQUEST, "TODO4003", "Todo 삭제에 실패했습니다."),
    FAIL_UPDATE_TODO(HttpStatus.BAD_REQUEST, "TODO4004", "Todo 수정에 실패했습니다."),
    FAIL_COMPLETE_TODO(HttpStatus.BAD_REQUEST, "TODO4005", "Todo 완료에 실패했습니다."),
    FAIL_QUERY_TODO(HttpStatus.BAD_REQUEST, "TODO4006", "Todo 조회에 실패했습니다."),

    // Terms 관련 에러
    FAIL_GET_TERMS(HttpStatus.BAD_REQUEST, "TERMS4001", "약관 목록 조회에 실패했습니다."),
    INVALID_TERMS_ID(HttpStatus.BAD_REQUEST, "TERMS4002", "잘못된 형식의 약관 ID 입니다."),
    NOT_FOUND_TERMS(HttpStatus.NOT_FOUND, "TERMS4003", "해당 약관이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
