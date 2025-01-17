package nalance.backend.domain.graph.repository;

import nalance.backend.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface GraphRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByDate(LocalDateTime date); // 날짜별 데이터 조회
    @Query("SELECT t FROM Todo t WHERE t.date >= :start AND t.date <= :end")
    List<Todo> findByDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}

