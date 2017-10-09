package code.challenge.library.exception;

public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String username) {
        super("username: " + username + " is already taken");
    }
}
