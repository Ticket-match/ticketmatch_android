package de.ticket_match.ticketmatch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        final ProgressDialog progressDialog;
        String email = ((EditText)findViewById(R.id.forgotAccount_mail)).getText().toString();

        if(email.equals("")){
            Toast.makeText(getApplicationContext(),"Please fill out the required information!",Toast.LENGTH_SHORT).show();
        } else if(!email.contains("@")) {
            Toast.makeText(getApplicationContext(), "Please provide an email address!", Toast.LENGTH_SHORT).show();
        } else if (!isNetworkConnected()){
            Toast.makeText(getApplicationContext(), "No internet connection. Registration failed.", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = ProgressDialog.show(ForgotAccount.this, "Please wait ...",  "Sending password email ...", true);
            progressDialog.setCancelable(true);
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Password reset email is being sent",Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
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

    public void btn_website(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TicketMatch.TM_WEBSITE));
        startActivity(browserIntent);
    }
}
