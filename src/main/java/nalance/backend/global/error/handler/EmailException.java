package nalance.backend.global.error.handler;

import nalance.backend.global.error.code.BaseErrorCode;
import nalance.backend.global.error.exception.GeneralException;

public class EmailException extends GeneralException {

    public EmailException(BaseErrorCode code) {
        super(code);
    }
}
