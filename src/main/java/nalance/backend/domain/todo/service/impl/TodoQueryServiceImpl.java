package nalance.backend.domain.todo.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.todo.dto.TodoDTO;
import nalance.backend.domain.todo.entity.Todo;
import nalance.backend.domain.todo.repository.TodoRepository;
import nalance.backend.domain.todo.service.TodoQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static  nalance.backend.domain.todo.dto.TodoDTO.TodoResponse.*;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoQueryServiceImpl implements TodoQueryService {

    private final TodoRepository todoRepository;

    @Override
    public Optional<Todo> findTodo(Long id) {
        return todoRepository.findById(id);
    }

    @Override
    public TodoPreviewListResponse getTodoList(TodoDTO.TodoRequest.TodoQueryRequest todoQueryRequest, Integer page) {
        // 작성 x
        return null;
    }
}
