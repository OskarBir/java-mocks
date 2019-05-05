import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplSelfMockTest {
    private static String PASSWORD1;
    private static UserDAO userDAO;
    private static Security security;
    private static User user;
    private static UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        PASSWORD1 = "abc123";
        userDAO = new UserDaoMock();
        security = new SecurityMock();
        user = new UserMock();
        userService = new UserServiceImpl(userDAO, security);
    }

    @Test
    public void notNullUserService() {
        assertNotNull(userService);
    }

    @Test
    public void assignNewPasswordToUser() {
        assertTrue(userDAO.updateUser(user));
        assertTrue(user.setPassword(security.md5(user.getPassword())));
    }

    @Test
    public void assignNewPasswordToUserWithNoPassword(){
        assertThrows(NullPointerException.class, () -> security.md5(null));
    }

    @Test
    public void assignNewPasswordWhenDAOThrowsException() {
        assertThrows(IllegalStateException.class, () -> userDAO.updateUser(null));
    }

    @Test
    public void assignNewPasswordWhenMD5ThrowsException() {
        assertThrows(NullPointerException.class, () -> security.md5(null));
    }

    @Test
    public void assignNewPasswordWhenThereIsNoUserThrowsException() {
        user = null;
        assertThrows(NullPointerException.class, () -> userService.assignPassword(user));
    }

    @Test
    public void isPasswordCorrect() {
        assertEquals(user.getPassword(), "123456789");
    }

    @Test
    public void constructorTest() {
        UserServiceImpl userService = new UserServiceImpl(userDAO, security);
        assertSame(userService.getUserDAO(), userDAO);
        assertSame(userService.getSecurity(), security);
    }
}

class UserDaoMock implements UserDAO{

    @Override
    public Boolean updateUser(User user) {
        if(user == null)
            throw new IllegalStateException();
        return true;
    }
}

class SecurityMock implements Security{

    @Override
    public String md5(String password) {
        if(password == null)
            throw new NullPointerException();
        return password;
    }
}

class UserMock implements User{

    @Override
    public String getPassword() {
        return "123456789";
    }

    @Override
    public Boolean setPassword(String passwordMD5) {
        return true;
    }
}
