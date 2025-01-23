package nalance.backend.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nalance.backend.domain.member.entity.Member;
import nalance.backend.domain.todo.entity.QTodo;
import nalance.backend.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static nalance.backend.domain.todo.converter.TodoConverter.toStatus;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    private final QTodo todo = QTodo.todo;

    @Override
    public Page<Todo> findTodos(Member member, List<LocalDate> dateList, List<Long> categoryIdList, Integer status, Pageable pageable){
        BooleanBuilder predicate = new BooleanBuilder();

        predicate.and(todo.member.eq(member));

        if(dateList != null && !dateList.isEmpty()){
            predicate.and(todo.date.in(dateList));
        }

        if(categoryIdList != null && !categoryIdList.isEmpty()){
            predicate.and(todo.category.categoryId.in(categoryIdList));
        }

        if(status != null) {
            predicate.and(todo.status.eq(toStatus(status)));
        }

        List<Todo> results = jpaQueryFactory
                .selectFrom(todo)
                .where(predicate)
                .orderBy(todo.date.asc())
                .offset(pageable.getOffset()) // 시작 인덱스
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch();

        Long result = jpaQueryFactory
                .select(todo.count())
                .from(todo)
                .where(predicate)
                .fetchOne();

        long total = (result != null)?result:0L;

        return new PageImpl<>(results, pageable, total);
    }
}
