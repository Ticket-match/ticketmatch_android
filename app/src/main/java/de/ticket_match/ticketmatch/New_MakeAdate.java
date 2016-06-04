package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
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

    }

    public void new_makeadate_withman(View view){

        boolean checked = ((CheckBox)view).isChecked();

    }

    public void new_makeadate_withwoman(View view){

        boolean checked = ((CheckBox)view).isChecked();

    }

    public void new_makeadate_date(View view){

        New_MakeAdate.NewMakeADate rbd = new New_MakeAdate.NewMakeADate();
        rbd.show(getFragmentManager(), "rbd");
    }

    public void btn_new_makeadate(View view){

        // Get data out of input screen and save in Backend. With Strings or Array?

        Toast.makeText(getApplicationContext(),"Your date is registered!",Toast.LENGTH_SHORT).show();

        Intent makeadate = new Intent(getApplicationContext(), MakeADate.class);
        startActivity(makeadate);

    }

    public static class NewMakeADate extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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
            String date = day + "." + month + "." + year;
            ((TextView)getActivity().findViewById(R.id.new_makeadate_date)).setText(date);
        }
    }
}
