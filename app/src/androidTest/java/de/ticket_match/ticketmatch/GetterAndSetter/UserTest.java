package de.ticket_match.ticketmatch.GetterAndSetter;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import de.ticket_match.ticketmatch.entities.User;

/**
 * Created by goetz on 25.06.2016.
 */
public class UserTest extends TestCase{

    private String firstName;
    private String lastName;
    private String gender;
    private String birthday;
    private String location;
    private ArrayList<String> interests;
    private ArrayList<HashMap<String,String>> ratings;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testSetFirstName(){
        User user = new User(firstName, lastName, gender, birthday, location, interests, ratings);
        String firstNameTest = "Andreas";
        user.setFirstName(firstNameTest);
        assertEquals(user.getFirstName(), firstNameTest);
    }

    @Test
    public void testSetLastName(){
        User user = new User(firstName, lastName, gender, birthday, location, interests, ratings);
        String lastNameTest = "Becker";
        user.setLastName(lastNameTest);
        assertEquals(user.getLastName(), lastNameTest);
    }

    @Test
    public void testSetGender(){
        User user = new User(firstName, lastName, gender, birthday, location, interests, ratings);
        String genderTest = "Male";
        user.setGender(genderTest);
        assertEquals(user.getGender(), genderTest);
    }

    @Test
    public void testSetBirthday(){
        User user = new User(firstName, lastName, gender, birthday, location, interests, ratings);
        String birthdayTest = "06.09.1992";
        user.setBirthday(birthdayTest);
        assertEquals(user.getBirthday(), birthdayTest);
    }

    @Test
    public void testSetLocation(){
        User user = new User(firstName, lastName, gender, birthday, location, interests, ratings);
        String locationTest = "Heidelberg";
        user.setLocation(locationTest);
        assertEquals(user.getLocation(), locationTest);
    }

    @Test
    public void testSetInterests(){
        interests = new ArrayList<>();
        User user = new User(firstName, lastName, gender, birthday, location, interests, ratings);
        String interestsTest = "Interest";

        for (int i = 0; i < 3; i++){
            interests.add(i, interestsTest + i);
        }

        user.setInterests(interests);

        //tests, whether arraylist "interest" contains the same values
        assertEquals(user.getInterests().get(0), "Interest0");
        assertEquals(user.getInterests().get(1), "Interest1");
        assertEquals(user.getInterests().get(2), "Interest2");
        assertEquals(user.getInterests(), interests);
    }

    @Test
    public void testSetRatings(){
        ratings = new ArrayList<>();
        User user = new User(firstName, lastName, gender, birthday, location, interests, ratings);
        String key = "1234";
        String value = "5";

        HashMap<String, String> hm = new HashMap<>();
        hm.put(key,  value);
        ratings.add(0, hm);

        user.setRatings(ratings);

        assertEquals(user.getRatings().size(), ratings.size());
        assertEquals(user.getRatings().get(0), hm);
        assertEquals(user.getRatings(), ratings);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
