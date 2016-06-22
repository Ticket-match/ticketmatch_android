package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

import de.ticket_match.ticketmatch.entities.MakeDate;

public class New_MakeAdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__make_adate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.event_type, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner)findViewById(R.id.new_makeadate_eventtype)).setAdapter(adapter);


        //Datepicker
        ((TextView)findViewById(R.id.new_makeadate_date)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TicketMatch)getApplication()).minimizeKeyboard(v);
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(getParent(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = dayOfMonth + "." + (monthOfYear+1) + "." + year;
                        ((TextView)((TabHost)((MainActivityTabHost)getParent()).findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.new_makeadate_date)).setText(date);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                return false;
            }
        });

        //Timepicker
        ((TextView)findViewById(R.id.new_makeadate_time)).setOnTouchListener(new View.OnTouchListener(){
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

                        ((TextView)((TabHost)((MainActivityTabHost)getParent()).findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.new_makeadate_time)).setText(time);
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

    public void btn_new_makeadate(View view){

        //check if all values are entered, if yes save data in database and delete input fields
        String date = ((TextView)findViewById(R.id.new_makeadate_date)).getText().toString();
        String time = ((TextView)findViewById(R.id.new_makeadate_time)).getText().toString();
        String name = ((EditText)findViewById(R.id.new_makeadate_eventname)).getText().toString();
        String location = ((EditText)findViewById(R.id.new_makeadate_eventlocation)).getText().toString();
        String type = ((Spinner)findViewById(R.id.new_makeadate_eventtype)).getSelectedItem().toString();
        boolean withman = ((CheckBox)findViewById(R.id.new_makeadate_withman)).isChecked();
        boolean withwoman = ((CheckBox)findViewById(R.id.new_makeadate_withwoman)).isChecked();

        String swithman = (withman?"true": "false");
        String swithwoman = (withwoman?"true": "false");

        if(date.equals("Date") | time.equals("Time") | name.equals("") | location.equals("")){
            Toast.makeText(getApplicationContext(),"Please fill out the required information!",Toast.LENGTH_SHORT).show();
        }
        else {
            // Create ticket in database
            MakeDate new_date = new MakeDate(date, location, name, time, type, FirebaseAuth.getInstance().getCurrentUser().getUid(), swithman, swithwoman);
            ((ArrayList<MakeDate>) ((MainActivityTabHost) getParent()).baseBundle.getSerializable("makeadate_list")).add(new_date);
            String key = FirebaseDatabase.getInstance().getReference().child("makedates").push().getKey();
            ((ArrayList<String>) ((MainActivityTabHost) getParent()).baseBundle.getSerializable("makeadate_list_keys")).add(key);
            FirebaseDatabase.getInstance().getReference().child("makedates").child(key).setValue(new_date);

            //delete input fields
            ((TextView) findViewById(R.id.new_makeadate_date)).setText("Date");
            ((TextView) findViewById(R.id.new_makeadate_time)).setText("Time");
            ((EditText) findViewById(R.id.new_makeadate_eventname)).setText("");
            ((EditText) findViewById(R.id.new_makeadate_eventlocation)).setText("");
            ((Spinner) findViewById(R.id.new_makeadate_eventtype)).setSelection(0);
            ((CheckBox) findViewById(R.id.new_makeadate_withman)).setChecked(false);
            ((CheckBox) findViewById(R.id.new_makeadate_withwoman)).setChecked(false);

            // hide keyboard
            View aview = this.getCurrentFocus();
            if (aview != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(aview.getWindowToken(), 0);
            }

            Toast.makeText(getApplicationContext(), "Your date is registered!", Toast.LENGTH_SHORT).show();
            ((TabHost) getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("makeadate");
            ((((RecyclerView)((TabHost)getParent().findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.makedate_overview)).getAdapter())).notifyDataSetChanged();
        }

    }

}
