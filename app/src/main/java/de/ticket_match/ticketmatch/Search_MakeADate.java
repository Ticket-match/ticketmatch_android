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

public class Search_MakeADate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__make_adate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.event_type, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner)findViewById(R.id.search_makeadate_eventtype)).setAdapter(adapter);

    }

    public void search_makeadate_withman(View view){

        boolean checked = ((CheckBox)view).isChecked();

    }

    public void search_makeadate_withwoman(View view){

        boolean checked = ((CheckBox)view).isChecked();

    }

    public void search_makeadate_date(View view){

        Search_MakeADate.SearchMakeADate rbd = new Search_MakeADate.SearchMakeADate();
        rbd.show(getFragmentManager(), "rbd");
    }

    public void btn_search_makeadate(View view){

        // Get data out of input screen and save in Backend. With Strings or Array?

        Intent makeadateresults = new Intent(getApplicationContext(), MakeADate_SearchResults.class);
        startActivity(makeadateresults);

    }

    public static class SearchMakeADate extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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
            ((TextView)getActivity().findViewById(R.id.search_makeadate_date)).setText(date);
        }
    }
}
