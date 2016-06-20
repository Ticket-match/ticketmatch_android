package de.ticket_match.ticketmatch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.FacebookSdk;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.ticket_match.ticketmatch.entities.User;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private CallbackManager mCallbackManager;
    LoginButton loginButton;
    public User user;
    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions("email","public_profile","user_birthday","user_location");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                Log.d(TAG, "after requestFacebookData");
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    // User is signed in
                    Intent maintab = new Intent(getApplicationContext(), MainActivityTabHost.class);
                    startActivity(maintab);
                }
            }

        };
        mAuth.addAuthStateListener(mAuthListener);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    @Override
    public void onBackPressed() {
        mAuth.removeAuthStateListener(mAuthListener);
        finish();
        System.exit(0);
    }

    //delete email and password input fields
    @Override
    protected void onPause() {
        ((EditText) findViewById(R.id.login_mail)).setText("");
        ((EditText) findViewById(R.id.login_password)).setText("");
        super.onPause();
    }

    //method for login button
    public void btn_login(View view) {
        String email = ((EditText) findViewById(R.id.login_mail)).getText().toString();
        String password = ((EditText) findViewById(R.id.login_password)).getText().toString();
        final ProgressDialog progressDialog;

        if (email.equals("") | password.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill out the required information!", Toast.LENGTH_SHORT).show();
        } else if (!email.contains("@")) {
            Toast.makeText(getApplicationContext(), "Please provide an email address!", Toast.LENGTH_SHORT).show();
        } else if (!isNetworkConnected()) {
            Toast.makeText(getApplicationContext(), "No internet connection. Login failed.", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Logging in ...", true);
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

    //Facebook Login

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //Authentication Facebook User with Firebase
    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        requestFacebookData(token);
                        Log.d(TAG, "signInWithCredential:onComplete: SUCCESS!!!");
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    //Get the Userdata from Facebook and store it in Firebase Database
    private void requestFacebookData(final AccessToken accessToken){

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {

                    String firstname = "Firstname";
                    String lastname = "Lastname";
                    String birthday = "1.1.1900";
                    String gender = "Male";
                    String userID = "";
                    String location = "Default";


                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        Log.d(TAG, "in onCompletedGraphREquest");
                        try{
                            firstname = object.getString("first_name");
                            lastname = object.getString("last_name");
                            birthday = object.getString("birthday");
                            gender = object.getString("gender");
                            userID = object.getString("id");
                            Log.d(TAG, "requestFacebookData Worked" + userID);
                            location = object.getString("location");
                            Log.d(TAG, "requestFacebookData Worked" + object.getString("first_name"));

                        }catch (JSONException e){
                            Log.d(TAG, "You came in the Catch Block");
                            e.printStackTrace();
                        }

                        user = new User(firstname, lastname, gender, birthday, location, new ArrayList<String>(0), new ArrayList<HashMap<String, String>>(0));
                        mDatabase.child("users").child(uid).setValue(user);

                        Log.d(TAG, "requestFacebookData Worked");
                        //Log.d(TAG, "UserID is from Firebase" + FirebaseAuth.getInstance().getCurrentUser().getUid());

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,gender,birthday,location,email");
        request.setParameters(parameters);
        request.executeAsync();

        Log.d(TAG, "End requestFacebookData");

    }


    // Check Internet Status
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    //method for forgot account button
    public void tv_forgot_account(View view) {
        Intent forgotAccount = new Intent(this, ForgotAccount.class);
        startActivity(forgotAccount);
    }

    //method for register button
    public void btn_activity_register(View view) {
        Intent register = new Intent(this, Register.class);
        startActivity(register);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://de.ticket_match.ticketmatch/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://de.ticket_match.ticketmatch/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

