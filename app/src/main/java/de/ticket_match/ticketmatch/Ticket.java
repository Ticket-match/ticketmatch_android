package de.ticket_match.ticketmatch;

import java.util.HashMap;

public class Ticket {

    private String name;
    private String type;
    private HashMap<String, String> price;
    private String quantity;
    private String location;
    private String date;
    private String user;
    private String time;

    public Ticket(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Ticket(String name, String type, HashMap<String, String> price, String quantity, String location, String date, String user, String time){
        this.setName(name);
        this.setType(type);
        this.setPrice(price);
        this.setQuantity(quantity);
        this.setLocation(location);
        this.setDate(date);
        this.setUser(user);
        this.setTime(time);

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, String> getPrice() {
        return price;
    }

    public void setPrice(HashMap<String, String> price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
