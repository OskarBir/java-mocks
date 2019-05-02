public class UserServiceImpl{
    private UserDAO userDAO;
    private Security security;

    public void assignPassword(User user) throws Exception {
        String passwordMD5 = security.md5(user.getPassword());
        user.setPassword(passwordMD5);
        userDAO.updateUser(user);
    }

    public UserServiceImpl(UserDAO dao, Security security){
        this.userDAO = dao;
        this.security = security;
    }

}