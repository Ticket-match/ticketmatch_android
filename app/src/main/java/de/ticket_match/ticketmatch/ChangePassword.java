package de.ticket_match.ticketmatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    private final String TAG = "ChangePassword";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

    /*@Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }*/

    public void btn_changepassword (View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        String currentPassword = ((EditText)findViewById(R.id.changePassword_currentPassword)).getText().toString();
        final String newPassword = ((EditText)findViewById(R.id.changePassword_newPassword)).getText().toString();
        String newPasswordReenter = ((EditText)findViewById(R.id.changePassword_newPassword_reenter)).getText().toString();


        if(email.equals("") | currentPassword.equals("") | newPassword.equals("") | newPasswordReenter.equals("")){
            Toast.makeText(getApplicationContext(),"Please fill out the required information!",Toast.LENGTH_SHORT).show();
        }
        else if(!newPassword.equals(newPasswordReenter)){
            Toast.makeText(getApplicationContext(),"New Passwords do not match. Please retype it.",Toast.LENGTH_SHORT).show();
        }
        else {
            final ProgressDialog progressDialog;

            progressDialog = ProgressDialog.show(ChangePassword.this, "Please wait ...",  "Password is changing ...", true);
            progressDialog.setCancelable(true);
            //Re-authenticate User (Requirement from Firebase) For more information see: https://firebase.google.com/docs/auth/android/manage-users#re-authenticate_a_user

            AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //Set new user password after re-authentication
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "Password change was successful!", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                            else{
                                //Show the user the error message, if re-authentication failed
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }

    public void btn_website(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TicketMatch.TM_WEBSITE));
        startActivity(browserIntent);
    }
}
