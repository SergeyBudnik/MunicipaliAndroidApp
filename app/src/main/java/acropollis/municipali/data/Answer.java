package acropollis.municipali.data;

import java.util.Map;

import acropollis.municipali.data.common.Language;
import lombok.Data;

@Data
public class Answer {
    private long id;
    private Map<Language, String> text;
}
