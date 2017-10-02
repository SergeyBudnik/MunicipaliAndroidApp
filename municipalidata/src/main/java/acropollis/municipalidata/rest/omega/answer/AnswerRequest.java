package acropollis.municipalidata.rest.omega.answer;

import lombok.Data;

@Data
public class AnswerRequest {
    private long articleId;
    private long questionId;
    private long answerId;
    private String userAuthToken;
}