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
            String date = "" + c.get(Calendar.DAY_OF_MONTH) + "." + c.get(Calendar.MONTH) + "." + c.get(Calendar.YEAR);

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

/*public class Message_Chat extends ListActivity {

    private String mUsername;
    private DatabaseReference mFirebaseRef = FirebaseDatabase.getInstance().getReference();;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;

    private String foreignUid;

    private String currentKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__chat);

        // Make sure we have a mUsername
        mUsername = ((MainActivityTabHost)getParent()).baseBundle.getString("myprofile_name");

        //Find uid of chat partner and put it in the baseBundle
        String p1 = ((MainActivityTabHost) getParent()).baseBundle.getString("chat_p1");
        String p2 = ((MainActivityTabHost) getParent()).baseBundle.getString("chat_p2");
        foreignUid = null;
        if(p1.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            foreignUid = p2;
        } else {
            foreignUid = p1;
        }
        ((MainActivityTabHost) getParent()).baseBundle.putString(TicketMatch.FOREIGN_PROFILE_UID, foreignUid);



        //createChatList();
    }



    public void createChatList() {
        mFirebaseRef = mFirebaseRef.child("chats");

        mFirebaseRef.child(((MainActivityTabHost) getParent()).baseBundle.getString("messages_chat_key")).child("messages").orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int keyInt;
                if(dataSnapshot.exists()) {
                    keyInt = Integer.parseInt(dataSnapshot.getKey()) + 1;
                } else {
                    keyInt = 0;
                }

                currentKey = String.valueOf(keyInt);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.send_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        //Was used before
        //mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), this, R.layout.message, mUsername);
        //parameter should be messages from base bundle !!!
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.child(((MainActivityTabHost) getParent()).baseBundle.getString("messages_chat_key")).child("messages"), this, R.layout.message, mUsername);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.notifyDataSetChanged();
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });
    }

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
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

            Message message = new Message(mUsername, date, time,  input);
            mFirebaseRef.child(((MainActivityTabHost) getParent()).baseBundle.getString("messages_chat_key")).child("messages").child(currentKey).setValue(message);

            inputText.setText("");
        }
    }

    //Navigate to foreign profile of chat partner
    public void btn_go_foreign(View view) {
                ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("foreign_profile");
    }

}*/