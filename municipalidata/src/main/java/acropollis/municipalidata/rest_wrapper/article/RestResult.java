package acropollis.municipalidata.rest_wrapper.article;

import lombok.Getter;

public class RestResult<T> {
    public enum Result {
        SUCCESS, FAILURE
    }

    @Getter private Result result;
    @Getter private T data;

    public static <T> RestResult<T> success(T data) {
        return new RestResult<T>(Result.SUCCESS, data);
    }

    public static <T> RestResult<T> failure() {
        return new RestResult<T>(Result.FAILURE, null);
    }

    private RestResult(Result result, T data) {
        this.result = result;
        this.data = data;
    }

    public boolean isSuccessfull() {
        return result == Result.SUCCESS;
    }
}
