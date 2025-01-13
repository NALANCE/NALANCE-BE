package nalance.backend.domain.graph.service;

import nalance.backend.domain.graph.dto.GraphDTO.GraphResponse.*;

public interface GraphService {
    GraphDailyResponse getDailyGraph(String date);
    CalendarMonthlyResponse getMonthlyCalendar(int year, int month);
}
