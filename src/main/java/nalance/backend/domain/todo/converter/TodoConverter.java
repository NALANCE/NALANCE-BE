package nalance.backend.domain.todo.converter;

import nalance.backend.domain.todo.dto.TodoDTO;
import nalance.backend.domain.todo.entity.Todo;
import nalance.backend.global.common.enums.Status;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class TodoConverter {

    public static Todo toCreateTodo(TodoDTO.TodoRequest.TodoCreateRequest request){

        Status status = toStatus(request.getStatus());

        return Todo.builder()
                .todoName(request.getTodoName())
                .duration(request.getDuration())
                .date(request.getDate())
                .status(status)
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
                .todoName(todo.getTodoName())
                .duration(todo.getDuration())
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
