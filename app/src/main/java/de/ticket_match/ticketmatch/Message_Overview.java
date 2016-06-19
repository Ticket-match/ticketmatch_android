package de.ticket_match.ticketmatch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import de.ticket_match.ticketmatch.entities.Chat;
import de.ticket_match.ticketmatch.entities.User;

public class Message_Overview extends AppCompatActivity {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private ArrayList<Chat> chats = new ArrayList<Chat>(0);
    private ArrayList<String> chats_keys = new ArrayList<String>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__overview);


        final RecyclerView rv = (RecyclerView) findViewById(R.id.messages_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        //Added "this" to transfer this activity and chats_keys
        final RVAdapter adapter = new RVAdapter(chats, this, chats_keys);
        rv.setAdapter(adapter);

        mDatabase.child("chats").orderByChild("participant1").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()) {
                    Chat chat = d.getValue(Chat.class);
                    if (!chats_keys.contains(d.getKey())) {
                        chats.add(chat);
                        chats_keys.add(d.getKey());
                    } else if(chats_keys.contains(d.getKey())) {
                        Chat ref = chats.get(chats_keys.indexOf(d.getKey()));
                        if (!chat.getLastMessage().get("text").equals(ref.getLastMessage().get("text"))) {
                            chats.remove(chats_keys.indexOf(d.getKey()));
                            chats.add(chats_keys.indexOf(d.getKey()), chat);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("chats").orderByChild("participant2").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()) {
                    Chat chat = d.getValue(Chat.class);
                    if (!chats_keys.contains(d.getKey())) {
                        chats.add(chat);
                        chats_keys.add(d.getKey());
                    } else if(chats_keys.contains(d.getKey())) {
                        Chat ref = chats.get(chats_keys.indexOf(d.getKey()));
                        if (!chat.getLastMessage().get("text").equals(ref.getLastMessage().get("text"))) {
                            chats.remove(chats_keys.indexOf(d.getKey()));
                            chats.add(chats_keys.indexOf(d.getKey()), chat);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
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
}


/*
public class Message_Overview extends AppCompatActivity {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private ArrayList<Chat> chats = new ArrayList<Chat>(0);
    private ArrayList<String> chats_keys = new ArrayList<String>(0);

    public static class ChatListAdapter extends BaseAdapter {
        ArrayList<Chat> chats;
        Context context;
        private static LayoutInflater inflater=null;

        public ChatListAdapter(Message_Overview mainActivity, ArrayList<Chat> chats) {
            this.chats = chats;
            context=mainActivity;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return chats.size();
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
            // TODO Auto-generated method stub
            View rowView = inflater.inflate(R.layout.listitem_messages, null);

            Chat chat = chats.get(position);

            String fuid;
            if (chat.getParticipant1().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) fuid = chat.getParticipant2();
            else fuid = chat.getParticipant1();

            HashMap<String, String> message = chat.getLastMessage();
            int text_length = 25;
            String text = message.get("text");
            if(text.length() > text_length) {
                text = text.substring(0,text_length) + " ...";
            }
            text = text.replaceAll("\n"," ");
            String date_time = message.get("date") + "\n" + message.get("timestamp");

            ((TextView) rowView.findViewById(R.id.listitem_messages_date_time)).setText(date_time);

            final TextView t = (TextView) rowView.findViewById(R.id.listitem_messages_name_text);
            t.setText(text);

            mDatabase.child("users").child(fuid).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            String text = user.getFirstName() + " " + user.getLastName() + "\n" + t.getText().toString();
                            t.setText(text);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

            final ImageView i = (ImageView)rowView.findViewById(R.id.listitem_messages_image);

            mStorage.child("images/"+fuid+".jpg").getBytes(512*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    i.setImageBitmap(bm);
                }
            });

            return rowView;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__overview);

        mDatabase.child("chats").orderByChild("participant1").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()) {
                    Chat chat = d.getValue(Chat.class);
                    if (!chats_keys.contains(d.getKey())) {
                        chats.add(chat);
                        chats_keys.add(d.getKey());
                    } else if(chats_keys.contains(d.getKey())) {
                        Chat ref = chats.get(chats_keys.indexOf(d.getKey()));
                        if (!chat.getLastMessage().get("text").equals(ref.getLastMessage().get("text"))) {
                            chats.remove(chats_keys.indexOf(d.getKey()));
                            chats.add(chats_keys.indexOf(d.getKey()), chat);
                        }
                    }
                }

                ((ChatListAdapter)((ListView)findViewById(R.id.messages_list)).getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("chats").orderByChild("participant2").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()) {
                    Chat chat = d.getValue(Chat.class);
                    if (!chats_keys.contains(d.getKey())) {
                        chats.add(chat);
                        chats_keys.add(d.getKey());
                    } else if(chats_keys.contains(d.getKey())) {
                        Chat ref = chats.get(chats_keys.indexOf(d.getKey()));
                        if (!chat.getLastMessage().get("text").equals(ref.getLastMessage().get("text"))) {
                            chats.remove(chats_keys.indexOf(d.getKey()));
                            chats.add(chats_keys.indexOf(d.getKey()), chat);
                        }
                    }
                }

                ((ChatListAdapter)((ListView)findViewById(R.id.messages_list)).getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ((ListView) findViewById(R.id.messages_list)).setAdapter(new ChatListAdapter(this, chats));
        ((ListView) findViewById(R.id.messages_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fname = ((TextView)view.findViewById(R.id.listitem_messages_name_text)).getText().toString();
                fname = fname.substring(0,fname.indexOf("\n"));

                ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("messages_chat");

                if(!chats.get(position).getParticipant1().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    ((Message_Chat)((TabHost)getParent().findViewById(R.id.tabHost)).getCurrentView().getContext()).updateList(chats_keys.get(position), chats.get(position).getParticipant1(),fname);
                } else if (!chats.get(position).getParticipant2().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    ((Message_Chat)((TabHost)getParent().findViewById(R.id.tabHost)).getCurrentView().getContext()).updateList(chats_keys.get(position), chats.get(position).getParticipant2(),fname);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }
}*/

