package code.challenge.library.exception;

public class BookNotFoundException extends DataNotFoundException {

    public BookNotFoundException(long bookId) {
        super("Book with bookId: " + bookId + " is not found");
    }
}
