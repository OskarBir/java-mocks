import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceImplTests {

    private static String PASSWORD1;
    private static String PASSWORD2;
    private static UserDAO userDAO;
    private static Security security;
    private static User user;
    private static UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        PASSWORD1 = "abc123";
        PASSWORD2 = "def456";
        userDAO = mock(UserDAO.class);
        security = mock(Security.class);
        user = mock(User.class);
        userService = new UserServiceImpl(userDAO, security);
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
        when(user.getPassword()).thenReturn(PASSWORD1);
        when(security.md5(PASSWORD1)).thenReturn(PASSWORD2);
        doThrow(new IllegalStateException()).when(userDAO).updateUser(user);
        UserServiceImpl userService = new UserServiceImpl(userDAO, security);
        assertThrows(IllegalStateException.class, () -> userService.assignPassword(user));
    }
}
