package nalance.backend.domain.graph.service.impl;

import lombok.RequiredArgsConstructor;
import nalance.backend.domain.graph.dto.GraphDTO.GraphResponse.*;
import nalance.backend.domain.graph.repository.GraphRepository;
import nalance.backend.domain.graph.service.GraphService;
import nalance.backend.domain.todo.entity.Todo;
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

    private final GraphRepository graphRepository;

    @Override
    public GraphDailyResponse getDailyGraph(String date) {
        // 날짜를 LocalDateTime으로 변환
        LocalDateTime startOfDay = LocalDate.parse(date).atStartOfDay();
        LocalDateTime endOfDay = LocalDate.parse(date).atTime(LocalTime.MAX);

        // 데이터 조회
        List<Todo> todos = graphRepository.findByDate(startOfDay);

        // 조회한 데이터를 이용해 응답 데이터 구성
        return GraphDailyResponse.builder()
                .date(date)
                .data(calculateCategoryRatios(todos)) // 카테고리별 비율 계산
                .build();
    }

    @Override
    public CalendarMonthlyResponse getMonthlyCalendar(int year, int month) {
        LocalDateTime startOfMonth = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.of(year, month, startOfMonth.toLocalDate().lengthOfMonth()).atTime(LocalTime.MAX);

        // 해당 월의 데이터 조회
        List<Todo> todos = graphRepository.findByDateBetween(startOfMonth, endOfMonth);

        // 카테고리별 비율 계산
        Map<String, Double> categoryRatios = calculateCategoryRatios(todos);

        // 응답 데이터 구성
        return CalendarMonthlyResponse.builder()
                .year(year)
                .month(month)
                .data(categoryRatios) // 카테고리별 비율 데이터
                .build();
    }

    private Map<String, Double> calculateCategoryRatios(List<Todo> todos) {
        if (todos == null || todos.isEmpty()) {
            return Map.of(); // 데이터가 없으면 빈 맵 반환
        }

        // 카테고리별 그룹화 및 비율 계산
        Map<String, Double> categoryRatios = todos.stream()
                .collect(Collectors.groupingBy(
                        todo -> todo.getCategory().getCategoryName(), // 카테고리명으로 그룹화
                        Collectors.summingDouble(Todo::getDuration)   // 각 카테고리의 시간 합산
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // 카테고리명
                        entry -> (entry.getValue() / todos.stream()
                                .mapToDouble(Todo::getDuration).sum()) * 100 // 비율 계산
                ));

        return categoryRatios;
    }
}
