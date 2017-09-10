package acropollis.municipalidata.dto.article.question.answer;

import java.io.Serializable;

import lombok.Data;

@Data
public class TranslatedAnswer implements Serializable {
    private long id;
    private String text;
}
