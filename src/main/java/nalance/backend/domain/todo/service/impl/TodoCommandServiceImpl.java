package nalance.backend.domain.todo.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.todo.dto.TodoDTO;
import nalance.backend.domain.todo.service.TodoCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoCommandServiceImpl implements TodoCommandService {

    @Override
    public void createTodo(TodoDTO.TodoRequest.TodoCreateRequest request) {
        // 작성 x
    }

    @Override
    public void deleteTodo(Long todoId) {
        // 작성 x
    }

    @Override
    public void updateTodo(TodoDTO.TodoRequest.TodoUpdateRequest request, Long todoId) {
        // 작성 x
    }

    @Override
    public void completeTodo(Long todoId) {
        // 작성 x
    }
}
