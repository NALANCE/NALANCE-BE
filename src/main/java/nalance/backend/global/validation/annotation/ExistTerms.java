package nalance.backend.global.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import nalance.backend.global.validation.validator.TermsExistValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TermsExistValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistTerms {
    String message() default "잘못된 형식의 약관 ID 입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
