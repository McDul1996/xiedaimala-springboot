package hello.entity;

public class LoginResult extends Result<User> {
    boolean isLogin;

    public static Result success(String msg, User user, boolean isLogin) {
        return new LoginResult("ok", msg, user, isLogin);
    }

    public static Result success(String msg, boolean isLogin) {
        return new LoginResult("ok", msg, null, isLogin);
    }

    public static Result failed(String msg) {
        return new LoginResult("fail", msg, null, false);
    }

    public static Result registerSuccess(String msg, User user) {
        return new LoginResult("ok", msg, user, true);
    }

    public boolean isLogin() {
        return isLogin;
    }

    protected LoginResult(String status, String msg, User data, boolean isLogin) {
        super(status, msg, data);
        this.isLogin = isLogin;
    }


}
