package code.challenge.library.exception;

public class UserNotFoundException extends DataNotFoundException {

    public UserNotFoundException(String username) {
        super("User with username: " + username + " is not found");
    }
}
