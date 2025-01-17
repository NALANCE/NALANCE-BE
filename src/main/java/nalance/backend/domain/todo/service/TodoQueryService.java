package nalance.backend.domain.todo.service;

import nalance.backend.domain.todo.dto.TodoDTO;
import nalance.backend.domain.todo.entity.Todo;

import java.util.Optional;

public interface TodoQueryService {
    Optional<Todo> findTodo(Long id);

    TodoDTO.TodoResponse.TodoPreviewListResponse getTodoList(TodoDTO.TodoRequest.TodoQueryRequest todoQueryRequest, Integer page);
}
