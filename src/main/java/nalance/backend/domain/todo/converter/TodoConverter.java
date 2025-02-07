package nalance.backend.domain.todo.converter;

import nalance.backend.domain.category.entity.Category;
import nalance.backend.domain.member.entity.Member;
import nalance.backend.domain.todo.dto.TodoDTO;
import nalance.backend.domain.todo.entity.Todo;
import nalance.backend.global.common.enums.Status;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class TodoConverter {

    public static Todo toCreateTodo(TodoDTO.TodoRequest.TodoCreateRequest request, Member member, Category category){

        Status status = toStatus(request.getStatus());

        return Todo.builder()
                .todoName(request.getTodoName())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .date(request.getDate())
                .status(status)
                .member(member)
                .category(category)
                .build();
    }

    public static Status toStatus(Integer num){

        Status status = null;
        switch(num){
            case 1: status = Status.COMPLETED; break;
            case 2: status = Status.INCOMPLETE; break;
        }

        return status;
    }

    public static TodoDTO.TodoResponse.TodoPreviewResponse toTodoPreviewResponse(Todo todo){
        return TodoDTO.TodoResponse.TodoPreviewResponse.builder()
                .todoId(todo.getTodoId())
                .todoName(todo.getTodoName())
                .startTime(todo.getStartTime())
                .endTime(todo.getEndTime())
                .formattedDuration(todo.getFormattedDuration())
                .date(todo.getDate())
                .status(todo.getStatus())
                .build();
    }

    public static TodoDTO.TodoResponse.TodoPreviewListResponse toTodoPreviewListResponse(Page<Todo> todoList){
        List<TodoDTO.TodoResponse.TodoPreviewResponse> todoPreviewResponseList = todoList.stream()
                .map(TodoConverter::toTodoPreviewResponse).collect(Collectors.toList());

        return TodoDTO.TodoResponse.TodoPreviewListResponse.builder()
                .todoList(todoPreviewResponseList)
                .listSize(todoPreviewResponseList.size())
                .totalPage(todoList.getTotalPages())
                .totalElements(todoList.getTotalElements())
                .isFirst(todoList.isFirst())
                .isLast(todoList.isLast())
                .build();
    }
}
