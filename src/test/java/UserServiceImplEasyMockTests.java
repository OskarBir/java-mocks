import interfaces.Security;
import interfaces.User;
import interfaces.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.UserServiceImpl;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplEasyMockTests {


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
        expect(user.getPassword()).andReturn(PASSWORD1);
        replay(user);
        replay(userDAO);
        verify(userDAO);
    }

    @Test
    public void assignNewPasswordToUserWithNoPassword(){
        expect(user.getPassword()).andReturn(null);
        expect(security.md5(null)).andThrow(new NullPointerException());
        UserServiceImpl userService = new UserServiceImpl(userDAO, security);
        assertThrows(IllegalStateException.class, () -> userService.assignPassword(user));
    }

    @Test
    public void assignNewPasswordWhenDAOThrowsException() {
        expect(security.md5(user.getPassword())).andReturn(PASSWORD1);
        expect(userDAO).andThrow(new IllegalStateException());
        UserServiceImpl userService = new UserServiceImpl(userDAO, security);
        assertThrows(IllegalStateException.class, () -> userService.assignPassword(user));
    }

    @Test
    public void assignNewPasswordWhenMD5ThrowsException() {
        expect(security.md5(user.getPassword())).andReturn(PASSWORD1);
        expect(userDAO).andThrow(new IllegalStateException());
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
        expect(user.getPassword()).andReturn("123456789");
        replay(user);
        assertEquals(user.getPassword(), "123456789");
    }

    @Test
    public void constructorTest() {
        UserServiceImpl userService = new UserServiceImpl(userDAO, security);
        assertSame(userService.getUserDAO(), userDAO);
        assertSame(userService.getSecurity(), security);
    }













}
