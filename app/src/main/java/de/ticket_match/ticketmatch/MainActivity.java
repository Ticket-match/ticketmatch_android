package de.ticket_match.ticketmatch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.utilities.Base64;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.FacebookSdk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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
    public boolean tmpExists;
    private String fbp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        TicketMatch.doSharedPreferences(getSharedPreferences("TicketMatch", 0));

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","user_about_me","user_birthday","user_location"));
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                if (error.getMessage().equals("net::ERR_INTERNET_DISCONNECTED"))
                    Toast.makeText(getApplicationContext(), "No internet connection. Login failed.", Toast.LENGTH_SHORT).show();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    try{
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
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
            fbp = "firebase";
            TicketMatch.editor.putString("PID", fbp);
            TicketMatch.editor.commit();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
        final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Logging in ...", true);
        progressDialog.setCancelable(true);
        fbp = "facebook";
        TicketMatch.editor.putString("PID", fbp);
        TicketMatch.editor.commit();
        mAuth.signInWithCredential(FacebookAuthProvider.getCredential(token.getToken()))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Log.d(TAG, "Daten werden von Facebook geladen....");
                            requestFacebookData(token);
                        } else {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            LoginManager.getInstance().logOut();
                        }
                    }
                });
    }

    //Get the Userdata from Facebook and store it in Firebase Database
    private void requestFacebookData(final AccessToken accessToken){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {

                            String firstname = "";
                            String lastname = "";
                            String birthday = "";
                            String gender = "";
                            String userID = "";
                            String location = "";

                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.d(TAG, "in onCompletedGraphREquest");

                                try{
                                    userID = object.getString("id");
                                    if(object.has("first_name")) firstname = object.getString("first_name");
                                    if(object.has("last_name")) lastname = object.getString("last_name");
                                    if(object.has("birthday")) {
                                        birthday = object.getString("birthday");
                                        birthday = birthday.substring(3,5) + "." + birthday.substring(0,2) + "." + birthday.substring(6);
                                    }
                                    if(object.has("gender")) {
                                        gender = object.getString("gender");
                                        gender = gender.substring(0,1).toUpperCase() + gender.substring(1).toLowerCase();
                                    }
                                    if(object.has("location")) location = object.getJSONObject("location").getString("name");
                                } catch (JSONException e){
                                    e.printStackTrace();
                                }

                                if (!userID.equals("")) {
                                    final String imageUrl = "https://graph.facebook.com/" + userID + "/picture?type=large";

                                    try
                                    {
                                        URL url = new URL(imageUrl);//(String)params[0]);
                                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                        connection.setDoInput(true);
                                        connection.setInstanceFollowRedirects(true);
                                        connection.connect();
                                        InputStream input = connection.getInputStream();
                                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                                        if (myBitmap == null) myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profilbild);
                                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                        byte[] ba = bytes.toByteArray();
                                        mStorage.child("images/" + uid + ".jpg").putBytes(ba);
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }

                                    if (!firstname.equals("")) mDatabase.child("users").child(uid).child("firstName").setValue(firstname);
                                    if (!lastname.equals("")) mDatabase.child("users").child(uid).child("lastName").setValue(lastname);
                                    if (!birthday.equals("")) mDatabase.child("users").child(uid).child("birthday").setValue(birthday);
                                    if (!gender.equals("")) mDatabase.child("users").child(uid).child("gender").setValue(gender);
                                    if (!location.equals("")) mDatabase.child("users").child(uid).child("location").setValue(location);
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,gender,birthday,location,email");
                request.setParameters(parameters);
                request.executeAndWait();
            }
        });

        t.start();

        try{
            t.join();
        } catch (Exception e) {

        }
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

    public void btn_website(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TicketMatch.TM_WEBSITE));
        startActivity(browserIntent);
    }
}
