package xyz.yunzhongyan.www.domain.vo;

import lombok.Data;

@Data
public class Result {
    public static final int OK = 0; // 成功
    public static final int FAILURE = 1; // 失败

    private int code;
    private String message;
    private Object data;

    public Result code(int code) {
        this.code = code;
        return this;
    }

    public Result message(String message) {
        this.message = message;
        return this;
    }

    public Result data(Object data) {
        this.data = data;
        return this;
    }

    public static Result success() {
        return new Result().code(OK);
    }

    public static Result success(Object data) {
        return new Result().code(OK).data(data);
    }

    public static Result fail(String message) {
        return new Result().code(FAILURE).message(message);
    }


    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
