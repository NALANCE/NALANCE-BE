package nalance.backend.domain.todo.service;

import nalance.backend.domain.todo.dto.GraphDTO.GraphResponse.*;

public interface GraphService {
    GraphDailyResponse getDailyGraph(String date);
    CalendarMonthlyResponse getMonthlyCalendar(int year, int month);
}
