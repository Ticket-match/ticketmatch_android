package de.ticket_match.ticketmatch.GetterAndSetter;

import junit.framework.TestCase;

import org.junit.Test;

import de.ticket_match.ticketmatch.entities.MakeDate;

/**
 * Created by goetz on 25.06.2016.
 */
public class MakeDateTest extends TestCase{

    private String date;
    private String location;
    private String name;
    private String withwoman;
    private String withman;
    private String time;
    private String type;
    private String user;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testSetDate(){
        MakeDate md = new MakeDate(date, location, name, withman, withman, time, type, user);
        String dateTest = "05.02.2016";
        md.setDate(dateTest);
        assertEquals(md.getDate(), dateTest);
    }

    @Test
    public void testSetLocation(){
        MakeDate md = new MakeDate(date, location, name, withman, withman, time, type, user);
        String locationTest = "Mannheim";
        md.setLocation(locationTest);
        assertEquals(md.getLocation(), locationTest);
    }

    @Test
    public void testSetName(){
        MakeDate md = new MakeDate(date, location, name, withman, withman, time, type, user);
        String nameTest = "Anna Meyer";
        md.setName(nameTest);
        assertEquals(md.getName(), nameTest);
    }

    @Test
    public void testSetWithman(){
        MakeDate md = new MakeDate(date, location, name, withman, withman, time, type, user);
        String withmanTest = "Yes";
        md.setWithman(withmanTest);
        assertEquals(md.isWithman(), withmanTest);
    }

    @Test
    public void testSetWithwoman(){
        MakeDate md = new MakeDate(date, location, name, withman, withman, time, type, user);
        String withwomanTest = "No";
        md.setWithwoman(withwomanTest);
        assertEquals(md.isWithwoman(), withwomanTest);
    }

    @Test
    public void testSetTime(){
        MakeDate md = new MakeDate(date, location, name, withman, withman, time, type, user);
        String timeTest = "17:00";
        md.setTime(timeTest);
        assertEquals(md.getTime(), timeTest);
    }

    @Test
    public void testSetType(){
        MakeDate md = new MakeDate(date, location, name, withman, withman, time, type, user);
        String typeTest = "EM-Ticket";
        md.setType(typeTest);
        assertEquals(md.getType(), typeTest);
    }

    @Test
    public void testSetUser(){
        MakeDate md = new MakeDate(date, location, name, withman, withman, time, type, user);
        String userTest = "12345678";
        md.setUser(userTest);
        assertEquals(md.getUser(), userTest);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
