package au.com.tla.forum;

import java.io.Serializable;
import java.util.Date;

public class Answer implements Serializable {

    private static final long serialVersionUID = -8021405130487035780L;

    private String id;
    private String text;
    private String userId; // the ID of the person answering the question
    private Date lastModified;

    public Answer(String id, String text, String userId) {
        this.id = id;
        setText(text);
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Answer: ").append(text);
        buf.append(" - ").append(lastModified);
        buf.append(" by ").append(userId);
        return buf.toString();
    }
}
