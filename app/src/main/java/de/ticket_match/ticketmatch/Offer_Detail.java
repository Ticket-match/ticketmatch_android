package de.ticket_match.ticketmatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Offer_Detail extends AppCompatActivity {

    //ArrayList<Ticket> ticket_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer__detail);

        //ticket_details = (ArrayList<Ticket>)((MainActivityTabHost) getParent()).baseBundle.getSerializable("tickets_offerdetail");
        Ticket ticket = ((ArrayList<Ticket>)((MainActivityTabHost) getParent()).baseBundle.getSerializable("tickets_offerdetail")).get(((MainActivityTabHost) getParent()).baseBundle.getInt("tickets_offerdetail_position"));

        String type = ticket.getType();
        String eventname = ticket.getName();
        String date = ticket.getDate();
        HashMap<String, String> price = ticket.getPrice();
        String location = ticket.getLocation();
        String quantity = ticket.getQuantity();

        TextView t = (TextView)findViewById(R.id.ticket_detail);
        t.setText(type+"\n"+eventname+"\n"+date+"\n"+price.get("value") + " " + price.get("currency") + "\n" + location + "\n" + quantity);
    }

    public void btn_delete_offer (View view){
        int position = ((MainActivityTabHost) getParent()).baseBundle.getInt("tickets_offerdetail_position");
        ((ArrayList<Ticket>)((MainActivityTabHost) getParent()).baseBundle.getSerializable("tickets_offerdetail")).remove(position);
        FirebaseDatabase.getInstance().getReference().child("tickets").child(((ArrayList<String>)((MainActivityTabHost) getParent()).baseBundle.getSerializable("tickets_offerdetail_keys")).get(position)).removeValue();
        ((ArrayList<String>)((MainActivityTabHost) getParent()).baseBundle.getSerializable("tickets_offerdetail_keys")).remove(position);
        ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("tickets");
        ((Offer_Overview.MyTicketAdapter)((HeaderViewListAdapter)((ListView)((TabHost)getParent().findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.offeroverview_list)).getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
    }
}
