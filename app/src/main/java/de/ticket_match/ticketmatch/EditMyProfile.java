package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.ConnectivityManager;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.ticket_match.ticketmatch.entities.User;

public class EditMyProfile extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        // Dropdown Menu
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.register_gender, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.edit_myprofile_gender)).setAdapter(adapter);

        //Datepicker
        ((TextView)findViewById(R.id.edit_myprofile_birthdate)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TicketMatch)getApplication()).minimizeKeyboard(v);
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(getParent(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = dayOfMonth + "." + (monthOfYear+1) + "." + year;
                        ((TextView)((TabHost)((MainActivityTabHost)getParent()).findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.edit_myprofile_birthdate)).setText(date);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                return false;
            }
        });

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
        }
        else {
            //Create a User class with attributes
            user = new User(firstname,lastname,gender,birthdate,location, new ArrayList<String>(0), new ArrayList<HashMap<String, String>>(0));
            // Edit Account information
            mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
            ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("myprofile");

        }

        // delete input fields
        ((EditText)findViewById(R.id.edit_myprofile_firstname)).setText("");
        ((EditText)findViewById(R.id.edit_myprofile_lastname)).setText("");
        ((EditText)findViewById(R.id.edit_myprofile_location)).setText("");
        ((TextView)findViewById(R.id.edit_myprofile_birthdate)).setText("Birthday");
        ((Spinner)findViewById(R.id.edit_myprofile_gender)).setSelection(0);

        // hide keyboard
        View aview = this.getCurrentFocus();
        if (aview != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(aview.getWindowToken(), 0);
        }

    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    // Check Internet Status
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
