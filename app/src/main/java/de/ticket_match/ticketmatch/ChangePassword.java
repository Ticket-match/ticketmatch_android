package de.ticket_match.ticketmatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {
//Test Push for Git
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

    public void btn_changepassword (View view){

        String password = ((EditText)findViewById(R.id.newpassword)).getText().toString();
        String passwordreenter = ((EditText)findViewById(R.id.newpassword_reenter)).getText().toString();

        if(password.equals("") | passwordreenter.equals("")){
            Toast.makeText(getApplicationContext(),"Please fill out the requiered information!",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), password+passwordreenter, Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }

    }

}
