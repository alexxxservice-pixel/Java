/**
 * Власне виключення для некоректних вхідних даних.
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
