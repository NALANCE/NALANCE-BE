package nalance.backend.global.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nalance.backend.domain.member.dto.MemberDTO;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.validation.annotation.PasswordMatch;
import org.springframework.stereotype.Component;
import nalance.backend.domain.member.dto.MemberDTO.MemberRequest.MemberPasswordUpdateRequest;

@Component
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, MemberPasswordUpdateRequest> {
    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MemberPasswordUpdateRequest value, ConstraintValidatorContext context) {
        if (value == null || value.getPassword() == null || value.getConfirmPassword() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.PASSWORD_MISMATCH.getMessage())
                    .addConstraintViolation();
            return false;
        }

        if (!value.getPassword().equals(value.getConfirmPassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.PASSWORD_MISMATCH.getMessage())
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
