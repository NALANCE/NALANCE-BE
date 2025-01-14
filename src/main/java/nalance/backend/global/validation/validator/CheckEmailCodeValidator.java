package nalance.backend.global.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.validation.annotation.CheckEmailCode;
import org.springframework.stereotype.Component;

@Component
public class CheckEmailCodeValidator implements ConstraintValidator<CheckEmailCode, String> {

    private static final String EMAIL_CODE_PATTERN = "^[A-Z0-9]{6}$";

    @Override
    public void initialize(CheckEmailCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || !value.matches(EMAIL_CODE_PATTERN)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.INVALID_EMAIL_CODE.getMessage())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
