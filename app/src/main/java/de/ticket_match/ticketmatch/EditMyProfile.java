package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import de.ticket_match.ticketmatch.entities.User;

public class EditMyProfile extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private User user;
    public ArrayList<String> interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        // Dropdown Menu
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.register_gender, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.edit_myprofile_gender)).setAdapter(adapter);


        user = TicketMatch.getCurrentUser();
        if(!user.getFirstName().equals("")) {
            ((EditText) findViewById(R.id.edit_myprofile_firstname)).setText(user.getFirstName());
        }
        if(!user.getLastName().equals("")){
            ((EditText) findViewById(R.id.edit_myprofile_lastname)).setText(user.getLastName());
        }
        if(!user.getLocation().equals("")){
            ((EditText) findViewById(R.id.edit_myprofile_location)).setText(user.getLocation());
        }
        if(!user.getBirthday().equals("")){
            ((TextView) findViewById(R.id.edit_myprofile_birthdate)).setText(user.getBirthday());
        }
        if(user.getGender().equals("Neutral")){
            ((Spinner) findViewById(R.id.edit_myprofile_gender)).setSelection(0);
        } else if(user.getGender().equals("Male")){
            ((Spinner) findViewById(R.id.edit_myprofile_gender)).setSelection(1);
        } else{
            ((Spinner) findViewById(R.id.edit_myprofile_gender)).setSelection(2);
        }
    }

    public void btn_edit_myprofile(View view){

        String firstname = ((EditText)findViewById(R.id.edit_myprofile_firstname)).getText().toString();
        String lastname = ((EditText)findViewById(R.id.edit_myprofile_lastname)).getText().toString();
        String location = ((EditText)findViewById(R.id.edit_myprofile_location)).getText().toString();
        String birthdate = ((TextView)findViewById(R.id.edit_myprofile_birthdate)).getText().toString();
        String gender = ((Spinner)findViewById(R.id.edit_myprofile_gender)).getSelectedItem().toString();

        if(firstname.equals("") | lastname.equals("") | location.equals("") | birthdate.equals("Birthday")) {
            Toast.makeText(getApplicationContext(), "Please fill out the required information!", Toast.LENGTH_SHORT).show();
        } else if (!isNetworkConnected()){
            Toast.makeText(getApplicationContext(), "No internet connection. Editing profile information failed.", Toast.LENGTH_SHORT).show();
        } else {
            // Create a User with attributes and the interests and ratings from screen MyProfile
            user = new User(firstname, lastname,gender, birthdate, location, user.getInterests(), user.getRatings());
            // Edit attributes by setting new values
            mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

            // hide keyboard
            View aview = this.getCurrentFocus();
            if (aview != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(aview.getWindowToken(), 0);
            }

            Toast.makeText(getApplicationContext(), "Profile edited successfully!", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    // Birthday Date Picker
    public void editmyprofile_birthdate(View view) {
        ((TicketMatch)getApplication()).minimizeKeyboard(view);
        EditmyProfileBirthdateDialog rbd = new EditmyProfileBirthdateDialog();

        rbd.show(getFragmentManager(), "rbd");
    }

    public static class EditmyProfileBirthdateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //Use the current date as the default date in the date picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dpd;

        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            //month+1: array starts at 0
            month = month+1;
            String date = (day<10?"0"+day:day) + "." + (month<10?"0"+month:month) + "." + year;
            ((TextView)getActivity().findViewById(R.id.edit_myprofile_birthdate)).setText(date);
        }
    }


    // Check Internet Status
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void btn_website(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TicketMatch.TM_WEBSITE));
        startActivity(browserIntent);
    }
}
