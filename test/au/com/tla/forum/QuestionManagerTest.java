package au.com.tla.forum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class QuestionManagerTest {

    public static final String TEST_USER_ID = "clinton";

    private QuestionManager manager;

    @Before
    public void setUp() throws Exception {
        manager = new QuestionManager();
    }

    @Test
    public void testGetQuestionsDefault() {
        assertNotNull(manager.getQuestions());
        assertEquals(0, manager.getQuestions().size());
    }

    @Test
    public void testSetQuestions() {
        manager.addQuestion("Q1", TEST_USER_ID);
        assertEquals(1, manager.getQuestions().size());

        List<Question> list = new ArrayList<Question>();
        list.add(new Question("2", "Q2", TEST_USER_ID));
        list.add(new Question("3", "Q3", TEST_USER_ID));

        manager.setQuestions(list);
        assertEquals(list, manager.getQuestions());
    }

    @Test
    public void testAddQuestion() {
        Question question = manager.addQuestion("Q1", TEST_USER_ID);
        assertNotNull(question);
        assertEquals("Q1", question.getText());
        assertEquals(TEST_USER_ID, question.getOwnerId());

        assertEquals(1, manager.getQuestions().size());
    }

    @Test
    public void testUpdateQuestion() {
        Question question = manager.addQuestion("Q1", TEST_USER_ID);
        String questionId = question.getId();
        String newQuestionText = "The new question";
        manager.updateQuestion(newQuestionText, questionId);
        assertEquals(newQuestionText, manager.getQuestion(questionId).getText());
    }

    @Test
    public void testGetQuestion() {
        String text = "To be, or not to be?";
        Question createdQuestion = manager.addQuestion(text, TEST_USER_ID);
        Question fetchedQuestion = manager.getQuestion(createdQuestion.getId());

        assertEquals(createdQuestion, fetchedQuestion);
        assertEquals(text, fetchedQuestion.getText());
    }

    @Test
    public void testGetQuestions() {
        manager.addQuestion("Q1", TEST_USER_ID);
        manager.addQuestion("Q2", TEST_USER_ID);
        assertEquals(2, manager.getQuestions().size());

    }
}
