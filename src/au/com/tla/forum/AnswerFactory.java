package au.com.tla.forum;

/**
 * A class to create new Answer instances.
 * 
 * @author Clinton
 * 
 */
public class AnswerFactory {

    public static Answer createAnswer(String text, String userId) {
        long sequenceNum = SequenceGenerator.getNextSequenceNumber();
        String id = Long.toString(sequenceNum);
        return new Answer(id, text, userId);
    }
}
