package au.com.tla.forum;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to manage a List of Question objects.
 * 
 * @author Clinton
 * 
 */
public class QuestionManager {

    // TODO: consider using a Map keyed by questionId for faster retrieval
    private List<Question> questions;

    public QuestionManager() {
        questions = new ArrayList<Question>();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Question addQuestion(String text, String userId) {
        String id = Long.toString(SequenceGenerator.getNextSequenceNumber());
        Question question = new Question(id, text, userId);
        questions.add(question);
        return question;
    }

    public Question updateQuestion(String text, String questionId) {
        Question question = getQuestion(questionId);
        if (question != null) {
            question.setText(text);
        }
        return question;
    }

    /**
     * Return the Question instance with the given Id, else null.
     * 
     * @param questionId
     * @return the Question instance with the given Id, else null
     */
    public Question getQuestion(String questionId) {
        Question match = null;
        for (Question question : getQuestions()) {
            if (questionId.equals(question.getId())) {
                match = question;
                break;
            }
        }
        return match;
    }
}
