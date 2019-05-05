import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplMockitoTests {

    private static String PASSWORD1;
    private static UserDAO userDAO;
    private static Security security;
    private static User user;
    private static UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        PASSWORD1 = "abc123";
        userDAO = mock(UserDAO.class);
        security = mock(Security.class);
        user = mock(User.class);
        userService = new UserServiceImpl(userDAO, security);
    }

    @Test
    public void notNullUserService() {
        assertNotNull(userService);
    }
    @Test
    public void assignNewPasswordToUser() throws Exception {
        when(security.md5(user.getPassword())).thenReturn(PASSWORD1);
        userService.assignPassword(user);
        verify(userDAO).updateUser(user);
    }

    @Test
    public void assignNewPasswordToUserWithNoPassword(){
        when(user.getPassword()).thenReturn(null);
        when(security.md5(null)).thenThrow(new NullPointerException());
        UserServiceImpl userService = new UserServiceImpl(userDAO, security);
        assertThrows(NullPointerException.class, () -> userService.assignPassword(user));
    }

    @Test
    public void assignNewPasswordWhenDAOThrowsException() {
        when(security.md5(user.getPassword())).thenReturn(PASSWORD1);
        doThrow(new IllegalStateException()).when(userDAO).updateUser(user);
        UserServiceImpl userService = new UserServiceImpl(userDAO, security);
        assertThrows(IllegalStateException.class, () -> userService.assignPassword(user));
    }

    @Test
    public void assignNewPasswordWhenMD5ThrowsException() {
        when(security.md5(user.getPassword())).thenReturn(PASSWORD1);
        doThrow(new IllegalStateException()).when(userDAO).updateUser(user);
        UserServiceImpl userService = new UserServiceImpl(userDAO, security);
        assertThrows(IllegalStateException.class, () -> userService.assignPassword(user));
    }

    @Test
    public void assignNewPasswordWhenThereIsNoUserThrowsException() {
        user = null;
        assertThrows(NullPointerException.class, () -> userService.assignPassword(user));
    }

    @Test
    public void isPasswordCorrect() throws Exception {
        when(user.getPassword()).thenReturn("123456789");
        userService.assignPassword(user);
        assertEquals(user.getPassword(), "123456789");
    }

    @Test
    public void constructorTest() {
        UserServiceImpl userService = new UserServiceImpl(userDAO, security);
        assertSame(userService.getUserDAO(), userDAO);
        assertSame(userService.getSecurity(), security);
    }
}
