package de.ticket_match.ticketmatch.GetterAndSetter;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.HashMap;

import de.ticket_match.ticketmatch.entities.Ticket;

/**
 * Created by goetz on 25.06.2016.
 */
public class TicketTest extends TestCase{

    private String name;
    private String type;
    private HashMap<String, String> price;
    private String quantity;
    private String location;
    private String date;
    private String user;
    private String time;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testSetName(){
        Ticket ticket = new Ticket(name, type, price, quantity, location, date, user, time);
        String nameTest = "Konzert Mc Fitti";
        ticket.setName(nameTest);
        assertEquals(ticket.getName(), nameTest);
    }

    @Test
    public void testSetType(){
        Ticket ticket = new Ticket(name, type, price, quantity, location, date, user, time);
        String typeTest = "Konzertticket";
        ticket.setType(typeTest);
        assertEquals(ticket.getType(), typeTest);
    }

    @Test
    public void testSetPrice(){
        price = new HashMap<>();
        Ticket ticket = new Ticket(name, type, price, quantity, location, date, user, time);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("key", "value");
        price.put("key", "value");

        ticket.setPrice(price);

        assertEquals(ticket.getPrice(), hm);
    }

    @Test
    public void testSetQuantity(){
        Ticket ticket = new Ticket(name, type, price, quantity, location, date, user, time);
        String quantityTest = "3";
        ticket.setQuantity(quantityTest);
        assertEquals(ticket.getQuantity(), quantityTest);
    }

    @Test
    public void testSetLocation(){
        Ticket ticket = new Ticket(name, type, price, quantity, location, date, user, time);
        String locationTest = "Frankfurt/Main";
        ticket.setLocation(locationTest);
        assertEquals(ticket.getLocation(), locationTest);
    }

    @Test
    public void testSetDate(){
        Ticket ticket = new Ticket(name, type, price, quantity, location, date, user, time);
        String dateTest = "26.05.2016";
        ticket.setDate(dateTest);
        assertEquals(ticket.getDate(), dateTest);
    }

    @Test
    public void testSetUser(){
        Ticket ticket = new Ticket(name, type, price, quantity, location, date, user, time);
        String userTest = "Anna Meyer";
        ticket.setUser(userTest);
        assertEquals(ticket.getUser(), userTest);
    }

    @Test
    public void testSetTime(){
        Ticket ticket = new Ticket(name, type, price, quantity, location, date, user, time);
        String timeTest = "17:00";
        ticket.setTime(timeTest);
        assertEquals(ticket.getTime(), timeTest);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
