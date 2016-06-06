package de.ticket_match.ticketmatch;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivityTabHost extends AppCompatActivity {
    Bundle baseBundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_tab_host);

        // TabHost Setup
        TabHost th = (TabHost)findViewById(R.id.tabHost);
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        th.setup(mLocalActivityManager);

        // Tab "My Profile"
        TabHost.TabSpec ts1 = th.newTabSpec("myprofile");
        View ts1Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator, th.getTabWidget(), false);
        ((TextView) ts1Ind.findViewById(android.R.id.title)).setText("My Profile");
        ((ImageView) ts1Ind.findViewById(android.R.id.icon)).setImageResource(R.drawable.contacts);
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
        ((TextView) ts2Ind.findViewById(android.R.id.title)).setText("Messages");
        ((ImageView) ts2Ind.findViewById(android.R.id.icon)).setImageResource(R.drawable.message);
        ts2.setIndicator(ts2Ind);
        ts2.setContent(new Intent(this, Message_Overview.class));

        // Tab "Messages Chat"
        TabHost.TabSpec ts7 = th.newTabSpec("messages_chat");
        View ts7Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts7.setIndicator(ts7Ind);
        ts7.setContent(new Intent(this, Message_Chat.class));

        // Tab "Tickets"
        TabHost.TabSpec ts3 = th.newTabSpec("tickets");
        View ts3Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator, th.getTabWidget(), false);
        ((TextView) ts3Ind.findViewById(android.R.id.title)).setText("Tickets");
        ((ImageView) ts3Ind.findViewById(android.R.id.icon)).setImageResource(R.drawable.ticket);
        ts3.setIndicator(ts3Ind);
        ts3.setContent(new Intent(this, Offer_Overview.class));

        // Tab "Tickets New Offer"
        TabHost.TabSpec ts8 = th.newTabSpec("tickets_newoffer");
        View ts8Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts8.setIndicator(ts8Ind);
        ts8.setContent(new Intent(this, NewOffer.class));

        // Tab "Tickets Offer Detail"
        TabHost.TabSpec ts9 = th.newTabSpec("tickets_offerdetail");
        View ts9Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts9.setIndicator(ts9Ind);
        ts9.setContent(new Intent(this, Offer_Detail.class));

        // Tab "Search"
        TabHost.TabSpec ts4 = th.newTabSpec("search");
        View ts4Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator, th.getTabWidget(), false);
        ((TextView) ts4Ind.findViewById(android.R.id.title)).setText("Search");
        ((ImageView) ts4Ind.findViewById(android.R.id.icon)).setImageResource(R.drawable.search);
        ts4.setIndicator(ts4Ind);
        ts4.setContent(new Intent(this, Find.class));

        // Tab "Search Vendor"
        TabHost.TabSpec ts10 = th.newTabSpec("search_vendor");
        View ts10Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts10.setIndicator(ts10Ind);
        ts10.setContent(new Intent(this, Find_Vendor.class));

        // Tab "Search Vendor Message"
        TabHost.TabSpec ts11 = th.newTabSpec("search_vendor_message");
        View ts11Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts11.setIndicator(ts11Ind);
        ts11.setContent(new Intent(this, Message_Vendor.class));

        // Tab "Make A Date"
        TabHost.TabSpec ts5 = th.newTabSpec("makeadate");
        View ts5Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator, th.getTabWidget(), false);
        ((TextView) ts5Ind.findViewById(android.R.id.title)).setText("Make A Date");
        ((ImageView) ts5Ind.findViewById(android.R.id.icon)).setImageResource(R.drawable.group);
        ts5.setIndicator(ts5Ind);
        ts5.setContent(new Intent(this, MakeADate.class));

        // Tab "Make A Date Detail"
        TabHost.TabSpec ts12 = th.newTabSpec("makeadate_detail");
        View ts12Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts12.setIndicator(ts12Ind);
        ts12.setContent(new Intent(this, MakeADate_Detail.class));

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

        // Tab "Make A Date Search Result Message"
        TabHost.TabSpec ts16 = th.newTabSpec("makeadate_search_result_message");
        View ts16Ind = getLayoutInflater().inflate(R.layout.activity_main_activity_tab_indicator_inv, th.getTabWidget(), false);
        ts16.setIndicator(ts16Ind);
        ts16.setContent(new Intent(this, MakeADateResults_Message.class));

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
        th.addTab(ts10);
        th.addTab(ts11);
        th.addTab(ts12);
        th.addTab(ts13);
        th.addTab(ts14);
        th.addTab(ts15);
        th.addTab(ts16);
        th.addTab(ts17);
        th.addTab(ts18);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void btn_tm_logo(View view) {

        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.change_password:
                        Intent changepassword =  new Intent(getApplicationContext(), ChangePassword.class);
                        startActivity(changepassword);
                        return true;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();

    }
}
