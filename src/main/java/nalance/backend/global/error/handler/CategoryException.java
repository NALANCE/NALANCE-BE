package nalance.backend.global.error.handler;

import nalance.backend.global.error.code.BaseErrorCode;
import nalance.backend.global.error.exception.GeneralException;

public class CategoryException extends GeneralException {
    public CategoryException(BaseErrorCode code) {
        super(code);
    }
}
