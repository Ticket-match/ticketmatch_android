package de.ticket_match.ticketmatch;

/**
 * Created by D060194 on 10.06.2016.
 */
public class MakeDate {

    private String date;
    private String location;
    private String name;
    private String withwoman;
    private String withman;
    private String time;
    private String type;
    private String user;

    MakeDate(){

    }
    MakeDate(String date, String location, String name, String time, String type, String user,  String withman, String withwoman){

        this.setDate(date);
        this.setLocation(location);
        this.setName(name);
        this.setTime(time);
        this.setUser(user);
        this.setWithman(withman);
        this.setWithwoman(withwoman);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String isWithwoman() {
        return withwoman;
    }

    public void setWithwoman(String withwoman) {
        this.withwoman = withwoman;
    }

    public String isWithman() {
        return withman;
    }

    public void setWithman(String withman) {
        this.withman = withman;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
