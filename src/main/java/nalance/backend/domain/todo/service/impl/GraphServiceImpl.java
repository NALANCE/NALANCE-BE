package nalance.backend.domain.todo.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.category.entity.Category;
import nalance.backend.domain.category.repository.CategoryRepository;
import nalance.backend.domain.todo.dto.GraphDTO.GraphResponse.*;
import nalance.backend.domain.todo.repository.TodoRepository;
import nalance.backend.domain.todo.service.GraphService;
import nalance.backend.domain.todo.entity.Todo;
import nalance.backend.global.common.enums.Status;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.error.handler.CategoryException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GraphServiceImpl implements GraphService {

    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public GraphDailyResponse getDailyGraph(Long memberId, String date) {
        // 날짜를 LocalDateTime으로 변환
        LocalDate startOfDay = LocalDate.parse(date);

        // 데이터 조회
        List<Todo> todos = todoRepository.findByMemberIdAndDate(memberId, startOfDay);

        // 조회한 데이터를 이용해 응답 데이터 구성
        return GraphDailyResponse.builder()
                .date(date)
                .data(calculateCategoryRatios(todos)) // 카테고리별 비율 계산
                .build();
    }

    @Override
    public CalendarMonthlyResponse getMonthlyCalendar(Long memberId, int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = LocalDate.of(year, month, startOfMonth.lengthOfMonth());

        // 해당 월의 데이터 조회
        List<Todo> todos = todoRepository.findByMemberIdAndDateBetween(memberId, startOfMonth, endOfMonth);




        // 응답 데이터 구성
        return CalendarMonthlyResponse.builder()
                .year(year)
                .month(month)
                .data(calculateCategoryRatios(todos)) // 카테고리별 데이터
                .build();
    }

    private List<CategoryDataResponse> calculateCategoryRatios(List<Todo> todos) {
        if (todos == null || todos.isEmpty()) {
            return List.of(); // 데이터가 없을 때 빈 리스트 반환
        }
        List<Todo> completedTodos = todos.stream()
                .filter(todo -> Status.COMPLETED.equals(todo.getStatus()))
                .collect(Collectors.toList());

        if (completedTodos.isEmpty()) {
            return List.of(); // COMPLETED 상태의 데이터가 없을 때 빈 리스트 반환
        }

        // COMPLETED 상태의 Todo로 카테고리별 그룹화 및 비율 계산
        double totalDuration = completedTodos.stream().mapToDouble(Todo::getDuration).sum();

        return completedTodos.stream()
                .collect(Collectors.groupingBy(
                        todo -> todo.getCategory().getCategoryName(), // 카테고리명으로 그룹화
                        Collectors.summingDouble(Todo::getDuration)   // 각 카테고리의 시간 합산
                ))
                .entrySet().stream()
                .map(entry -> {
                    String categoryName = entry.getKey();
                    Double ratio = (entry.getValue() / totalDuration) * 100;
                    String color = categoryRepository.findByMemberIdAndCategoryName(completedTodos.get(0).getMember().getMemberId(), categoryName) // ✅ 수정된 메서드 호출
                            .map(Category::getColor)
                            .orElseThrow(() -> new CategoryException(ErrorStatus.CATEGORY_NOT_FOUND));


                    return CategoryDataResponse.builder()
                            .category(categoryName)
                            .ratio(ratio)
                            .color(color)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
