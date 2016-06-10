package de.ticket_match.ticketmatch;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Offer_Overview extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Ticket> tickets = new ArrayList<Ticket>(0);
    private ArrayList<String> tickets_keys = new ArrayList<String>(0);

    public static class MyTicketAdapter extends BaseAdapter {
        ArrayList<Ticket> result;
        ArrayList<String> ticket_keys;
        Context context;
        private static LayoutInflater inflater=null;

        public MyTicketAdapter(Offer_Overview mainActivity, ArrayList<Ticket>  tickets, ArrayList<String> ticket_keys) {
            result=tickets;
            this.ticket_keys = ticket_keys;
            context=mainActivity;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return result.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Ticket ticket = result.get(position);
            String ticket_key = ticket_keys.get(position);
            String type = ticket.getType();
            String name = ticket.getName();
            String date = ticket.getDate();

            View rowView = inflater.inflate(R.layout.listitem_offeroverview, null);

            ((TextView) rowView.findViewById(R.id.row_type)).setText(ticket.getName() + "\n" + ticket.getType());
            ((TextView) rowView.findViewById(R.id.row_name)).setText(ticket.getDate() + "\n" + ticket.getLocation());
            ((TextView) rowView.findViewById(R.id.row_date)).setText(ticket.getTime() + "\n" + ticket.getQuantity() + "pc. | " + ticket.getPrice().get("value") + " " + ticket.getPrice().get("currency"));
            ((TextView) rowView.findViewById(R.id.row_key)).setText(ticket_key);
            return rowView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer__overview);

        ((MainActivityTabHost) getParent()).baseBundle.putSerializable("tickets_offerdetail", tickets);
        ((MainActivityTabHost) getParent()).baseBundle.putSerializable("tickets_offerdetail_keys", tickets_keys);

        ListView listview = (ListView) findViewById(R.id.offeroverview_list);

        //ViewGroup header = (ViewGroup)getLayoutInflater().inflate(R.layout.offeroverview_headerlayout, listview, false);
        //listview.addHeaderView(header);

        listview.setAdapter(new MyTicketAdapter(this, tickets, tickets_keys));

        mDatabase.child("tickets").orderByChild("user").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()) {
                    Ticket ticket = d.getValue(Ticket.class);
                    tickets.add(ticket);
                    tickets_keys.add((String)d.getKey());
                    //((MyTicketAdapter)((HeaderViewListAdapter)((ListView)findViewById(R.id.offeroverview_list)).getAdapter()).getWrappedAdapter()).notifyDataSetChanged()
                }
                ((MyTicketAdapter)(((ListView)findViewById(R.id.offeroverview_list)).getAdapter())).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivityTabHost) getParent()).baseBundle.putInt("tickets_offerdetail_position", position-1);
                ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("tickets_offerdetail");
            }
        });
    }

    public void btn_newoffer (View view){
        ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("tickets_newoffer");
    }

    public void btn_search_ticket (View view){
        ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("tickets_search");
    }

    public void btn_ticket_delete (View view) {
        String ticket_key = ((TextView)((View)view.getParent()).findViewById(R.id.row_key)).getText().toString();
        int ticket_position = tickets_keys.indexOf(ticket_key);
        tickets_keys.remove(ticket_position);
        tickets.remove(ticket_position);
        ((MyTicketAdapter)((ListView) findViewById(R.id.offeroverview_list)).getAdapter()).notifyDataSetChanged();
        mDatabase.child("tickets").child(ticket_key).removeValue();
    }


}
