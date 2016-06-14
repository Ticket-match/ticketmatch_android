package de.ticket_match.ticketmatch.entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Chat {

    private String participant1;
    private String participant2;
    private ArrayList<HashMap<String,String>> messages;

    public Chat(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Chat(String participant1, String participant2, ArrayList<HashMap<String,String>> messages) {
        this.setParticipant1(participant1);
        this.setParticipant2(participant2);
        this.setMessages(messages);
    }

    public String getParticipant1() {
        return participant1;
    }

    public void setParticipant1(String participant1) {
        this.participant1 = participant1;
    }

    public String getParticipant2() {
        return participant2;
    }

    public void setParticipant2(String participant2) {
        this.participant2 = participant2;
    }

    public ArrayList<HashMap<String, String>> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<HashMap<String, String>> messages) {
        this.messages = messages;
    }
}
