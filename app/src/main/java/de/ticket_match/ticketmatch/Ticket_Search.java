package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
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
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Ticket_Search extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Ticket> tickets = new ArrayList<Ticket>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket__search);

        //Dropdown Event Type
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.event_type_search, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner)findViewById(R.id.event_type)).setAdapter(adapter);


        //Datepicker
        ((TextView)findViewById(R.id.date)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(getParent(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = dayOfMonth + "." + (monthOfYear+1) + "." + year;
                        ((TextView)((TabHost)((MainActivityTabHost)getParent()).findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.date)).setText(date);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                return false;
            }
        });

    }

    //check if all values are entered, if yes save data in database and delete input fields
    public void btn_newoffer(View view){
        tickets.clear();
        final String location = ((EditText)findViewById(R.id.eventlocation)).getText().toString();
        final String date = ((TextView)findViewById(R.id.date)).getText().toString();
        final String type = ((Spinner)findViewById(R.id.event_type)).getSelectedItem().toString();

        final ArrayList<String[]> command = new ArrayList<String[]>(0);

        if(!date.equals("Date")){
            command.add(new String []{"date", date});
        }
        if(!location.equals("")){
            command.add(new String []{"location", location});
        }
        if(!type.equals("")){
            command.add(new String []{"type", type});
        }
        if(command.size()==0){
            Toast.makeText(getApplicationContext(),"You have to enter at least one search condition",Toast.LENGTH_SHORT).show();
        } else {
            mDatabase.child("tickets").orderByChild(command.get(0)[0]).equalTo(command.get(0)[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Ticket ticket = d.getValue(Ticket.class);
                        if (!ticket.getUser().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            if (command.size() > 1) {
                                if (command.get(1)[0].equals("location") & ticket.getLocation().equals(location)) {
                                    tickets.add(ticket);
                                } else if (command.get(1)[0].equals("type") & ticket.getType().equals(type)) {
                                    tickets.add(ticket);
                                }
                                if (command.size() > 2) {
                                    if (command.get(2)[0].equals("type") & ticket.getType().equals(type)) {
                                        tickets.add(ticket);
                                    }
                                }
                            } else {
                                tickets.add(ticket);
                            }
                        }
                    }
                    ((Find.CustomAdapter) ((ListView) ((TabHost) getParent().findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.find_results)).getAdapter()).notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //set tickets in a bundle to push it to the next activity (Find)
            ((MainActivityTabHost) getParent()).baseBundle.putSerializable("tickets_search_result", tickets);
            ((TabHost) getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("search");

            // delete input fields
            ((EditText) findViewById(R.id.eventlocation)).setText("");
            ((TextView) findViewById(R.id.date)).setText("Date");
            ((Spinner) findViewById(R.id.event_type)).setSelection(0);
        }

    }
}