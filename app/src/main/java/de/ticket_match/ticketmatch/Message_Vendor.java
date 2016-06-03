package de.ticket_match.ticketmatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

public class Message_Vendor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__vendor);
    }

    public void foreign_profile (View view){
        Intent foreign = new Intent(this, ForeignProfile.class);
        startActivity(foreign);
    }

    public void send_message (View view){
        Toast.makeText(getApplicationContext(),"Your message was sent!",Toast.LENGTH_SHORT).show();

       // Intent vendor = new Intent(this, Find_Vendor.class);
        //startActivity(vendor);
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
                        //ChangePasswordDialog cdp = new ChangePasswordDialog();
                        //cdp.show(getFragmentManager(), "cdp");
                        Intent changepassword =  new Intent(getApplicationContext(), ChangePassword.class);
                        startActivity(changepassword);
                        return true;
                    case R.id.logout:
                        Toast.makeText(getApplicationContext(),"logout",Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();

    }

    public void btn_profile(View view) {
        Intent myprofile = new Intent(this, MyProfile.class);
        startActivity(myprofile);
    }

    public void btn_message(View view) {
        Intent message = new Intent(this, Message_Overview.class);
        startActivity(message);
    }

    public void btn_ticketoffer(View view) {
        Intent offeroverview = new Intent(this, Offer_Overview.class);
        startActivity(offeroverview);
    }

    public void btn_search(View view) {
        Intent find = new Intent(this, Find.class);
        startActivity(find);
    }

    public void btn_makematch(View view) {
        Intent makeadate = new Intent(this, MakeADate.class);
        startActivity(makeadate);
    }
}
