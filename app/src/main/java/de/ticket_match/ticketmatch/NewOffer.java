package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import de.ticket_match.ticketmatch.entities.Ticket;

public class NewOffer extends AppCompatActivity {

    private Calendar ticketDate;

    EditText mEventName;
    EditText mEventLoation;
    EditText mNumberOfTickets;
    TextView mDate;
    TextView mTime;
    EditText mPrice;
    CheckBox mCheckBoxPrice;
    Spinner mEventType;
    Spinner mCurrency;

    ArrayAdapter<CharSequence> adapterEventType;
    ArrayAdapter<CharSequence> adapterCurrency;

    //Used to edit a ticket - see Offer_Overview, method: btn_ticket_edit
    Boolean editTicket = false;
    String ticketKey;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public String getTicketKey() {
        return ticketKey;
    }

    public void setTicketKey(String ticketKey) {
        this.ticketKey = ticketKey;
    }

    public Boolean isEditTicket() {
        return editTicket;
    }

    public void setEditTicket(Boolean editTicket) {
        this.editTicket = editTicket;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_offer);


        //Assign all Input Fields
        mEventName = (EditText)findViewById(R.id.eventname);
        mEventLoation = (EditText)findViewById(R.id.eventlocation);
        mNumberOfTickets = (EditText)findViewById(R.id.numberoftickets);
        mDate = (TextView)findViewById(R.id.date);
        mPrice = (EditText)findViewById(R.id.price);
        mTime = (TextView)findViewById(R.id.time);
        mCheckBoxPrice = (CheckBox)findViewById(R.id.checkbox_price);
        mEventType = (Spinner)findViewById(R.id.event_type);
        mCurrency = ((Spinner)findViewById(R.id.currency));

        //ArrayAdapter for the Spinner
        adapterEventType = ArrayAdapter.createFromResource(this, R.array.event_type, R.layout.support_simple_spinner_dropdown_item);
        adapterEventType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mEventType.setAdapter(adapterEventType);

        adapterCurrency = ArrayAdapter.createFromResource(this, R.array.currency, R.layout.support_simple_spinner_dropdown_item);
        adapterCurrency.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mCurrency.setAdapter(adapterCurrency);

