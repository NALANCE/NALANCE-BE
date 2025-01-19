package nalance.backend.domain.todo.repository;

import nalance.backend.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>,TodoRepositoryCustom {
}
