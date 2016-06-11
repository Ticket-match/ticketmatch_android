package de.ticket_match.ticketmatch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent maintab = new Intent(getApplicationContext(), MainActivityTabHost.class);
                    startActivity(maintab);
                }
            }
        });
    }

    //delete email and password input fields
    @Override
    protected void onPause() {
        ((EditText)findViewById(R.id.login_mail)).setText("");
        ((EditText)findViewById(R.id.login_password)).setText("");
        super.onPause();
    }

    //method for login button
    public void btn_login (View view){
        String email = ((EditText)findViewById(R.id.login_mail)).getText().toString();
        String password = ((EditText)findViewById(R.id.login_password)).getText().toString();
        final ProgressDialog progressDialog;

        if(email.equals("") | password.equals("")){
            Toast.makeText(getApplicationContext(),"Please fill out the required information!",Toast.LENGTH_SHORT).show();
        } else if(!email.contains("@")) {
            Toast.makeText(getApplicationContext(), "Please provide an email address!", Toast.LENGTH_SHORT).show();
        } else if (!isNetworkConnected()){
            Toast.makeText(getApplicationContext(), "No internet connection. Login failed.", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...",  "Logging in ...", true);
            progressDialog.setCancelable(true);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });

        }
    }

    // Check Internet Status
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    //method for forgot account button
    public void tv_forgot_account(View view){
        Intent forgotAccount =  new Intent(this, ForgotAccount.class);
        startActivity(forgotAccount);
    }

    //method for register button
    public void btn_activity_register(View view){
        Intent register =  new Intent(this, Register.class);
        startActivity(register);
    }
}
