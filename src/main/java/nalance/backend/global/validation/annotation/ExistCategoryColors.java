package nalance.backend.global.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import nalance.backend.global.validation.validator.CategoryColorsExistValidator;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryColorsExistValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistCategoryColors {

    String message() default "선택 가능한 카테고리 색상이 아닙니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}