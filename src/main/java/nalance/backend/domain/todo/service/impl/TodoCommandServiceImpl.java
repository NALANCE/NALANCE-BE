package nalance.backend.domain.todo.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.category.entity.Category;
import nalance.backend.domain.category.repository.CategoryRepository;
import nalance.backend.domain.todo.converter.TodoConverter;
import nalance.backend.domain.todo.dto.TodoDTO;
import nalance.backend.domain.todo.entity.Todo;
import nalance.backend.domain.todo.repository.TodoRepository;
import nalance.backend.domain.todo.service.TodoCommandService;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.handler.CategoryException;
import nalance.backend.global.error.handler.TodoException;
import nalance.backend.global.validation.annotation.ExistTodo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoCommandServiceImpl implements TodoCommandService {

    private final CategoryRepository categoryRepository;
    private final TodoRepository todoRepository;

    @Override
    public void createTodo(TodoDTO.TodoRequest.TodoCreateRequest request) {

        Todo todo = TodoConverter.toCreateTodo(request);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryException(ErrorStatus.CATEGORY_NOT_FOUND));

        todo.addCategory(category);
        todoRepository.save(todo);
    }

    @Override
    public void deleteTodo(Long todoId) {

        todoRepository.deleteById(todoId);
    }

    @Override
    public void updateTodo(TodoDTO.TodoRequest.TodoUpdateRequest request, Long todoId) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(ErrorStatus.TODO_NOT_FOUND));

        todo.updateTodo(request.getTodoName(),request.getDuration(),request.getDate());
    }

    @Override
    public void completeTodo(Long todoId) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(ErrorStatus.TODO_NOT_FOUND));

        todo.completeTodo();
    }
}
