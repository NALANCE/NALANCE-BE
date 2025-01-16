package nalance.backend.global.error.handler;

import nalance.backend.global.error.code.BaseErrorCode;
import nalance.backend.global.error.exception.GeneralException;

public class TermsException extends GeneralException {
    public TermsException(BaseErrorCode code) {super(code);}
}
