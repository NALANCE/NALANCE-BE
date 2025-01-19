package nalance.backend.domain.todo.service;

import nalance.backend.domain.todo.dto.TodoDTO;
import nalance.backend.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TodoQueryService {
    Optional<Todo> findTodo(Long id);

    Page<Todo> getTodoList(TodoDTO.TodoRequest.TodoQueryRequest todoQueryRequest, Integer page);
}
