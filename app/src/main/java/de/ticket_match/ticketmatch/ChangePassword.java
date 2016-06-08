package de.ticket_match.ticketmatch;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

    public void btn_changepassword (View view){

        String password = ((EditText)findViewById(R.id.newpassword)).getText().toString();
        String passwordreenter = ((EditText)findViewById(R.id.newpassword_reenter)).getText().toString();

        if(password.equals("") | passwordreenter.equals("")){
            Toast.makeText(getApplicationContext(),"Please fill out the required information!",Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(passwordreenter)){
            Toast.makeText(getApplicationContext(),"Passwords do not match. Please retype it.",Toast.LENGTH_SHORT).show();
        }
        else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(ChangePassword.this, "Password change failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            onBackPressed();
        }
    }
}
