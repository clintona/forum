package au.com.tla.forum;

/**
 * A class to generate identifiers.
 * 
 * In reality, this would be done at the database layer.
 * 
 * @author Clinton
 * 
 */
public class SequenceGenerator {

    private static long currentSequenceNumber = 1;

    public static synchronized long getNextSequenceNumber() {
        return currentSequenceNumber++;
    }
}
