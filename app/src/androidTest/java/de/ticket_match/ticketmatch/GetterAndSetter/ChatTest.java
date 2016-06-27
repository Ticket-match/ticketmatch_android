package de.ticket_match.ticketmatch.GetterAndSetter;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import de.ticket_match.ticketmatch.entities.Chat;

/**
 * Created by goetz on 25.06.2016.
 */
public class ChatTest extends TestCase{

    private String participant1;
    private String participant2;
    private ArrayList<HashMap<String,String>> messages;
    private HashMap<String,String> lastMessage;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testSetParticipant1(){
        Chat chat = new Chat(participant1, participant2, messages, lastMessage);
        String participant1Test = "Teilnehmer1";
        chat.setParticipant1(participant1Test);
        assertEquals(chat.getParticipant1(), participant1Test);
    }

    @Test
    public void testSetParticipant2(){
        Chat chat = new Chat(participant1, participant2, messages, lastMessage);
        String participant2Test = "Teilnehmer1";
        chat.setParticipant2(participant2Test);
        assertEquals(chat.getParticipant2(), participant2Test);
    }

    @Test
    public void testSetMessages(){
        messages = new ArrayList<>();
        Chat chat = new Chat(participant1, participant2, messages, lastMessage);
        String key = "1234";
        String value = "5";

        HashMap<String, String> hm = new HashMap<>();
        hm.put(key,  value);
        messages.add(0, hm);

        chat.setMessages(messages);

        assertEquals(chat.getMessages().size(), messages.size());
        assertEquals(chat.getMessages().get(0), hm);
        assertEquals(chat.getMessages(), messages);
    }

    @Test
    public void testSetLastMessage(){
        lastMessage = new HashMap<>();
        Chat chat = new Chat(participant1, participant2, messages, lastMessage);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("key", "value");
        lastMessage.put("key", "value");

        chat.setLastMessage(lastMessage);

        assertEquals(chat.getLastMessage(), hm);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
