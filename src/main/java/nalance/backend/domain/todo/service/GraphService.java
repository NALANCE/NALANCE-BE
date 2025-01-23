package nalance.backend.domain.todo.service;

import nalance.backend.domain.todo.dto.GraphDTO.GraphResponse.*;

public interface GraphService {
    GraphDailyResponse getDailyGraph(Long memberId, String date);
    CalendarMonthlyResponse getMonthlyCalendar(Long memberId, int year, int month);
}