        //Datepicker
        mDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TicketMatch)getApplication()).minimizeKeyboard(v);
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(getParent(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (dayOfMonth < c.get(Calendar.DAY_OF_MONTH) || monthOfYear < c.get(Calendar.MONTH) || year < c.get(Calendar.YEAR)) {
                            Toast.makeText(getApplicationContext(),"Please don't use a past date!", Toast.LENGTH_SHORT).show();
                            dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                            monthOfYear = c.get(Calendar.MONTH);
                            year = c.get(Calendar.YEAR);
                        }
                        int month = monthOfYear+1;
                        String date = (dayOfMonth<10?"0"+dayOfMonth:dayOfMonth) + "." + (month<10?"0"+month:month) + "." + year;
                        ((TextView)((TabHost)((MainActivityTabHost)getParent()).findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.date)).setText(date);
                        ticketDate = new GregorianCalendar(year, monthOfYear, dayOfMonth-1);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
                return false;
            }
        });

        //Timepicker
        mTime.setOnTouchListener(new View.OnTouchListener(){
            String time = "";
            @Override
            public boolean onTouch(View v, MotionEvent event){
                ((TicketMatch)getApplication()).minimizeKeyboard(v);
                Calendar c = Calendar.getInstance();
                TimePickerDialog tpd = new TimePickerDialog(getParent(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //The starting null is not shown. Therefore we have added the null in the string variable
                        if(minute<10){
                            time = hourOfDay + ":0" + minute;
                        }else{
                            time = hourOfDay + ":" + minute;
                        }

                        ((TextView)((TabHost)((MainActivityTabHost)getParent()).findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.time)).setText(time);
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                tpd.show();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    //if ticket is for free price enter field is hided
    public void cb_clicked(View view){

        boolean checked = ((CheckBox)view).isChecked();

        if(checked){
            mPrice.setVisibility(View.INVISIBLE);
            mPrice.setText("0");
            mCurrency.setVisibility(View.INVISIBLE);
        } else{
            mPrice.setVisibility(View.VISIBLE);
            mCurrency.setVisibility(View.VISIBLE);
        }
    }

    //check if all values are entered, if yes save data in database and delete input fields
    public void btn_newoffer(View view){

        String eventname = mEventName.getText().toString();
        String eventlocation = mEventLoation.getText().toString();
        String numberoftickets = mNumberOfTickets.getText().toString();
        String date = mDate.getText().toString();
        String time = mTime.getText().toString();
        String price = mPrice.getText().toString();
        boolean free = mCheckBoxPrice.isChecked();
        String type = mEventType.getSelectedItem().toString();
        String currency = mCurrency.getSelectedItem().toString();

        HashMap<String, String> price_currency = new HashMap<String, String>(0);
        price_currency.put("value", price);
        price_currency.put("currency", currency);

        if(eventname.equals("") | eventlocation.equals("") | numberoftickets.equals("") | date.equals("Date") | (price.equals("") & free | time.equals("Time"))){
            Toast.makeText(getApplicationContext(),"Please fill out the required information!",Toast.LENGTH_SHORT).show();
        }
        else {

            if (isEditTicket()) {
                Ticket newticket = new Ticket(eventname, type, price_currency, numberoftickets, eventlocation, date, FirebaseAuth.getInstance().getCurrentUser().getUid(), time);
                FirebaseDatabase.getInstance().getReference().child("tickets").child(ticketKey).setValue(newticket);

                this.setEditTicket(false);
                this.setTicketKey("");
            } else {

                // Create ticket in database
                Ticket newticket = new Ticket(eventname, type, price_currency, numberoftickets, eventlocation, date, FirebaseAuth.getInstance().getCurrentUser().getUid(), time);
                //((ArrayList<Ticket>) ((MainActivityTabHost) getParent()).baseBundle.getSerializable("tickets_offerdetail")).add(newticket);
                String key = FirebaseDatabase.getInstance().getReference().child("tickets").push().getKey();
                //((ArrayList<String>) ((MainActivityTabHost) getParent()).baseBundle.getSerializable("tickets_offerdetail_keys")).add(key);
                FirebaseDatabase.getInstance().getReference().child("tickets").child(key).setValue(newticket);
            }

                // delete input fields
                setEmtpyTicketTextFields();

                // hide keyboard
                ((TicketMatch) getApplication()).minimizeKeyboard(view);

                Toast.makeText(getApplicationContext(), "Your ticket is registered!", Toast.LENGTH_SHORT).show();
                ((TabHost) getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("tickets");

                //((((RecyclerView)((TabHost)getParent().findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.offeroverview_list)).getAdapter())).notifyDataSetChanged();
        }


    }

    /**
     * Sets all TextFields of the screen to the approprieate values of a ticket
     * @param ticket Ticket Entity
     */
    public void setTicketTextFields(Ticket ticket){
        mEventName.setText(ticket.getName());
        mEventLoation.setText(ticket.getLocation());
        mNumberOfTickets.setText(ticket.getQuantity());
        mDate.setText(ticket.getDate());
        mPrice.setText(ticket.getPrice().get("value"));
        mCheckBoxPrice.setChecked(false);
        cb_clicked(mCheckBoxPrice);
        mEventType.setSelection(adapterEventType.getPosition(ticket.getType()));
        mCurrency.setSelection(adapterCurrency.getPosition(ticket.getPrice().get("currency")));
        mTime.setText(ticket.getTime());
    }

    public void setEmtpyTicketTextFields(){
        HashMap price = new HashMap();
        price.put("value", "");
        price.put("curreny", adapterCurrency.getItem(0));
        Ticket emptyTicket = new Ticket("","",price,"","","Date","","Time");
        setTicketTextFields(emptyTicket);
    }

}
