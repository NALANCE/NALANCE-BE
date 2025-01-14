package nalance.backend.global.error.exception.handler;

import nalance.backend.global.error.code.BaseErrorCode;
import nalance.backend.global.error.exception.GeneralException;

public class CategoryHandler extends GeneralException {
    public CategoryHandler(BaseErrorCode code) {
        super(code);
    }
}
