package nalance.backend.global.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.validation.annotation.ExistCategoryColors;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryColorsExistValidator implements ConstraintValidator<ExistCategoryColors, String> {

    @Override
    public void initialize(ExistCategoryColors constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        List<String> availableColors = new ArrayList<>(Arrays.asList(
                "#FFE292", "#FFDAA3", "#FCC79B", "#FFAD82",
                "#FDB9A8", "#F3ACA6", "#E6A7D0", "#CFB7F2",
                "#99AFE9", "#94BDEE", "#81D2E5", "#87D1D0",
                "#7BCBBE", "#8BCFB6", "#C2E0AE", "#DDE89A"));
        if(!availableColors.contains(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.CATEGORY_COLOR_UNUSABLE.toString()).addConstraintViolation();
        }
        return true;
    }
}