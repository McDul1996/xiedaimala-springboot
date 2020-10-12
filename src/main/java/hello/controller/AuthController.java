package hello.controller;

import hello.entity.LoginResult;
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
    public Result auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User userByUsername = userService.getUserByUsername(authentication == null ? null : authentication.getName());
        if (userByUsername == null) {
            return LoginResult.success("用户没有登录", false);
        }
        return LoginResult.success(null, userByUsername, true);
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        if (username == null || password == null) {
            return LoginResult.failed("用户名或密码不能为空");
        }
        if (username.length() < 1 || username.length() > 15) {
            return LoginResult.failed("invalid username");
        }
        if (password.length() < 6 || password.length() > 15) {
            return LoginResult.failed("invalid username");
        }
        User user = userService.getUserByUsername(username);
        if (user == null) {
            userService.save(username, password);
            User SuccessUser = userService.getUserByUsername(username);
            return LoginResult.registerSuccess("success!", SuccessUser);
        }
        return LoginResult.failed("user already exists");
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
            return LoginResult.failed("用户不存在");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return LoginResult.success("登录成功", userService.getUserByUsername(username), true);
        } catch (BadCredentialsException e) {
            return LoginResult.failed("密码不正确");
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Result logout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return LoginResult.failed("用户没有登陆");
        }
        SecurityContextHolder.clearContext();
        return LoginResult.success("success!",true);
    }

}
