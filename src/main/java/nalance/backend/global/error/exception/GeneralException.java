package nalance.backend.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nalance.backend.global.error.code.BaseErrorCode;
import nalance.backend.global.error.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

  private BaseErrorCode code;

  public ErrorReasonDTO getErrorReason() {
    return this.code.getReason();
  }

  public ErrorReasonDTO getErrorReasonHttpStatus(){
    return this.code.getReasonHttpStatus();
  }
}

