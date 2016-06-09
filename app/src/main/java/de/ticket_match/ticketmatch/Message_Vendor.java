package de.ticket_match.ticketmatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TabHost;
import android.widget.Toast;

public class Message_Vendor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__vendor);

        ((EditText)findViewById(R.id.vendor_message)).setText(((MainActivityTabHost) getParent()).baseBundle.getString("tickets_search_message"));
    }

    public void foreign_profile (View view){
        //Intent foreign = new Intent(this, ForeignProfile.class);
        //startActivity(foreign);
        ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("foreign_profile");
    }

    public void send_message (View view){
        Toast.makeText(getApplicationContext(),"Your message was sent!",Toast.LENGTH_SHORT).show();

        //Intent vendor = new Intent(this, Find_Vendor.class);
        //startActivity(vendor);
        ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("search");
    }
}
