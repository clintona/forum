package au.com.tla.forum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Question implements Serializable {
    private static final long serialVersionUID = 350692709468480086L;

    private String id;
    private String text;
    private Date lastModified;
    private String ownerId;
    List<Answer> answers; // lazily loaded

    public Question(String id, String text, String userId) {
        this.id = id;
        setText(text);
        this.ownerId = userId;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        setLastModified(new Date());
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public List<Answer> getAnswers() {
        if (answers == null) {
            answers = new ArrayList<Answer>();
        }
        return answers;
    }

    public void addAnswer(Answer answer) {
        getAnswers().add(answer);
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Return true if the userId is the ownerId, else false.
     * 
     * @param userId
     * @return true if the userId is the ownerId, else false
     */
    public boolean isOwner(String userId) {
        return getOwnerId().equals(userId);
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Question #" + id);
        buf.append(": ").append(text);
        buf.append(" - ").append(lastModified);
        buf.append(" by ").append(ownerId);
        return buf.toString();
    }

    /**
     * @return a formatted String of answers, one answer per line.
     */
    public String getAnswersAsString() {
        // In java7, can use System.lineSeparator()
        String newLine = System.getProperty("line.separator");
        StringBuilder buf = new StringBuilder();
        for (Answer answer : getAnswers()) {
            buf.append(answer.toString()).append(newLine);
        }
        return buf.toString();
    }
}
