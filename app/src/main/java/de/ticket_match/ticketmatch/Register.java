package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class Register extends AppCompatActivity {

    private static final String TAG = "Register";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.register_gender, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner)findViewById(R.id.register_gender)).setAdapter(adapter);

        // Firebase Authentication Listener
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fireUser = firebaseAuth.getCurrentUser();
                if (fireUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + fireUser.getUid());
                    mDatabase.child("users").child(fireUser.getUid()).setValue(user);

                    Intent myProfile = new Intent(getApplicationContext(), MyProfile.class);
                    startActivity(myProfile);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onPause() {
        ((EditText)findViewById(R.id.register_firstname)).setText("");
        ((EditText)findViewById(R.id.register_lastname)).setText("");
        ((EditText)findViewById(R.id.register_email)).setText("");
        ((EditText)findViewById(R.id.register_password)).setText("");
        ((EditText)findViewById(R.id.register_location)).setText("");
        ((TextView)findViewById(R.id.register_birthdate)).setText("Birthday");
        ((Spinner)findViewById(R.id.register_gender)).setSelection(0);

        super.onPause();
    }

    public void btn_register (View view){

        String firstname = ((EditText)findViewById(R.id.register_firstname)).getText().toString();
        String lastname = ((EditText)findViewById(R.id.register_lastname)).getText().toString();
        String email = ((EditText)findViewById(R.id.register_email)).getText().toString();
        String password = ((EditText)findViewById(R.id.register_password)).getText().toString();
        String location = ((EditText)findViewById(R.id.register_location)).getText().toString();
        String birthdate = ((TextView)findViewById(R.id.register_birthdate)).getText().toString();
        String gender = ((Spinner)findViewById(R.id.register_gender)).getSelectedItem().toString();

        if(firstname.equals("") | lastname.equals("") | email.equals("") | password.equals("") | location.equals("") | birthdate.equals("Birthday")){
            Toast.makeText(getApplicationContext(),"Please fill out the requiered information!",Toast.LENGTH_SHORT).show();
        } else if(!email.contains("@")) {
            Toast.makeText(getApplicationContext(), "Please provide a valid email address!", Toast.LENGTH_SHORT).show();
        } else if(password.length()<6){
            Toast.makeText(getApplicationContext(), "Your password must contain at least 6 characters!", Toast.LENGTH_SHORT).show();
        } else if (!isNetworkConnected()){
            Toast.makeText(getApplicationContext(), "No internet connection. Registration failed.", Toast.LENGTH_SHORT).show();
        }
        else {
            //Create a User class with attributes
            user = new User(firstname,lastname,gender,birthdate,location);
            // Create an Account via Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });


        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void register_birthdate(View view) {
        RegisterBirthdateDialog rbd = new RegisterBirthdateDialog();
        rbd.show(getFragmentManager(), "rbd");
    }

    public static class RegisterBirthdateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {


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
            ((TextView)getActivity().findViewById(R.id.register_birthdate)).setText(date);
        }
    }

    /*public static class RegisterBirthdateDialog extends DialogFragment {
        public Dialog onCreateDialog (Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_birthdate, null));
            builder.setPositiveButton("Select", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id ){
                    ((DatePicker)getActivity().findViewById(R.id.register_birthdate_datePicker)).getCalendarView().getDate();
                    ((TextView)getActivity().findViewById(R.id.register_birthdate)).setText(date);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id ){
                    //dialog.cancel();
                }
            });
            return builder.create();
        }
    }*/
}
