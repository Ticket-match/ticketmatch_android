package de.ticket_match.ticketmatch;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.ticket_match.ticketmatch.entities.Ticket;

public class Offer_Overview extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Ticket> tickets = new ArrayList<Ticket>(0);
    private ArrayList<String> tickets_keys = new ArrayList<String>(0);

    TabHost mTabHost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer__overview);

        mTabHost = ((TabHost)getParent().findViewById(R.id.tabHost));

        ((MainActivityTabHost) getParent()).baseBundle.putSerializable("tickets_offerdetail", tickets);
        ((MainActivityTabHost) getParent()).baseBundle.putSerializable("tickets_offerdetail_keys", tickets_keys);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.offeroverview_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        final RVAdapter_OfferOverview adapter = new RVAdapter_OfferOverview(tickets, this, tickets_keys);
        rv.setAdapter(adapter);

        mDatabase.child("tickets").orderByChild("user").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()) {
                    Ticket ticket = d.getValue(Ticket.class);
                    tickets.add(ticket);
                    tickets_keys.add((String)d.getKey());
                }

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                Date dl,dr;

                for (int n = 0; n < tickets.size(); n++) {
                    for (int m = 0; m < (tickets.size()-1) - n; m++) {
                        try {
                            dl = dateFormat.parse(tickets.get(m).getDate());
                            dr = dateFormat.parse(tickets.get(m+1).getDate());
                            int i = dl.compareTo(dr);

                            if (i<0) {
                                Ticket swapTicket = tickets.get(m);
                                tickets.set(m,tickets.get(m+1));
                                tickets.set(m+1,swapTicket);

                                String swapKey = tickets_keys.get(m);
                                tickets_keys.set(m,tickets_keys.get(m+1));
                                tickets_keys.set(m+1,swapKey);
                            } else if (i==0) {
                                dl = timeFormat.parse(tickets.get(m).getTime());
                                dr = timeFormat.parse(tickets.get(m+1).getTime());
                                i = dl.compareTo(dr);

                                if (i<0) {
                                    Ticket swapTicket = tickets.get(m);
                                    tickets.set(m,tickets.get(m+1));
                                    tickets.set(m+1,swapTicket);

                                    String swapKey = tickets_keys.get(m);
                                    tickets_keys.set(m,tickets_keys.get(m+1));
                                    tickets_keys.set(m+1,swapKey);
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }

                (((RecyclerView) findViewById(R.id.offeroverview_list)).getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    public void btn_newoffer (View view){
        mTabHost.setCurrentTabByTag("tickets_newoffer");
    }

    public void btn_search_ticket (View view){
        mTabHost.setCurrentTabByTag("tickets_search");
    }

    public void btn_ticket_delete (View view) {
        String ticket_key = getTicketKeyFromCardItem(view);
        deleteTicket(ticket_key);
    }

    public void btn_ticket_edit (View view) {
        String ticket_key = getTicketKeyFromCardItem(view);
        Ticket ticket = getTicketFromCardItem(view);
        mTabHost.setCurrentTabByTag("tickets_newoffer");
        NewOffer newOffer = (NewOffer) mTabHost.getCurrentView().getContext();
        newOffer.setEditTicket(true);
        newOffer.setTicketKey(ticket_key);
        newOffer.setTicketTextFields(ticket);
    }

    public String getTicketKeyFromCardItem(View view){
        //We have an invisible Textfield in the style file to get the ticket key.
        return ((TextView)((View)view.getParent().getParent()).findViewById(R.id.row_key)).getText().toString();
    }

    public Ticket getTicketFromCardItem(View view){
        String ticket_key = getTicketKeyFromCardItem(view);
        int ticket_position = tickets_keys.indexOf(ticket_key);
        Ticket ticket = tickets.get(ticket_position);
        return ticket;
    }


    /**
     * Deletes a ticket out of the Firebase Database and out of
     * the bundle inside of the application.
     * @param FirebaseTicketKey Ticket Key, generated by Firebase
     * @see Ticket
     */
    public void deleteTicket(String FirebaseTicketKey){
        int ticketPosition = tickets_keys.indexOf(FirebaseTicketKey);
        tickets_keys.remove(ticketPosition);
        tickets.remove(ticketPosition);
        (((RecyclerView) findViewById(R.id.offeroverview_list)).getAdapter()).notifyDataSetChanged();
        mDatabase.child("tickets").child(FirebaseTicketKey).removeValue();

    }

}