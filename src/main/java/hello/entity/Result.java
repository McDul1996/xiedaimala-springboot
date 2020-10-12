package hello.entity;

public abstract class Result<T> {
    String status;
    String msg;
    T data;

//    public static Result failed(String msg) {
//        return new Result("fail", msg, false);
//    }
//
//    public static Result success(String msg) {
//        return new Result("ok", msg, true);
//    }

    protected Result(String status, String msg) {
        this(status, msg, null);
    }

    protected Result(String status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

}