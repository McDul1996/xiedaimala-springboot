package hello.controller;

import hello.entity.Result;
import hello.entity.User;
import hello.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {
    private UserService userService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }


    @GetMapping("/auth")
    @ResponseBody
    public Object auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User userByUsername = userService.getUserByUsername(authentication == null ? null : authentication.getName());
        if (userByUsername == null) {
            return new Result("ok", "用户没有登录", false);
        }
        return new Result("ok", null, userByUsername, true);
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        if (username == null || password == null) {
            return Result.failed("用户名或密码不能为空");
        }
        if (username.length() < 1 || username.length() > 15) {
            return Result.failed("invalid username");
        }
        if (password.length() < 6 || password.length() > 15) {
            return Result.failed("invalid username");
        }
        User user = userService.getUserByUsername(username);
        if (user == null) {
            userService.save(username, password);
            return Result.success("success!");
        }
        return Result.failed("user already exists");
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return Result.failed("用户不存在");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return new Result("ok", "登录成功", userService.getUserByUsername(username), true);
        } catch (BadCredentialsException e) {
            return Result.failed("密码不正确");
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Result logout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return Result.failed("用户没有登陆");
        }
        SecurityContextHolder.clearContext();
        return Result.success("success!");
    }

}
