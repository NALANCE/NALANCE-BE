package nalance.backend.global.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import nalance.backend.domain.todo.entity.Todo;
import nalance.backend.domain.todo.service.TodoCommandService;
import nalance.backend.domain.todo.service.TodoQueryService;
import nalance.backend.global.validation.annotation.ExistTodo;
import org.springframework.stereotype.Component;
import nalance.backend.global.error.code.status.ErrorStatus;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TodoExistValidator implements ConstraintValidator<ExistTodo, Long> {
    private final TodoQueryService todoQueryService;

    @Override
    public void initialize(ExistTodo constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Optional<Todo> target = todoQueryService.findTodo(value);

        if (target.isEmpty()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.TODO_NOT_FOUND.toString())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}