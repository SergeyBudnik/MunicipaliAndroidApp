package acropollis.municipali.data;

import lombok.Data;

@Data
public class AnswerRequest {
    private String userId;
    private long articleId;
    private long questionId;
    private long answerId;
}