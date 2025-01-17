package nalance.backend.domain.todo.service;

import nalance.backend.domain.todo.dto.TodoDTO;

public interface TodoCommandService {

    void createTodo(TodoDTO.TodoRequest.TodoCreateRequest request);

    void deleteTodo(Long todoId);

    void updateTodo(TodoDTO.TodoRequest.TodoUpdateRequest request, Long todoId);

    void completeTodo(Long todoId);
}
