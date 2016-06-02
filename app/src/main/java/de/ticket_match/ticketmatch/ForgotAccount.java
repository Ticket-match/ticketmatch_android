package de.ticket_match.ticketmatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
            Toast.makeText(getApplicationContext(),"Please fill out the requiered information!",Toast.LENGTH_SHORT).show();
        } else if(!email.contains("@")) {
            Toast.makeText(getApplicationContext(), "Please provide an email adress!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),email,Toast.LENGTH_SHORT).show();
            Intent loginscreen =  new Intent(this, MainActivity.class);
            startActivity(loginscreen);
        }

    }
}
