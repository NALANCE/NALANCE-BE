package nalance.backend.global.error.handler;

import nalance.backend.global.error.code.BaseErrorCode;
import nalance.backend.global.error.exception.GeneralException;

public class MemberException extends GeneralException {
    public MemberException(BaseErrorCode code) {
        super(code);
    }
}
