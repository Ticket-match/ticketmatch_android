package de.ticket_match.ticketmatch;

import android.app.Application;
import com.firebase.client.Firebase;

public class AwesomeFireBase extends Application {

    private static final String TAG = "AwesomeFireBase";


    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
