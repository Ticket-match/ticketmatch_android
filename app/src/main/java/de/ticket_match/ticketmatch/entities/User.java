package de.ticket_match.ticketmatch.entities;

import java.util.ArrayList;
import java.util.HashMap;

public class User {

    private String firstName;
    private String lastName;
    private String gender;
    private String birthday;
    private String location;
    private ArrayList<String> interests;
    private ArrayList<HashMap<String,String>> ratings;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String firstName, String lastName, String gender, String birthday, String location, ArrayList<String> interests, ArrayList<HashMap<String,String>> ratings) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setGender(gender);
        this.setBirthday(birthday);
        this.setLocation(location);
        this.setInterests(interests);
        this.setRatings(ratings);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public ArrayList<HashMap<String, String>> getRatings() {
        return ratings;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public void setRatings(ArrayList<HashMap<String, String>> ratingitems) {
        this.ratings = ratingitems;
    }
}
