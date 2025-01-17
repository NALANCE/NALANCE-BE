package nalance.backend.global.error.handler;

import nalance.backend.global.error.code.BaseErrorCode;
import nalance.backend.global.error.exception.GeneralException;

public class TodoException extends GeneralException {
    public TodoException(BaseErrorCode code) {
        super(code);
    }
}
