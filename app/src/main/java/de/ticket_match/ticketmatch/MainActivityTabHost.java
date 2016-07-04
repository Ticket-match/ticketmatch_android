package de.ticket_match.ticketmatch;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivityTabHost extends AppCompatActivity {
    Bundle baseBundle = new Bundle();
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    TabHost th;
    Intent mServiceIntent;
    MessageNotifications mn;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    Bundle b;
    String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_tab_host);

        settings = getSharedPreferences("TicketMatch", 0);
        editor = settings.edit();
        editor.putString("UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        editor.commit();

        pid = settings.getString("PID","");

        if (MessageNotifications.getInstance() == null) {
            //Send work request to the MessageNotification class
            mServiceIntent = new Intent(this, MessageNotifications.class);
            // Starts the IntentService: MessageNotification
            startService(mServiceIntent);
        } else {
            mn = MessageNotifications.getInstance();
        }

        //set header text of my profile the first time
        ((TextView)findViewById(R.id.headerTitle)).setText("My Profile");

        // TabHost Setup
        th = (TabHost)findViewById(R.id.tabHost);
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        th.setup(mLocalActivityManager);

        // Tab "My Profile"
        TabHost.TabSpec ts1 = th.newTabSpec("myprofile");
        View ts1Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator, th.getTabWidget(), false);
        ((TextView) ts1Ind.findViewById(android.R.id.title)).setText("My Profile");
        ((ImageView) ts1Ind.findViewById(android.R.id.icon)).setImageResource(R.drawable.contacts);
        ((ImageView) ts1Ind.findViewById(android.R.id.icon)).setContentDescription("myprofile");
        ts1.setIndicator(ts1Ind);
        ts1.setContent(new Intent(this, MyProfile.class));

        // Tab "My Profile Ratings"
        TabHost.TabSpec ts6 = th.newTabSpec("myprofile_ratings");
        View ts6Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts6.setIndicator(ts6Ind);
        ts6.setContent(new Intent(this, MyProfileRatings.class));

        // Tab "Messages"
        TabHost.TabSpec ts2 = th.newTabSpec("messages");
        View ts2Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator, th.getTabWidget(), false);
        ((TextView) ts2Ind.findViewById(android.R.id.title)).setText("My Messages");
        ((ImageView) ts2Ind.findViewById(android.R.id.icon)).setImageResource(R.drawable.message);
        ((ImageView) ts2Ind.findViewById(android.R.id.icon)).setContentDescription("messages");
        ts2.setIndicator(ts2Ind);
        ts2.setContent(new Intent(this, Message_Overview.class));

        // Tab "Messages Chat"
        TabHost.TabSpec ts7 = th.newTabSpec("messages_chat");
        View ts7Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts7.setIndicator(ts7Ind);
        ts7.setContent(new Intent(this,Message_Chat.class));

        // Tab "Tickets"
        TabHost.TabSpec ts3 = th.newTabSpec("tickets");
        View ts3Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator, th.getTabWidget(), false);
        ((TextView) ts3Ind.findViewById(android.R.id.title)).setText("My Tickets");
        ((ImageView) ts3Ind.findViewById(android.R.id.icon)).setImageResource(R.drawable.ticket);
        ((ImageView) ts3Ind.findViewById(android.R.id.icon)).setContentDescription("tickets");
        ts3.setIndicator(ts3Ind);
        ts3.setContent(new Intent(this, Offer_Overview.class));

        // Tab "Tickets New Offer"
        TabHost.TabSpec ts8 = th.newTabSpec("tickets_newoffer");
        View ts8Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts8.setIndicator(ts8Ind);
        ts8.setContent(new Intent(this, NewOffer.class));

        // Tab "Tickets Search"
        TabHost.TabSpec ts9 = th.newTabSpec("tickets_search");
        View ts9Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts9.setIndicator(ts9Ind);
        ts9.setContent(new Intent(this, Ticket_Search.class));

        // Tab "Search"
        TabHost.TabSpec ts4 = th.newTabSpec("search");
        View ts4Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts4.setIndicator(ts4Ind);
        ts4.setContent(new Intent(this, Find.class));

        // Tab "Make A Date"
        TabHost.TabSpec ts5 = th.newTabSpec("makeadate");
        View ts5Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator, th.getTabWidget(), false);
        ((TextView) ts5Ind.findViewById(android.R.id.title)).setText("My Dates");
        ((ImageView) ts5Ind.findViewById(android.R.id.icon)).setImageResource(R.drawable.group);
        ((ImageView) ts5Ind.findViewById(android.R.id.icon)).setContentDescription("makeadate");
        ts5.setIndicator(ts5Ind);
        ts5.setContent(new Intent(this, MakeADate.class));

        // Tab "Make A Date New"
        TabHost.TabSpec ts13 = th.newTabSpec("makeadate_new");
        View ts13Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts13.setIndicator(ts13Ind);
        ts13.setContent(new Intent(this, New_MakeAdate.class));

        // Tab "Make A Date Search"
        TabHost.TabSpec ts14 = th.newTabSpec("makeadate_search");
        View ts14Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts14.setIndicator(ts14Ind);
        ts14.setContent(new Intent(this, Search_MakeADate.class));

        // Tab "Make A Date Search Result"
        TabHost.TabSpec ts15 = th.newTabSpec("makeadate_search_result");
        View ts15Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts15.setIndicator(ts15Ind);
        ts15.setContent(new Intent(this, MakeADate_SearchResults.class));

        // Tab "Foreign Profile"
        TabHost.TabSpec ts17 = th.newTabSpec("foreign_profile");
        View ts17Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts17.setIndicator(ts17Ind);
        ts17.setContent(new Intent(this, ForeignProfile.class));

        // Tab "Foreign Profile Ratings"
        TabHost.TabSpec ts18 = th.newTabSpec("foreign_profile_ratings");
        View ts18Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts18.setIndicator(ts18Ind);
        ts18.setContent(new Intent(this, ForeignProfileRating.class));

        //Tab "Edit My Profile"
        TabHost.TabSpec ts19 = th.newTabSpec("edit_myprofile");
        View ts19Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts19.setIndicator(ts19Ind);
        ts19.setContent(new Intent(this, EditMyProfile.class));

        /*TabHost.TabSpec ts20 = th.newTabSpec("change_password");
        View ts20Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts20.setIndicator(ts20Ind);
        ts20.setContent(new Intent(this, ChangePassword.class));*/

        // Add Tabs to TabHost
        th.addTab(ts1);
        th.addTab(ts2);
        th.addTab(ts3);
        th.addTab(ts4);
        th.addTab(ts5);
        th.addTab(ts6);
        th.addTab(ts7);
        th.addTab(ts8);
        th.addTab(ts9);
        th.addTab(ts13);
        th.addTab(ts14);
        th.addTab(ts15);
        th.addTab(ts17);
        th.addTab(ts18);
        th.addTab(ts19);
        //th.addTab(ts20);

        //Listener for tabchange so that header title can be changed
        th.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("myprofile")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("My Profile");
                } else if(tabId.equals("messages")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("My Messages");
                } else if(tabId.equals("tickets")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("My Tickets");
                } else if(tabId.equals("search")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("Search Results");
                } else if(tabId.equals("makeadate")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("My Dates");
                } else if(tabId.equals("myprofile_ratings")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("My Ratings");
                } else if(tabId.equals("messages_chat")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("Chat");
                } else if(tabId.equals("tickets_newoffer")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("Offer Your Ticket");
                } else if(tabId.equals("makeadate_new")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("Create A New Date");
                } else if(tabId.equals("makeadate_search")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("Search For Dates");
                } else if(tabId.equals("makeadate_search_result")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("Searchs Results");
                } else if(tabId.equals("foreign_profile")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("Profile");
                } else if(tabId.equals("foreign_profile_ratings")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("Ratings");
                } else if (tabId.equals("tickets_search")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("Search For Tickets");
                } else if (tabId.equals("edit_myprofile")) {
                    ((TextView)findViewById(R.id.headerTitle)).setText("Edit My Profile");
                } /*else if (tabId.equals("change_password")) {
                    ((TextView) findViewById(R.id.headerTitle)).setText("Change your password");
                } */else {
                    ((TextView)findViewById(R.id.headerTitle)).setText("TicketMatch");
                }
            }
        });
    }



    //Catch MyProfile Take Photo and Upload Photo --> set photo on screen, compress and upload to database
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 & resultCode==RESULT_OK){
            if (data != null) {
                Bitmap bm_camera = (Bitmap)data.getExtras().get("data");
                ((ImageButton)th.getCurrentView().findViewById(R.id.myprofile_image)).setImageBitmap(bm_camera);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm_camera.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte [] ba = bytes.toByteArray();
                mStorage.child("images/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg").putBytes(ba);
            }
        } else if(requestCode==2 & resultCode==RESULT_OK){
            if (data != null) {
                try {
                    Bitmap bm_upload = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    ((ImageButton)th.getCurrentView().findViewById(R.id.myprofile_image)).setImageBitmap(bm_upload);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm_upload.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    byte [] ba = bytes.toByteArray();
                    mStorage.child("images/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg").putBytes(ba);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Catch the permission request for taking or uploading a photo
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case TicketMatch.MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    TicketMatch.takePhoto(this);
                } else {
                    // permission was denied
                    Toast.makeText(getApplicationContext(),"Cannot take photo without permissions", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case TicketMatch.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    TicketMatch.uploadPhoto(this);
                } else {
                    // permission was denied
                    Toast.makeText(getApplicationContext(),"Cannot upload photo without permissions", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    //method for header menu button
    public void btn_tm_logo(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        if (pid.equals("firebase")) inflater.inflate(R.menu.popup_menu, popup.getMenu());
        else if (pid.equals("facebook")) inflater.inflate(R.menu.popup_menu_fb, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit_profile:
                        Intent editprofile =  new Intent(getApplicationContext(), EditMyProfile.class);
                        startActivity(editprofile);
                        //th.setCurrentTabByTag("edit_myprofile");
                        return true;
                    case R.id.change_password:
                        Intent changepassword =  new Intent(getApplicationContext(), ChangePassword.class);
                        startActivity(changepassword);
                        //th.setCurrentTabByTag("change_password");
                        return true;
                    case R.id.logout:
                        //stops IntentService: MessageNotifications
                        if (mn != null) {
                            mn.stopSelf();
                        } else {
                            stopService(mServiceIntent);
                        }
                        editor.remove("UID");
                        editor.remove("PID");
                        editor.commit();
                        // Firebase Logout
                        FirebaseAuth.getInstance().signOut();
                        //Facebook Logout
                        LoginManager.getInstance().logOut();
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainActivity);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        String tabId = th.getCurrentTabTag();
        if(tabId.equals("search")) {
            th.setCurrentTabByTag("tickets_search");
        } else if(tabId.equals("myprofile_ratings")) {
            th.setCurrentTabByTag("myprofile");
        } else if(tabId.equals("messages_chat")) {
            th.setCurrentTabByTag("messages");
        } else if(tabId.equals("tickets_newoffer")) {
            th.setCurrentTabByTag("tickets");
        } else if(tabId.equals("makeadate_new")) {
            th.setCurrentTabByTag("makeadate");
        } else if(tabId.equals("makeadate_search")) {
            th.setCurrentTabByTag("makeadate");
        } else if(tabId.equals("makeadate_search_result")) {
            th.setCurrentTabByTag("makeadate_search");
        } else if(tabId.equals("foreign_profile")) {
            th.setCurrentTabByTag("messages_chat");
        } else if(tabId.equals("foreign_profile_ratings")) {
            th.setCurrentTabByTag("foreign_profile");
        } else if (tabId.equals("tickets_search")) {
            th.setCurrentTabByTag("tickets");
        } else if (tabId.equals("edit_myprofile")) {
            th.setCurrentTabByTag("myprofile");
        }
    }
}
