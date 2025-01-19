package nalance.backend.global.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import nalance.backend.global.validation.validator.PasswordMatchValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatch {
    String message() default "비밀번호와 확인 비밀번호가 일치하지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
