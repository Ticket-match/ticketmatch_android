package de.ticket_match.ticketmatch.GetterAndSetter;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.junit.Test;

import de.ticket_match.ticketmatch.entities.Message;

/**
 * Created by goetz on 25.06.2016.
 */
public class MessageTest extends TestCase{

    private String mAuthor;
    private String mDate;
    private String mText;
    private String mTimestamp;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testSetAuthor(){
        Message message = new Message(mAuthor, mDate, mText, mTimestamp);
        String authorTest = "Hans Müller";
        message.setAuthor(authorTest);
        assertEquals(message.getAuthor(), authorTest);
    }

    @Test
    public void testSetDate(){
        Message message = new Message(mAuthor, mDate, mText, mTimestamp);
        String dateTest = "21.07.2015";
        message.setDate(dateTest);
        assertEquals(message.getDate(), dateTest);
    }

    @Test
    public void testSetText(){
        Message message = new Message(mAuthor, mDate, mText, mTimestamp);
        String textTest = "Hey, ist das Ticket noch zu haben?";
        message.setText(textTest);
        assertEquals(message.getText(), textTest);
    }

    @Test
    public void testSetTimestamp(){
        Message message = new Message(mAuthor, mDate, mText, mTimestamp);
        String timestampTest = "15:36";
        message.setTimestamp(timestampTest);
        assertEquals(message.getTimestamp(), timestampTest);
    }

    @Override
    protected TestResult createResult() {
        return super.createResult();
    }
}
