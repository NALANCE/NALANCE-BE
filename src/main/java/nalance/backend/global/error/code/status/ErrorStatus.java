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

    // 이메일 관련 에러
    FAIL_SEND_EMAIL(HttpStatus.BAD_REQUEST, "EMAIL4001", "이메일 전송에 실패했습니다."),
    NOT_FOUND_EMAIL_CODE(HttpStatus.NOT_FOUND, "EMAIL4002", "해당 이메일로 전송된 코드가 없습니다."),
    INCORRECT_EMAIL_CODE(HttpStatus.BAD_REQUEST, "EMAIL4003", "인증번호가 일치하지 않습니다."),


    // Member 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "해당 멤버가 존재하지 않습니다."),
    FAIL_JOIN_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER4002", "회원 가입에 실패했습니다."),
    INCORRECT_EMAIL_PASSWORD(HttpStatus.BAD_REQUEST,"MEMBER4003", "아이디와 비밀번호가 일치하지 않습니다."),
    FAIL_UPDATE_ID(HttpStatus.BAD_REQUEST, "MEMBER4004", "아이디 변경에 실패했습니다."),
    INCORRECT_PASSWORD_CODE(HttpStatus.BAD_REQUEST, "MEMBER4005", "비밀 번호 확인이 일치하지 않습니다."),
    FAIL_UPDATE_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER4006", "비밀번호 변경에 실패했습니다."),
    FAIL_DELETE_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER4007", "회원 탈퇴에 실패했습니다.");


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
