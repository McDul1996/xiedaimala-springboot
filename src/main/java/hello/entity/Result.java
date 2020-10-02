package hello.entity;

public class Result {
    String status;
    String msg;
    User data;
    boolean isLogin;

    public static Result failed(String msg) {
        return new Result("fail", msg, false);
    }

    public static Result success(String msg) {
        return new Result("ok", msg, true);
    }

    public Result(String status, String msg, boolean isLogin) {
        this(status, msg, null, isLogin);
    }

    public Result(String status, String msg, User data, boolean isLogin) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.isLogin = isLogin;
    }

    public User getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isLogin() {
        return isLogin;
    }
}