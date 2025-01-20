package nalance.backend.domain.todo.repository;

import nalance.backend.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long>,TodoRepositoryCustom {

    List<Todo> findByDate(LocalDate date); // 날짜별 데이터 조회
    @Query("SELECT t FROM Todo t WHERE t.date >= :start AND t.date <= :end")
    List<Todo> findByDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

}
