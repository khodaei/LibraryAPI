package code.challenge.library.exception;

public class UserBookNotFoundException extends DataNotFoundException {
    public UserBookNotFoundException(String username, long bookId) {
        super("Could not find book with bookId: " + bookId + " in user library with username: " + username);
    }
}
