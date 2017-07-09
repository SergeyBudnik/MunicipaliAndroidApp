package acropollis.municipali.data.rest.request;

import java.io.Serializable;

import lombok.Data;

@Data
public class AnswerRequest implements Serializable {
    private long articleId;
    private long questionId;
    private long answerId;
    private String userAuthToken;
}
