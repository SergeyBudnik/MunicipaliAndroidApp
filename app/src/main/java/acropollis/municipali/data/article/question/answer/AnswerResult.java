package acropollis.municipali.data.article.question.answer;

import java.io.Serializable;

import lombok.Data;

@Data
public class AnswerResult implements Serializable {
    private long answerId;
    private String text;
    private double percents;
}
