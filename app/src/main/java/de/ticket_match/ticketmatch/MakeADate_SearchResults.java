package de.ticket_match.ticketmatch;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import java.util.Set;

import de.ticket_match.ticketmatch.entities.Chat;
import de.ticket_match.ticketmatch.entities.MakeDate;

public class MakeADate_SearchResults extends AppCompatActivity {

    ListView listview;
    ArrayList<MakeDate> listitems_makeadate_results= new ArrayList<MakeDate>(0);
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_adate__search_results);

        listview = (ListView) findViewById(R.id.listview_makeadate_results);
        listitems_makeadate_results = (ArrayList<MakeDate>)((MainActivityTabHost) getParent()).baseBundle.getSerializable("makeadate_search_result");
        listview.setAdapter(new CustomAdapter(this, listitems_makeadate_results));



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final MakeDate text = listitems_makeadate_results.get(position);

                final String message = "Hello, " + "\n" + "I am interested to meet you in " + text.getLocation()+ " on " + text.getDate() + " at " + text.getTime();


                //create Dialog for asking if the vendor should be contacted
                AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
                builder.setMessage("Do you want to join the date?").setTitle("Join Date");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
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
                                                hm.put("author", ((MainActivityTabHost)getParent()).baseBundle.getString("myprofile_name"));

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
                                            } else{

                                                HashMap<String, String> hm = new HashMap<String, String>(0);
                                                hm.put("author", ((MainActivityTabHost)getParent()).baseBundle.getString("myprofile_name"));

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
                                                Toast.makeText(getApplicationContext(), "You sent a request to join the date!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else{

                                    HashMap<String, String> hm = new HashMap<String, String>(0);
                                    hm.put("author", ((MainActivityTabHost)getParent()).baseBundle.getString("myprofile_name"));

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
                                    Toast.makeText(getApplicationContext(), "You sent a request to join the date!", Toast.LENGTH_SHORT).show();;
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
                //builder.create().show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    public static class CustomAdapter extends BaseAdapter {
        ArrayList<MakeDate> result;
        Context context;
        private static LayoutInflater inflater=null;

        public CustomAdapter(MakeADate_SearchResults mainActivity, ArrayList<MakeDate>  findlist) {
            result=findlist;
            context=mainActivity;
            inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            MakeDate text = result.get(position);

            View rowView = inflater.inflate(R.layout.listitem_find, null);

            String gender = "";
            if(text.isWithman().equals("true") & text.isWithwoman().equals("false")){
                gender = "man";
            }else if (text.isWithwoman().equals("true") & text.isWithman().equals("false")){
                gender = "woman";
            }else gender = "man | woman";

            ((TextView) rowView.findViewById(R.id.row_type)).setText(text.getName() + "\n" + text.getType());
            ((TextView) rowView.findViewById(R.id.row_date)).setText(text.getDate() + "\n" + text.getLocation());
            ((TextView) rowView.findViewById(R.id.row_time)).setText(text.getTime() + "\n" + gender);
            return rowView;
        }
    }
}
