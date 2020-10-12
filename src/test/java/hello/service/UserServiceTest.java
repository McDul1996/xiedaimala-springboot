package hello.service;

import hello.entity.User;
import hello.dao.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserMapper mockMapper;
    @Mock
    BCryptPasswordEncoder mockEncoder;
    @InjectMocks
    UserService userService;

    @Test
    public void testSave() {
        // 调用userService;
        // 验证userService将请求转发给了userMapper;
        when(mockEncoder.encode("myPassword")).thenReturn("myEncoderPassword");
        userService.save("myUsername", "myPassword");
        verify(mockMapper).save("myUsername", "myEncoderPassword");
    }

    @Test
    public void testGetUserByUsername() {
        userService.getUserByUsername("myUsername");
        verify(mockMapper).findUserByUsername("myUsername");
    }

    @Test
    public void throwUsernameNotFound() {
        Assertions.assertThrows(UsernameNotFoundException.class, () ->
                userService.loadUserByUsername("myUsername"));
    }

    @Test
    public void returnUserDetailsWhenUserFound() {
        when(mockMapper.findUserByUsername("myUsername")).
                thenReturn(new User(123, "myUser123", "myPassword"));
        UserDetails userDetails = userService.loadUserByUsername("myUsername");
        Assertions.assertEquals("myUsername",userDetails.getUsername());
        Assertions.assertEquals("myPassword",userDetails.getPassword());
    }
}