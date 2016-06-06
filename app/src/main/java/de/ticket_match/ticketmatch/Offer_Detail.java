package de.ticket_match.ticketmatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Offer_Detail extends AppCompatActivity {
    Bundle bund;
    String offerdetails = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer__detail);


        bund = ((MainActivityTabHost)getParent()).baseBundle;
        offerdetails = bund.getString("offer");


        String type = offerdetails.substring(0, offerdetails.indexOf("|"));
        String eventname = offerdetails.substring(offerdetails.indexOf("|")+1,offerdetails.indexOf("|",offerdetails.indexOf("|")+1));
        String date = offerdetails.substring(offerdetails.indexOf("|",offerdetails.indexOf("|")+1)+1,offerdetails.length());


//+time+"\n"+price+"\n"+location
        //String time = offerdetails.substring(offerdetails.indexOf("|",offerdetails.indexOf("|")+1)+1,offerdetails.length());
        //String price = offerdetails.substring(offerdetails.indexOf("|",offerdetails.indexOf("|")+1)+1,offerdetails.length());
        //String location = offerdetails.substring(offerdetails.indexOf("|",offerdetails.indexOf("|")+1)+1,offerdetails.length());

        TextView t = (TextView)findViewById(R.id.ticket_detail);
        t.setText(type+"\n"+eventname+"\n"+date+"\n");

    }

    public void btn_delete_offer (View view){

        // Backend delete Row in Database; Open new Screen without dataset
        //Intent offer_overview = new Intent(getApplicationContext(), Offer_Overview.class);
        //startActivity(offer_overview);
        ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("tickets");

    }

    /*
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
    */

}
