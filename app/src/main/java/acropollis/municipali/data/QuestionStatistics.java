package acropollis.municipali.data;

import java.util.Map;

import lombok.Data;

@Data
public class QuestionStatistics {
    private Map<Long, Long> statistics;
}
