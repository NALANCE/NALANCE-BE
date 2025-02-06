package nalance.backend.domain.todo.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.category.entity.Category;
import nalance.backend.domain.category.repository.CategoryRepository;
import nalance.backend.domain.member.entity.Member;
import nalance.backend.domain.member.repository.MemberRepository;
import nalance.backend.domain.todo.converter.TodoConverter;
import nalance.backend.domain.todo.dto.TodoDTO;
import nalance.backend.domain.todo.entity.Todo;
import nalance.backend.domain.todo.repository.TodoRepository;
import nalance.backend.domain.todo.service.TodoCommandService;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.handler.CategoryException;
import nalance.backend.global.error.handler.MemberException;
import nalance.backend.global.error.handler.TodoException;
import nalance.backend.global.security.SecurityUtil;
import nalance.backend.global.validation.annotation.ExistTodo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoCommandServiceImpl implements TodoCommandService {

    private final CategoryRepository categoryRepository;
    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    @Override
    public void createTodo(TodoDTO.TodoRequest.TodoCreateRequest request) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryException(ErrorStatus.CATEGORY_NOT_FOUND));

        Todo todo = TodoConverter.toCreateTodo(request, member, category);

        setDurationFields(todo);

        todoRepository.save(todo);
    }

    private void setDurationFields(Todo todo){
        LocalTime start = todo.getStartTime();
        LocalTime end = todo.getEndTime();

        int duration = (int) Duration.between(start, end).toMinutes();
        todo.updateDuration(duration);

        todo.updateFormattedDuration(formatDuration(duration));
    }

    private String formatDuration(int durationMinutes){
        int hours = durationMinutes / 60;
        int minutes = durationMinutes % 60;

        if(hours > 0 && minutes > 0){
            return hours + "H " + minutes + "M";
        } else if (hours > 0) {
            return hours + "H";
        } else {
            return minutes + "M";
        }
    }

    @Override
    public void deleteTodo(Long todoId) {

        Long memberId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        Todo todo = todoRepository.findById(todoId)
                        .orElseThrow(() -> new TodoException(ErrorStatus.TODO_NOT_FOUND));

        if(!todo.getMember().equals(member)){
            throw new TodoException(ErrorStatus.TODO_NOT_OWNED);
        }

        todoRepository.deleteById(todoId);
    }

    @Override
    public void updateTodo(TodoDTO.TodoRequest.TodoUpdateRequest request, Long todoId) {

        Long memberId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(ErrorStatus.TODO_NOT_FOUND));

        if(!todo.getMember().equals(member)){
            throw new TodoException(ErrorStatus.TODO_NOT_OWNED);
        }

        todo.updateTodo(request.getTodoName(),request.getDuration(),request.getDate());
    }

    @Override
    public void completeTodo(Long todoId) {

        Long memberId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(ErrorStatus.TODO_NOT_FOUND));

        if(!todo.getMember().equals(member)){
            throw new TodoException(ErrorStatus.TODO_NOT_OWNED);
        }

        todo.completeTodo();
    }
}
