package de.ticket_match.ticketmatch;

import android.*;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.firebase.client.Firebase;

/**
 * Created by alexa on 11.06.2016.
 */
public class TicketMatch extends Application{

    public final static int  MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public final static int  MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

    public void minimizeKeyboard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
