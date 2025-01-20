package nalance.backend.domain.todo.entity;

import jakarta.persistence.*;
import lombok.*;
import nalance.backend.domain.category.entity.Category;
import nalance.backend.domain.member.entity.Member;
import nalance.backend.global.common.BaseEntity;
import nalance.backend.global.common.enums.Status;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public void addCategory(Category category){
        this.category = category;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String todoName;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public void updateTodo(String todoName, Integer duration, LocalDate date){
        if(todoName != null && !todoName.isBlank())
            this.todoName = todoName;
        if(duration != null)
            this.duration = duration;
        if(date != null)
            this.date = date;
    }

    public void completeTodo(){
        this.status = Status.COMPLETED;
    }
}
