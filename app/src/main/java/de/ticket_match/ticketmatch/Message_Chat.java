package de.ticket_match.ticketmatch;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import de.ticket_match.ticketmatch.entities.Chat;
import de.ticket_match.ticketmatch.entities.Message;
import de.ticket_match.ticketmatch.entities.Ticket;
import de.ticket_match.ticketmatch.entities.User;

public class Message_Chat extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Message> messages = new ArrayList<Message>(0);
    boolean check = false;
    String check_old;
    String fouid;

    ValueEventListener vel = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            messages.clear();
            for (DataSnapshot d:dataSnapshot.getChildren() ) {
                Message m = d.getValue(Message.class);
                messages.add(m);
            }
            ((ChatAdapter)((ListView)findViewById(R.id.message_chat_list)).getAdapter()).notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public static class ChatAdapter extends BaseAdapter {
        ArrayList<Message> result;
        String myusername;
        Context context;
        private static LayoutInflater inflater=null;

        public ChatAdapter(Message_Chat mainActivity, ArrayList<Message>  messages, String myusername) {
            result=messages;
            this.myusername=myusername;
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

            Message message = result.get(position);
            View rowView;

            if (message.getAuthor().equals(myusername)) {
                rowView = inflater.inflate(R.layout.listitem_message_chat_right, null);
                ((TextView) rowView.findViewById(R.id.listitem_messagechat)).setText(message.getText() + "\n" + message.getAuthor());
                ((TextView) rowView.findViewById(R.id.listitem_messagechat_date)).setText(message.getTimestamp() + "\n" + message.getDate());
            } else {
                rowView = inflater.inflate(R.layout.listitem_message_chat_left, null);
                ((TextView) rowView.findViewById(R.id.listitem_messagechat)).setText(message.getText() + "\n" + message.getAuthor());
                ((TextView) rowView.findViewById(R.id.listitem_messagechat_date)).setText(message.getTimestamp() + "\n" + message.getDate());
            }

            return rowView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__chat);
    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    public void updateList(String chatKey, String foreignKey) {
        messages.clear();

        if (!check) {
            ((ListView) findViewById(R.id.message_chat_list)).setAdapter(new ChatAdapter(this,messages,((MainActivityTabHost)getParent()).baseBundle.getString("myprofile_name")));
            mDatabase.child("chats").child(chatKey).child("messages").addValueEventListener(vel);
            check_old = chatKey;
            check = true;
        } else if (check) {
            mDatabase.child("chats").child(check_old).child("messages").removeEventListener(vel);
            mDatabase.child("chats").child(chatKey).child("messages").addValueEventListener(vel);
            check_old = chatKey;
        }

        mDatabase.child("users").child(foreignKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User f = dataSnapshot.getValue(User.class);
                ((TextView)findViewById(R.id.chat_with_name)).setText("Chatting with " + f.getFirstName() + " " + f.getLastName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        this.fouid = foreignKey;
    }

    public void btn_go_foreign(View view) {
        ((MainActivityTabHost)getParent()).baseBundle.putString(TicketMatch.FOREIGN_PROFILE_UID, fouid);
        ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("foreign_profile");
        ((ForeignProfile)((TabHost)getParent().findViewById(R.id.tabHost)).getCurrentView().getContext()).updateFProfile(fouid);
    }

    public void btn_send_message(View view) {
        EditText e = (EditText)findViewById(R.id.messageInput);

        if (!e.getText().toString().equals("")) {
            //get actual Date
            final Calendar c = Calendar.getInstance();
            String date = "" + c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH)+1) + "." + c.get(Calendar.YEAR);

            //get actual Time, if hours are less than 10 a zero will be added
            String time;
            if(c.get(Calendar.MINUTE) < 10){
                time = "" + c.get(Calendar.HOUR_OF_DAY) + ":0" + c.get(Calendar.MINUTE);
            }else{
                time = "" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
            }

            Message m = new Message(((MainActivityTabHost)getParent()).baseBundle.getString("myprofile_name"),date,time,e.getText().toString());
            mDatabase.child("chats").child(check_old).child("messages").child(String.valueOf(messages.size())).setValue(m);

            e.setText("");
        }
    }
}