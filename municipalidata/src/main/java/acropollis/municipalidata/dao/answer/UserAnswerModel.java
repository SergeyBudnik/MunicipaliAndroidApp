package acropollis.municipalidata.dao.answer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
class UserAnswerModel implements Serializable {
    private Map<String, Long> answers = new HashMap<>();
}
