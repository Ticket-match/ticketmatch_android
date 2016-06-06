package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

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

    public void cb_clicked(View view){

        boolean checked = ((CheckBox)view).isChecked();

        if(checked){
            ((EditText)findViewById(R.id.price)).setVisibility(View.INVISIBLE);
        } else{
            ((EditText)findViewById(R.id.price)).setVisibility(View.VISIBLE);
        }

    }

    public void btn_newoffer(View view){

        String price_clicked = "";

        String eventname = ((EditText)findViewById(R.id.eventname)).getText().toString();
        String eventlocation = ((EditText)findViewById(R.id.eventlocation)).getText().toString();
        String numberoftickets = ((EditText)findViewById(R.id.numberoftickets)).getText().toString();
        String date = ((TextView)findViewById(R.id.date)).getText().toString();
        String price = ((EditText)findViewById(R.id.price)).getText().toString();
        boolean free = ((CheckBox)findViewById(R.id.checkbox_price)).isChecked();

        if(price.equals("") & free==false){
            price_clicked="false";
        }

        if(eventname.equals("") | eventlocation.equals("") | numberoftickets.equals("") | date.equals("Date") | price_clicked.equals("false")){
            Toast.makeText(getApplicationContext(),"Please fill out the requiered information!",Toast.LENGTH_SHORT).show();
        } else {
            // Backend
            Toast.makeText(getApplicationContext(),"Your ticket is registered!",Toast.LENGTH_SHORT).show();
            //Intent offeroverview = new Intent(getApplicationContext(), Offer_Overview.class);
            //startActivity(offeroverview);
            ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("tickets");
        }


    }

    public void newoffer_date(View view) {
        NewOffer.NewOfferDate rbd = new NewOffer.NewOfferDate();
        rbd.show(getFragmentManager(), "rbd");
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

    /*
    public void btn_tm_logo(View view) {

        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.change_password:
                        //ChangePasswordDialog cdp = new ChangePasswordDialog();
                        //cdp.show(getFragmentManager(), "cdp");
                        Intent changepassword =  new Intent(getApplicationContext(), ChangePassword.class);
                        startActivity(changepassword);
                        return true;
                    case R.id.logout:
                        Toast.makeText(getApplicationContext(),"logout",Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();

    }

    public void btn_profile(View view) {
        Intent myprofile = new Intent(this, MyProfile.class);
        startActivity(myprofile);
    }

    public void btn_message(View view) {
        Intent message = new Intent(this, Message_Overview.class);
        startActivity(message);
    }

    public void btn_ticketoffer(View view) {
        Intent offeroverview = new Intent(this, Offer_Overview.class);
        startActivity(offeroverview);
    }

    public void btn_search(View view) {
        Intent find = new Intent(this, Find.class);
        startActivity(find);
    }

    public void btn_makematch(View view) {
        Intent makeadate = new Intent(this, MakeADate.class);
        startActivity(makeadate);
    }
    */

}
