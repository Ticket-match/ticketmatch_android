package de.ticket_match.ticketmatch.entities;

public class Message {
    private String mAuthor;
    private String mDate;
    private String mText;
    private String mTimestamp;

    public Message() {

    }

    public Message(String mAuthor, String mDate, String mTimestamp, String mText) {
        this.mAuthor = mAuthor;
        this.mDate = mDate;
        this.mTimestamp = mTimestamp;
        this.mText = mText;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        this.mTimestamp = timestamp;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }


    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}