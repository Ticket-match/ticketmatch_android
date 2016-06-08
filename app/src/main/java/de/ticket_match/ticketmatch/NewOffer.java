package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class NewOffer extends AppCompatActivity {

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
    }

    //if ticket is for free price enter field is hided
    public void cb_clicked(View view){

        boolean checked = ((CheckBox)view).isChecked();

        if(checked){
            ((EditText)findViewById(R.id.price)).setVisibility(View.INVISIBLE);
            ((EditText)findViewById(R.id.price)).setText("0");
        } else{
            ((EditText)findViewById(R.id.price)).setVisibility(View.VISIBLE);
        }
    }

    //check if all values are entered, if yes save data in database and delete input fields
    public void btn_newoffer(View view){
        String eventname = ((EditText)findViewById(R.id.eventname)).getText().toString();
        String eventlocation = ((EditText)findViewById(R.id.eventlocation)).getText().toString();
        String numberoftickets = ((EditText)findViewById(R.id.numberoftickets)).getText().toString();
        String date = ((TextView)findViewById(R.id.date)).getText().toString();
        String price = ((EditText)findViewById(R.id.price)).getText().toString();
        boolean free = ((CheckBox)findViewById(R.id.checkbox_price)).isChecked();
        String type = ((Spinner)findViewById(R.id.event_type)).getSelectedItem().toString();
        String currency = ((Spinner)findViewById(R.id.currency)).getSelectedItem().toString();

        HashMap<String, String> price_currency = new HashMap<String, String>(0);
        price_currency.put("value", price);
        price_currency.put("currency", currency);

        if(eventname.equals("") | eventlocation.equals("") | numberoftickets.equals("") | date.equals("Date") | (price.equals("") & free)){
            Toast.makeText(getApplicationContext(),"Please fill out the requiered information!",Toast.LENGTH_SHORT).show();
        } else {
            // Create ticket in database
            Ticket newticket = new Ticket(eventname, type, price_currency, numberoftickets, eventlocation, date, FirebaseAuth.getInstance().getCurrentUser().getUid());
            ((ArrayList<Ticket>)((MainActivityTabHost) getParent()).baseBundle.getSerializable("tickets_offerdetail")).add(newticket);
            String key = FirebaseDatabase.getInstance().getReference().child("tickets").push().getKey();
            ((ArrayList<String>)((MainActivityTabHost) getParent()).baseBundle.getSerializable("tickets_offerdetail_keys")).add(key);
            FirebaseDatabase.getInstance().getReference().child("tickets").child(key).setValue(newticket);

            // delete input fields
            ((EditText)findViewById(R.id.eventname)).setText("");
            ((EditText)findViewById(R.id.eventlocation)).setText("");
            ((EditText)findViewById(R.id.numberoftickets)).setText("");
            ((TextView)findViewById(R.id.date)).setText("");
            ((EditText)findViewById(R.id.price)).setText("");
            ((CheckBox)findViewById(R.id.checkbox_price)).setChecked(false);
            ((Spinner)findViewById(R.id.event_type)).setSelection(0);
            ((Spinner)findViewById(R.id.currency)).setSelection(0);

            Toast.makeText(getApplicationContext(),"Your ticket is registered!",Toast.LENGTH_SHORT).show();
            ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("tickets");
            ((Offer_Overview.MyTicketAdapter)((HeaderViewListAdapter)((ListView)((TabHost)getParent().findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.offeroverview_list)).getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
        }
    }

    //TODO: After calling the screen a second time the textview date disapears
    //Datepicker for input field date
    public void newoffer_date(View view) {
        NewOfferDate datepicker = new NewOfferDate();
        datepicker.show(getFragmentManager(), "datepicker");
    }

    public static class NewOfferDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //Use the current date as the default date in the date picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            //month+1: array starts at 0
            String date = day + "." + (month+1) + "." + year;
            ((TextView)getActivity().findViewById(R.id.date)).setText(date);
        }
    }
}
