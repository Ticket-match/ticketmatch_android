package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.ticket_match.ticketmatch.entities.User;

public class Register extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Dropdown Menu
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.register_gender, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.register_gender)).setAdapter(adapter);
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
        final ProgressDialog progressDialog;
        String firstname = ((EditText)findViewById(R.id.register_firstname)).getText().toString();
        String lastname = ((EditText)findViewById(R.id.register_lastname)).getText().toString();
        String email = ((EditText)findViewById(R.id.register_email)).getText().toString();
        String password = ((EditText)findViewById(R.id.register_password)).getText().toString();
        String location = ((EditText)findViewById(R.id.register_location)).getText().toString();
        String birthdate = ((TextView)findViewById(R.id.register_birthdate)).getText().toString();
        String gender = ((Spinner)findViewById(R.id.register_gender)).getSelectedItem().toString();

        if(firstname.equals("") | lastname.equals("") | email.equals("") | password.equals("") | location.equals("") | birthdate.equals("Birthday")){
            Toast.makeText(getApplicationContext(),"Please fill out the required information!",Toast.LENGTH_SHORT).show();
        } else if(!email.contains("@")) {
            Toast.makeText(getApplicationContext(), "Please provide a valid email address!", Toast.LENGTH_SHORT).show();
        } else if(password.length()<6){
            Toast.makeText(getApplicationContext(), "Your password must contain at least 6 characters!", Toast.LENGTH_SHORT).show();
        } else if (!isNetworkConnected()){
            Toast.makeText(getApplicationContext(), "No internet connection. Registration failed.", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog = ProgressDialog.show(Register.this, "Please wait ...",  "Registering ...", true);
            progressDialog.setCancelable(true);

            //Create a User class with attributes
            user = new User(firstname,lastname,gender,birthdate,location, new ArrayList<String>(0), new ArrayList<HashMap<String, String>>(0));
            // Create an Account via Firebase Authentication
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.profilbild);
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                byte[] ba = bytes.toByteArray();
                                mStorage.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg").putBytes(ba);
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                                try{
                                    Thread.sleep(1000);
                                } catch (Exception e) {

                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    // Check Internet Status
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    // Birthday Date Picker
    public void register_birthdate(View view) {
        ((TicketMatch)getApplication()).minimizeKeyboard(view);
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
            DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dpd;

        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            //month+1: array starts at 0
            month = month+1;
            String date = (day<10?"0"+day:day) + "." + (month<10?"0"+month:month) + "." + year;
            ((TextView)getActivity().findViewById(R.id.register_birthdate)).setText(date);
        }
    }
}
