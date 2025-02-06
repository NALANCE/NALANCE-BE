package nalance.backend.domain.todo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import nalance.backend.domain.category.entity.Category;
import nalance.backend.domain.member.entity.Member;
import nalance.backend.global.common.BaseEntity;
import nalance.backend.global.common.enums.Status;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String todoName;

    @Column(nullable = false)
    private String startTime;

    @Column(nullable = false)
    private String endTime;

    @Column(nullable = false)
    private String formattedDuration; // "XH YM" 형식으로 저장

    @Column(nullable = false)
    private Integer duration; // 분 단위 저장

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public void updateTodo(String todoName, String startTime, String endTime, LocalDate date){
        if(todoName != null && !todoName.isBlank())
            this.todoName = todoName;
        if(startTime != null)
            this.startTime = startTime;
        if(endTime != null)
            this.endTime = endTime;
        if(date != null)
            this.date = date;
    }

    public void completeTodo(){
        this.status = Status.COMPLETED;
    }

    public void updateFormattedDuration(String formattedDuration) { this.formattedDuration = formattedDuration; }
}
