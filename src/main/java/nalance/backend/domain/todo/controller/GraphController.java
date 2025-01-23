package nalance.backend.domain.todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nalance.backend.domain.todo.dto.GraphDTO.GraphResponse.*;
import nalance.backend.domain.todo.service.GraphService;
import nalance.backend.global.error.ApiResponse;
import nalance.backend.global.security.SecurityUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v0/graph")
@Tag(name = "그래프 관련 컨트롤러", description = "그래프 데이터를 조회하는 API")
public class GraphController {

    private final GraphService graphService;

    @GetMapping("/daily/{date}")
    @Operation(summary = "일별 그래프 조회 API", description = "특정 날짜의 그래프 데이터를 조회하는 API입니다.(Bearer Token 인증 필요)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "GRAPH4001", description = "그래프 데이터를 조회할 수 없습니다.")
    })
    public ApiResponse<GraphDailyResponse> getDailyGraph(
            @Parameter(description = "조회할 날짜 (yyyy-MM-dd 형식)", example = "2025-01-13")
            @PathVariable String date) {
        // 현재 로그인된 회원의 ID 가져오기
        Long memberId = SecurityUtil.getCurrentMemberId();
        var response = graphService.getDailyGraph(memberId,date);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/calendar/monthly/{year}/{month}")
    @Operation(summary = "월별 캘린더 데이터 조회 API", description = "특정 연도와 월의 캘린더 데이터를 조회하는 API입니다.(Bearer Token 인증 필요)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "GRAPH4002", description = "캘린더 데이터를 조회할 수 없습니다.")
    })
    public ApiResponse<CalendarMonthlyResponse> getMonthlyCalendar(
            @PathVariable int year,
            @PathVariable int month) {
        // 현재 로그인된 회원의 ID 가져오기
        Long memberId = SecurityUtil.getCurrentMemberId();
        var response = graphService.getMonthlyCalendar(memberId, year, month);
        return ApiResponse.onSuccess(response);
    }
}
