package de.ticket_match.ticketmatch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_account);
    }

    @Override
    protected void onPause() {
        ((EditText)findViewById(R.id.forgotAccount_mail)).setText("");
        super.onPause();
    }

    public void btn_forgotAccount(View view) {
        String email = ((EditText)findViewById(R.id.forgotAccount_mail)).getText().toString();

        if(email.equals("")){
            Toast.makeText(getApplicationContext(),"Please fill out the required information!",Toast.LENGTH_SHORT).show();
        } else if(!email.contains("@")) {
            Toast.makeText(getApplicationContext(), "Please provide an email address!", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"Password reset email is being sent",Toast.LENGTH_SHORT).show();
                                Log.d("Firebase", "Email sent.");
                            }
                        }
                    });
            Intent loginscreen =  new Intent(this, MainActivity.class);
            startActivity(loginscreen);
        }

    }
}
