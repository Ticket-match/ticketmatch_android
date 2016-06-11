package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import de.ticket_match.ticketmatch.entities.Ticket;

public class NewOffer extends AppCompatActivity {

    private Calendar ticketDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_offer);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.event_type, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner)findViewById(R.id.event_type)).setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter_currency = ArrayAdapter.createFromResource(this, R.array.currency, R.layout.support_simple_spinner_dropdown_item);
        adapter_currency.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner)findViewById(R.id.currency)).setAdapter(adapter_currency);

        //Datepicker
        ((TextView)findViewById(R.id.date)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TicketMatch)getApplication()).minimizeKeyboard(v);
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(getParent(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = dayOfMonth + "." + (monthOfYear+1) + "." + year;
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
        ((TextView)findViewById(R.id.time)).setOnTouchListener(new View.OnTouchListener(){
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

    //if ticket is for free price enter field is hided
    public void cb_clicked(View view){

        boolean checked = ((CheckBox)view).isChecked();

        if(checked){
            ((EditText)findViewById(R.id.price)).setVisibility(View.INVISIBLE);
            ((EditText)findViewById(R.id.price)).setText("0");
            findViewById(R.id.currency).setVisibility(View.INVISIBLE);
        } else{
            ((EditText)findViewById(R.id.price)).setVisibility(View.VISIBLE);
            findViewById(R.id.currency).setVisibility(View.VISIBLE);
        }
    }

    //check if all values are entered, if yes save data in database and delete input fields
    public void btn_newoffer(View view){
        String eventname = ((EditText)findViewById(R.id.eventname)).getText().toString();
        String eventlocation = ((EditText)findViewById(R.id.eventlocation)).getText().toString();
        String numberoftickets = ((EditText)findViewById(R.id.numberoftickets)).getText().toString();
        String date = ((TextView)findViewById(R.id.date)).getText().toString();
        String time = ((TextView)findViewById(R.id.time)).getText().toString();
        String price = ((EditText)findViewById(R.id.price)).getText().toString();
        boolean free = ((CheckBox)findViewById(R.id.checkbox_price)).isChecked();
        String type = ((Spinner)findViewById(R.id.event_type)).getSelectedItem().toString();
        String currency = ((Spinner)findViewById(R.id.currency)).getSelectedItem().toString();

        HashMap<String, String> price_currency = new HashMap<String, String>(0);
        price_currency.put("value", price);
        price_currency.put("currency", currency);

        if(eventname.equals("") | eventlocation.equals("") | numberoftickets.equals("") | date.equals("Date") | (price.equals("") & free | time.equals("Time"))){
            Toast.makeText(getApplicationContext(),"Please fill out the requiered information!",Toast.LENGTH_SHORT).show();
        }
        else {
            // Create ticket in database
            Ticket newticket = new Ticket(eventname, type, price_currency, numberoftickets, eventlocation, date, FirebaseAuth.getInstance().getCurrentUser().getUid(), time);
            ((ArrayList<Ticket>)((MainActivityTabHost) getParent()).baseBundle.getSerializable("tickets_offerdetail")).add(newticket);
            String key = FirebaseDatabase.getInstance().getReference().child("tickets").push().getKey();
            ((ArrayList<String>)((MainActivityTabHost) getParent()).baseBundle.getSerializable("tickets_offerdetail_keys")).add(key);
            FirebaseDatabase.getInstance().getReference().child("tickets").child(key).setValue(newticket);

            // delete input fields
            ((EditText)findViewById(R.id.eventname)).setText("");
            ((EditText)findViewById(R.id.eventlocation)).setText("");
            ((EditText)findViewById(R.id.numberoftickets)).setText("");
            ((TextView)findViewById(R.id.date)).setText("Date");
            ((EditText)findViewById(R.id.price)).setText("");
            ((CheckBox)findViewById(R.id.checkbox_price)).setChecked(false);
            cb_clicked(findViewById(R.id.checkbox_price));
            ((Spinner)findViewById(R.id.event_type)).setSelection(0);
            ((Spinner)findViewById(R.id.currency)).setSelection(0);
            ((TextView)findViewById(R.id.time)).setText("Time");

            // hide keyboard
            View aview = this.getCurrentFocus();
            if (aview != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(aview.getWindowToken(), 0);
            }

            Toast.makeText(getApplicationContext(),"Your ticket is registered!",Toast.LENGTH_SHORT).show();
            ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("tickets");
//            ((Offer_Overview.MyTicketAdapter)((HeaderViewListAdapter)((ListView)((TabHost)getParent().findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.offeroverview_list)).getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
            ((Offer_Overview.MyTicketAdapter)(((ListView)((TabHost)getParent().findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.offeroverview_list)).getAdapter())).notifyDataSetChanged();
        }
    }
}
