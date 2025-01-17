package nalance.backend.global.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import nalance.backend.global.validation.validator.TodoExistValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TodoExistValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistTodo {

    String message() default "해당하는 todo가 존재하지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

