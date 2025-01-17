package nalance.backend.domain.todo.converter;

import nalance.backend.domain.todo.dto.TodoDTO;
import nalance.backend.domain.todo.entity.Todo;
import nalance.backend.global.common.enums.Status;

public class TodoConverter {

    public static Todo toCreateTodo(TodoDTO.TodoRequest.TodoCreateRequest request){

        Status status = null;
        switch (request.getStatus()){
            case 1: status = Status.COMPLETED; break;
            case 2: status = Status.INCOMPLETE; break;
        }

        return Todo.builder()
                .todoName(request.getTodoName())
                .duration(request.getDuration())
                .date(request.getDate())
                .status(status)
                .build();
    }
}
