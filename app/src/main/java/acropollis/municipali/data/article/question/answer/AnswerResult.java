package acropollis.municipali.data.article.question.answer;

import lombok.Data;

@Data
public class AnswerResult {
    private long answerId;
    private String text;
    private double percents;
}
