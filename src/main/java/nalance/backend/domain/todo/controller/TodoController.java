package nalance.backend.domain.todo.controller;

import static  nalance.backend.domain.todo.dto.TodoDTO.TodoRequest.*;
import static  nalance.backend.domain.todo.dto.TodoDTO.TodoResponse.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nalance.backend.domain.todo.converter.TodoConverter;
import nalance.backend.domain.todo.entity.Todo;
import nalance.backend.domain.todo.service.TodoCommandService;
import nalance.backend.domain.todo.service.TodoQueryService;
import nalance.backend.global.error.ApiResponse;
import nalance.backend.global.validation.annotation.CheckPage;
import nalance.backend.global.validation.annotation.ExistTodo;
import nalance.backend.global.validation.validator.CheckPageValidator;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v0/todos")
@Tag(name = "Todo 관련 컨트롤러")
public class TodoController {

    private final TodoQueryService todoQueryService;
    private final TodoCommandService todoCommandService;
    private final CheckPageValidator checkPageValidator;

    @PostMapping("/")
    @Operation(summary = "Todo 생성 API", description = """
            Todo를 생성하는 API입니다. 유저의 토큰을 헤더로 주세요.
            
            status 값은 1 또는 2로 주세요. (1:completed, 2:incomplete)
            """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TODO4002", description = "TODO 생성에 실패했습니다.")
    })
    public ApiResponse<String> createTodo(@RequestBody @Valid TodoCreateRequest todoCreateRequest) {
        todoCommandService.createTodo(todoCreateRequest);
        return ApiResponse.onSuccess("Todo 생성 성공");
    }

    @DeleteMapping("/{todoId}")
    @Operation(summary = "Todo 삭제 API", description = "Todo를 삭제하는 API입니다. 유저의 토큰을 헤더로 주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TODO4003", description = "TODO 삭제에 실패했습니다.")
    })
    @Parameters({
            @Parameter(name = "todoId", description = "todo의 아이디, path variable 입니다."),
    })
    public ApiResponse<String> deleteTodo(@ExistTodo @PathVariable(name = "todoId") Long todoId) {
        todoCommandService.deleteTodo(todoId);
        return ApiResponse.onSuccess("Todo 삭제 성공");
    }

    @PatchMapping("/{todoId}")
    @Operation(summary = "Todo 수정 API", description = "Todo를 수정하는 API입니다. 유저의 토큰을 헤더로 주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TODO4004", description = "TODO 수정에 실패했습니다.")
    })
    @Parameters({
            @Parameter(name = "todoId", description = "todo의 아이디, path variable 입니다."),
    })
    public ApiResponse<String> updateTodo(@RequestBody @Valid TodoUpdateRequest todoUpdateRequest,
                                          @ExistTodo @PathVariable(name = "todoId") Long todoId) {
        //헤더 토큰 받기
        todoCommandService.updateTodo(todoUpdateRequest, todoId);
        return ApiResponse.onSuccess("Todo 수정 성공");
    }

    @PatchMapping("/{todoId}/complete")
    @Operation(summary = "Todo 완료 API", description = "Todo를 완료하는 API입니다. 유저의 토큰을 헤더로 주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TODO4005", description = "TODO 완료에 실패했습니다.")
    })
    @Parameters({
            @Parameter(name = "todoId", description = "todo의 아이디, path variable 입니다."),
    })
    public ApiResponse<String> completeTodo(@ExistTodo @PathVariable(name = "todoId") Long todoId) {
        todoCommandService.completeTodo(todoId);
        return ApiResponse.onSuccess("Todo 완료 성공");
    }

    @PatchMapping("/")
    @Operation(summary = "Todo 조회 API", description = """
            Todo를 조회하는 API입니다. 유저의 토큰을 헤더로 주세요.
            
            status 값은 1 또는 2로 주세요. (1:completed, 2:incomplete)
            """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TODO4006", description = "TODO 조회에 실패했습니다.")
    })
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호, 1번이 1 페이지 입니다.")
    })
    public ApiResponse<TodoPreviewListResponse> getTodoList(@RequestBody @Valid TodoQueryRequest todoQueryRequest,
                                                            @CheckPage@RequestParam(name = "page") Integer page) {
        Integer validatedPage = checkPageValidator.validateAndTransformPage(page);
        Page<Todo> todoList = todoQueryService.getTodoList(todoQueryRequest, validatedPage);
        return ApiResponse.onSuccess(TodoConverter.toTodoPreviewListResponse(todoList));
    }



}
