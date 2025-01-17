package nalance.backend.global.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import nalance.backend.domain.terms.entity.Terms;
import nalance.backend.domain.terms.service.TermsQueryService;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.validation.annotation.ExistTerms;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TermsExistValidator implements ConstraintValidator<ExistTerms, Long> {
    @Override
    public void initialize(ExistTerms constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value <= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.INVALID_TERMS_ID.getMessage())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
