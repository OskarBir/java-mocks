package interfaces;

public interface User {
    String getPassword();

    Boolean setPassword(String passwordMD5);
}
