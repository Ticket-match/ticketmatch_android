package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

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

    public void new_makeadate_withman(View view){

        boolean checked = ((CheckBox)view).isChecked();

    }

    public void new_makeadate_withwoman(View view){

        boolean checked = ((CheckBox)view).isChecked();

    }

    public void btn_new_makeadate(View view){

        // Get data out of input screen and save in Backend. With Strings or Array?

        //delete input fields
        ((TextView)findViewById(R.id.new_makeadate_date)).setText("Date");
        ((TextView)findViewById(R.id.new_makeadate_time)).setText("Time");
        ((EditText)findViewById(R.id.new_makeadate_eventname)).setText("");
        ((EditText)findViewById(R.id.new_makeadate_eventlocation)).setText("");
        ((Spinner)findViewById(R.id.new_makeadate_eventtype)).setSelection(0);
        ((CheckBox)findViewById(R.id.new_makeadate_withman)).setChecked(false);
        ((CheckBox)findViewById(R.id.new_makeadate_withwoman)).setChecked(false);

        // hide keyboard
        View aview = this.getCurrentFocus();
        if (aview != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(aview.getWindowToken(), 0);
        }

        Toast.makeText(getApplicationContext(),"Your date is registered!",Toast.LENGTH_SHORT).show();

        ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("makeadate");

    }

}
