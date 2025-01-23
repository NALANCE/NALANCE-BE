package nalance.backend.domain.todo.repository;

import nalance.backend.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long>,TodoRepositoryCustom {

    // 특정 날짜에 해당 사용자(memberId)의 데이터 조회
    @Query("SELECT t FROM Todo t WHERE t.member.memberId= :memberId AND t.date = :date")
    List<Todo> findByMemberIdAndDate(@Param("memberId") Long memberId, @Param("date") LocalDate date);

    // 특정 기간에 해당 사용자(memberId)의 데이터 조회
    @Query("SELECT t FROM Todo t WHERE t.member.memberId = :memberId AND t.date BETWEEN :start AND :end")
    List<Todo> findByMemberIdAndDateBetween(
            @Param("memberId") Long memberId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);
}
