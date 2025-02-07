package nalance.backend.domain.todo.service;

import nalance.backend.domain.todo.dto.TodoDTO;
import nalance.backend.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoQueryService {
    Optional<Todo> findTodo(Long id);

    Page<Todo> getTodoList(List<LocalDate> dateList, List<Long> categoryIdList, Integer status, Integer page);
}
