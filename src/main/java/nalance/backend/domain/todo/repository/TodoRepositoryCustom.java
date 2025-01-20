package nalance.backend.domain.todo.repository;

import nalance.backend.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepositoryCustom {
    Page<Todo> findTodos(List<LocalDate> dateList, List<Long> categoryIdList, Integer status, Pageable pageable);
}
