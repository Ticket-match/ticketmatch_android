package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

public class Find extends AppCompatActivity {

    ArrayList<Ticket> listitems_find;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        listview = (ListView) findViewById(R.id.find_results);

        listitems_find = (ArrayList<Ticket>)((MainActivityTabHost) getParent()).baseBundle.getSerializable("tickets_search_result");
        listview.setAdapter(new CustomAdapter(this, listitems_find));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Ticket text = listitems_find.get(position);

                final String message = "Hello, " + "\n" + "I am interested in your tickets: " + "\n" + text.getName() + "\n" + text.getType() + "\n" + text.getDate() + " | " + text.getTime() + "\n" + text.getQuantity() + " pc. | " + text.getPrice().get("value") + " " + text.getPrice().get("currency");


                //create Dialog for asking if the vendor should be contacted
                AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
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
                                                    hm.put("author", ((MainActivityTabHost)getParent()).baseBundle.getString("myprofile_name"));

                                                    //get actual Date
                                                    final Calendar c = Calendar.getInstance();
                                                    String date = "" + c.get(Calendar.DAY_OF_MONTH) + "." + c.get(Calendar.MONTH) + "." + c.get(Calendar.YEAR);

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

                                                    Chat chat = new Chat(FirebaseAuth.getInstance().getCurrentUser().getUid(), text.getUser(), a);
                                                    mDatabase.child("chats").push().setValue(chat);
                                                } else{

                                                    HashMap<String, String> hm = new HashMap<String, String>(0);
                                                    hm.put("author", ((MainActivityTabHost)getParent()).baseBundle.getString("myprofile_name"));

                                                    //get actual Date
                                                    final Calendar c = Calendar.getInstance();
                                                    String date = "" + c.get(Calendar.DAY_OF_MONTH) + "." + c.get(Calendar.MONTH) + "." + c.get(Calendar.YEAR);

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
                                                    ArrayList<HashMap<String, String>> messages = chat.getMessages();
                                                    messages.add(hm);
                                                    mDatabase.child("chats").child((String)keys.toArray()[0]).child("messages").setValue(messages);
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
                                    String date = "" + c.get(Calendar.DAY_OF_MONTH) + "." + c.get(Calendar.MONTH) + "." + c.get(Calendar.YEAR);

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
                                    ArrayList<HashMap<String, String>> messages = chat.getMessages();
                                    messages.add(hm);
                                    mDatabase.child("chats").child((String)keys.toArray()[0]).child("messages").setValue(messages);
                                }
                                }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("Cancel");
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

    public static class CustomAdapter extends BaseAdapter {
        ArrayList<Ticket> result;
        Context context;
        private static LayoutInflater inflater=null;

        public CustomAdapter(Find mainActivity, ArrayList<Ticket>  findlist) {
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

            Ticket text = result.get(position);

            View rowView = inflater.inflate(R.layout.listitem_find, null);

            ((TextView) rowView.findViewById(R.id.row_type)).setText(text.getName() + "\n" + text.getType());
            ((TextView) rowView.findViewById(R.id.row_date)).setText(text.getDate() + "\n" + text.getLocation());
            ((TextView) rowView.findViewById(R.id.row_time)).setText(text.getTime() + "\n" + text.getQuantity() + "pc. | " + text.getPrice().get("value") + " " + text.getPrice().get("currency"));
            return rowView;
        }
    }
}
