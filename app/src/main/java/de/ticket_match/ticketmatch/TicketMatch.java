package de.ticket_match.ticketmatch;

import android.*;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.ticket_match.ticketmatch.entities.User;

/**
 * Created by alexa on 11.06.2016.
 */
public class TicketMatch extends Application{

    //Read/Write Permissions
    public final static int  MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public final static int  MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    //Bundle Keys
    public final static String FOREIGN_PROFILE_UID = "foreign_profile_uid";

    //
    private static User currentUser;


    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

    public void minimizeKeyboard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void takePhoto(Activity activity){
        Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent_camera, 1);
    }

    public static void uploadPhoto(Activity activity){
        Intent intent_upload = new Intent();
        intent_upload.setType("image/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent_upload, "Select file to upload"), 2);
    }


    //Getter and Setter Methods
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        TicketMatch.currentUser = currentUser;
    }

}






