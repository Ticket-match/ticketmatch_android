package de.ticket_match.ticketmatch;

public class Ticket {

    private String name;
    private String type;
    private double price;
    private int quantity;
    private String location;
    private String date;
    private String user;

    public Ticket(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Ticket(String name, String type, double price, int quantity, String location, String date, String user){
        this.setName(name);
        this.setType(type);
        this.setPrice(price);
        this.setQuantity(quantity);
        this.setLocation(location);
        this.setDate(date);
        this.setUser(user);

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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
