package de.ticket_match.ticketmatch;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Firebase", "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent myprofile =  new Intent(getApplicationContext(), MyProfile.class);
                    startActivity(myprofile);
                } else {
                    // User is signed out
                    Log.d("Firebase", "onAuthStateChanged:signed_out");
                }

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
        ((EditText)findViewById(R.id.login_mail)).setText("");
        ((EditText)findViewById(R.id.login_password)).setText("");
        super.onPause();
    }

    public void btn_login (View view){

        String email = ((EditText)findViewById(R.id.login_mail)).getText().toString();
        String password = ((EditText)findViewById(R.id.login_password)).getText().toString();

        if(email.equals("") | password.equals("")){
            Toast.makeText(getApplicationContext(),"Please fill out the required information!",Toast.LENGTH_SHORT).show();
        } else if(!email.contains("@")) {
            Toast.makeText(getApplicationContext(), "Please provide an email address!", Toast.LENGTH_SHORT).show();
        } else {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("Firebase", "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w("Firebase", "signInWithEmail", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            // ...
                        }
                    });

            Toast.makeText(getApplicationContext(),email+password, Toast.LENGTH_SHORT).show();
        }



    }

    public void tv_forgot_account(View view){

        Intent forgotAccount =  new Intent(this, ForgotAccount.class);
        startActivity(forgotAccount);


    }

    public void btn_activity_register(View view){

        Intent register =  new Intent(this, Register.class);
        startActivity(register);

    }
    /*public static class ChangePasswordDialog extends DialogFragment{
        public Dialog onCreateDialog (Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_change_password, null));

            builder.setPositiveButton("Change", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id ){
                    Toast.makeText(getActivity(),"Change Password",Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id ){
                    dialog.cancel();

                }
            });
            return builder.create();
        }
    }*/

}
