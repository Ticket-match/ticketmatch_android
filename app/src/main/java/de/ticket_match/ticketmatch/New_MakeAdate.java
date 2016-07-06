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
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.ticket_match.ticketmatch.entities.MakeDate;

public class New_MakeAdate extends AppCompatActivity {


    TextView mDate;
    TextView mTime;
    EditText mName;
    EditText mLocation;
    Spinner mType;
    CheckBox mWithMan;
    CheckBox mwithWoman;

    ArrayAdapter<CharSequence> adapterEventType;

    //Used to edit a Date
    Boolean editMakeDate = false;
    String makeDateKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__make_adate);


        mDate = ((TextView)findViewById(R.id.new_makeadate_date));
        mTime = ((TextView)findViewById(R.id.new_makeadate_time));
        mName = ((EditText)findViewById(R.id.new_makeadate_eventname));
        mLocation = ((EditText)findViewById(R.id.new_makeadate_eventlocation));
        mType = ((Spinner)findViewById(R.id.new_makeadate_eventtype));
        mWithMan = ((CheckBox)findViewById(R.id.new_makeadate_withman));
        mwithWoman = ((CheckBox)findViewById(R.id.new_makeadate_withwoman));


        adapterEventType = ArrayAdapter.createFromResource(this, R.array.event_type, R.layout.support_simple_spinner_dropdown_item);
        adapterEventType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner)findViewById(R.id.new_makeadate_eventtype)).setAdapter(adapterEventType);


        //Datepicker
        ((TextView)findViewById(R.id.new_makeadate_date)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TicketMatch)getApplication()).minimizeKeyboard(v);
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(getParent(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //month+1: array starts at 0
                        monthOfYear = monthOfYear+1;
                        String date = (dayOfMonth<10?"0"+dayOfMonth:dayOfMonth) + "." + (monthOfYear<10?"0"+monthOfYear:monthOfYear) + "." + year;

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        Date today = null;

                        try {
                            today = dateFormat.parse(dateFormat.format(new Date()));
                            if (dateFormat.parse(date).compareTo(today)<0) {
                                Toast.makeText(getApplicationContext(),"Please don't use a past date!", Toast.LENGTH_SHORT).show();

                                Calendar c = Calendar.getInstance();
                                dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                                monthOfYear = c.get(Calendar.MONTH);
                                year = c.get(Calendar.YEAR);

                                date = (dayOfMonth<10?"0"+dayOfMonth:dayOfMonth) + "." + (monthOfYear<10?"0"+monthOfYear:monthOfYear) + "." + year;
                            }
                        } catch (Exception e) {
                        }

                        ((TextView)((TabHost)((MainActivityTabHost)getParent()).findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.new_makeadate_date)).setText(date);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
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
        String date = mDate.getText().toString();
        String time = mTime.getText().toString();
        String name = mName.getText().toString();
        String location = mLocation.getText().toString();
        String type = mType.getSelectedItem().toString();
        boolean withman = mWithMan.isChecked();
        boolean withwoman = mwithWoman.isChecked();

        String swithman = (withman?"true": "false");
        String swithwoman = (withwoman?"true": "false");

        if(date.equals("Date") | time.equals("Time") | name.equals("") | location.equals("")){
            Toast.makeText(getApplicationContext(),"Please fill out the required information!",Toast.LENGTH_SHORT).show();
        }
        else {

            if (isEditMakeDate()) {
                MakeDate new_date = new MakeDate(date, location, name, time, type, FirebaseAuth.getInstance().getCurrentUser().getUid(), swithman, swithwoman);
                FirebaseDatabase.getInstance().getReference().child("makedates").child(makeDateKey).setValue(new_date);

                this.setEditMakeDate(false);
                this.setMakeDateKey("");
            } else {
                // Create ticket in database
                MakeDate new_date = new MakeDate(date, location, name, time, type, FirebaseAuth.getInstance().getCurrentUser().getUid(), swithman, swithwoman);
                String key = FirebaseDatabase.getInstance().getReference().child("makedates").push().getKey();
                FirebaseDatabase.getInstance().getReference().child("makedates").child(key).setValue(new_date);
            }


            //delete input fields
            setEmtpyTicketTextFields();

            // hide keyboard
            ((TicketMatch)getApplication()).minimizeKeyboard(view);

            Toast.makeText(getApplicationContext(), "Your date is registered!", Toast.LENGTH_SHORT).show();
            ((TabHost) getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("makeadate");
        }

    }


    public void setMakeDateTextFields(MakeDate date){
        boolean isWithMan = Boolean.parseBoolean(date.isWithman());
        boolean isWithWoman = Boolean.parseBoolean(date.isWithwoman());

        mDate.setText(date.getDate());
        mTime.setText(date.getTime());
        mName.setText(date.getName());
        mType.setSelection(adapterEventType.getPosition(date.getType()));;
        mLocation.setText(date.getLocation());
        mWithMan.setChecked(isWithMan);
        mwithWoman.setChecked(isWithWoman);
    }

    public void setEmtpyTicketTextFields(){
        MakeDate emptyDate = new MakeDate("Date","","","Time","","","false","false");
        setMakeDateTextFields(emptyDate);
    }


    public Boolean isEditMakeDate() {
        return editMakeDate;
    }

    public void setEditMakeDate(Boolean editMakeDate) {
        this.editMakeDate = editMakeDate;
    }

    public String getMakeDateKey() {
        return makeDateKey;
    }

    public void setMakeDateKey(String makeDateKey) {
        this.makeDateKey = makeDateKey;
    }
}
