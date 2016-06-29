package de.ticket_match.ticketmatch;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.ticket_match.ticketmatch.entities.Chat;
import de.ticket_match.ticketmatch.entities.Ticket;

/**
 * Created by D060450 on 22.06.2016.
 */
public class RVAdapter_TicketSearchResults extends RecyclerView.Adapter<RVAdapter_TicketSearchResults.SearchTicketViewholder> {

    List<Ticket> tickets;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    Activity prevActivity;

    public RVAdapter_TicketSearchResults(List<Ticket> tickets, Activity prevActivity) {
        this.tickets = tickets;
        this.prevActivity = prevActivity;
    }

    @Override
    public SearchTicketViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewitem_ticketresults, parent, false);
        SearchTicketViewholder stvh = new SearchTicketViewholder(v);
        return stvh;
    }

    @Override
    public void onBindViewHolder(SearchTicketViewholder holder, final int position) {
        Ticket ticket = tickets.get(position);

        holder.type.setText(ticket.getName() + "\n" + ticket.getType());
        holder.date.setText(ticket.getDate() + "\n" + ticket.getLocation());
        holder.time.setText(ticket.getTime() + "\n" + ticket.getQuantity() + "pc. | " + ticket.getPrice().get("value") + " " + ticket.getPrice().get("currency"));

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Ticket text = tickets.get(position);

                final String message = "Hello, " + "\n" + "I am interested in your tickets: " + "\n" + text.getName() + "\n" + text.getType() + "\n" + text.getLocation() + "\n" + text.getDate() + " | " + text.getTime() + "\n" + text.getQuantity() + " pc. | " + text.getPrice().get("value") + " " + text.getPrice().get("currency");


                //create Dialog for asking if the vendor should be contacted
                AlertDialog.Builder builder = new AlertDialog.Builder(prevActivity.getParent());
                builder.setMessage("Do you want to contact the ticket vendor?").setTitle("Contact Vendor");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        final ArrayList<HashMap<String,Chat>> chats = new ArrayList<HashMap<String,Chat>>(0);

                        //Database Call if there is a chat with the participants vendor and user of app
                        //Database Call if there is a chat with the app user as a participant
                        mDatabase.child("chats").orderByChild("participant1").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot d : dataSnapshot.getChildren()) {
                                    Chat chat = d.getValue(Chat.class);
                                    if(chat.getParticipant2().equals(text.getUser())){
                                        //get Message key if there is a chat with both participants
                                        HashMap<String, Chat> key = new HashMap<String, Chat>(0);
                                        key.put((String)d.getKey(), chat);
                                        chats.add(key);
                                    }
                                }
                                //if there is no chat with the app user, it will be asked if there is a chat with the ticket vendor
                                if (chats.size()==0){
                                    mDatabase.child("chats").orderByChild("participant2").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                                Chat chat = d.getValue(Chat.class);
                                                if(chat.getParticipant1().equals(text.getUser())){
                                                    //get Message key if there is a chat with both participants
                                                    HashMap<String, Chat> key = new HashMap<String, Chat>(0);
                                                    key.put((String)d.getKey(), chat);
                                                    chats.add(key);
                                                }
                                            }
                                            //if there is neither a chat with the ticket vendor nor with the app user, a new chat will be created in the database
                                            if(chats.size()==0){
                                                ArrayList<HashMap<String, String>> a = new ArrayList<HashMap<String, String>>(0);
                                                HashMap<String, String> hm = new HashMap<String, String>(0);
                                                hm.put("author", ((MainActivityTabHost)prevActivity.getParent()).baseBundle.getString("myprofile_name"));

                                                //get actual Date
                                                final Calendar c = Calendar.getInstance();
                                                String date = "" + c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH)+1) + "." + c.get(Calendar.YEAR);

                                                //get actual Time, if hours are less than 10 a zero will be added
                                                final String time;
                                                if(c.get(Calendar.MINUTE) < 10){
                                                    time = "" + c.get(Calendar.HOUR_OF_DAY) + ":0" + c.get(Calendar.MINUTE);
                                                }else{
                                                    time = "" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
                                                }

                                                hm.put("date", date);
                                                hm.put("text", message);
                                                hm.put("timestamp", time);

                                                a.add(hm);

                                                Chat chat = new Chat(FirebaseAuth.getInstance().getCurrentUser().getUid(), text.getUser(), a, hm);
                                                mDatabase.child("chats").push().setValue(chat);
                                                Toast.makeText(prevActivity.getApplicationContext(), "You sent a request to buy the ticket!", Toast.LENGTH_SHORT).show();
                                            } else{

                                                HashMap<String, String> hm = new HashMap<String, String>(0);
                                                hm.put("author", ((MainActivityTabHost)prevActivity.getParent()).baseBundle.getString("myprofile_name"));

                                                //get actual Date
                                                final Calendar c = Calendar.getInstance();
                                                String date = "" + c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH)+1) + "." + c.get(Calendar.YEAR);

                                                //get actual Time, if hours are less than 10 a zero will be added
                                                final String time;
                                                if(c.get(Calendar.MINUTE) < 10){
                                                    time = "" + c.get(Calendar.HOUR_OF_DAY) + ":0" + c.get(Calendar.MINUTE);
                                                }else{
                                                    time = "" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
                                                }

                                                hm.put("date", date);
                                                hm.put("text", message);
                                                hm.put("timestamp", time);

                                                HashMap<String, Chat> key = chats.get(0);
                                                Set<String> keys = key.keySet();
                                                Chat chat = key.get(keys.toArray()[0]);
                                                mDatabase.child("chats").child((String)keys.toArray()[0]).child("messages").child(String.valueOf(chat.getMessages().size())).setValue(hm);
                                                mDatabase.child("chats").child((String)keys.toArray()[0]).child("lastMessage").setValue(hm);
                                                Toast.makeText(prevActivity.getApplicationContext(), "You sent a request to buy the ticket!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else{

                                    HashMap<String, String> hm = new HashMap<String, String>(0);
                                    hm.put("author", ((MainActivityTabHost)prevActivity.getParent()).baseBundle.getString("myprofile_name"));

                                    //get actual Date
                                    final Calendar c = Calendar.getInstance();
                                    String date = "" + c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH)+1) + "." + c.get(Calendar.YEAR);

                                    //get actual Time, if hours are less than 10 a zero will be added
                                    final String time;
                                    if(c.get(Calendar.MINUTE) < 10){
                                        time = "" + c.get(Calendar.HOUR_OF_DAY) + ":0" + c.get(Calendar.MINUTE);
                                    }else{
                                        time = "" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
                                    }

                                    hm.put("date", date);
                                    hm.put("text", message);
                                    hm.put("timestamp", time);

                                    HashMap<String, Chat> key = chats.get(0);
                                    Set<String> keys = key.keySet();
                                    Chat chat = key.get(keys.toArray()[0]);
                                    mDatabase.child("chats").child((String)keys.toArray()[0]).child("messages").child(String.valueOf(chat.getMessages().size())).setValue(hm);
                                    mDatabase.child("chats").child((String)keys.toArray()[0]).child("lastMessage").setValue(hm);
                                    Toast.makeText(prevActivity.getApplicationContext(), "You sent a request to buy the ticket!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();

                dialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }


    public static class SearchTicketViewholder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView type;
        TextView date;
        TextView time;

        SearchTicketViewholder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_ticketresult);
            type = (TextView) itemView.findViewById(R.id.ticketresult_type);
            date = (TextView) itemView.findViewById(R.id.ticketresult_date);
            time = (TextView) itemView.findViewById(R.id.ticketresult_time);
        }
    }
}
