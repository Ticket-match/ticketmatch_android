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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
                        if (!chat.getLastMessage().get("text").equals(ref.getLastMessage().get("text")) || !chat.getLastMessage().get("timestamp").equals(ref.getLastMessage().get("timestamp")) || !chat.getLastMessage().get("date").equals(ref.getLastMessage().get("date"))) {
                            chats.remove(chats_keys.indexOf(d.getKey()));
                            chats.add(chats_keys.indexOf(d.getKey()), chat);
                        }
                    }
                }

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                Date dl,dr;

                for (int n = 0; n < chats.size(); n++) {
                    for (int m = 0; m < (chats.size()-1) - n; m++) {
                        try {
                            dl = dateFormat.parse(chats.get(m).getLastMessage().get("date"));
                            dr = dateFormat.parse(chats.get(m+1).getLastMessage().get("date"));
                            int i = dl.compareTo(dr);

                            if (i<0) {
                                Chat swapChat = chats.get(m);
                                chats.set(m,chats.get(m+1));
                                chats.set(m+1,swapChat);

                                String swapKey = chats_keys.get(m);
                                chats_keys.set(m,chats_keys.get(m+1));
                                chats_keys.set(m+1,swapKey);
                            } else if (i==0) {
                                dl = timeFormat.parse(chats.get(m).getLastMessage().get("timestamp"));
                                dr = timeFormat.parse(chats.get(m+1).getLastMessage().get("timestamp"));
                                i = dl.compareTo(dr);

                                if (i<0) {
                                    Chat swapChat = chats.get(m);
                                    chats.set(m,chats.get(m+1));
                                    chats.set(m+1,swapChat);

                                    String swapKey = chats_keys.get(m);
                                    chats_keys.set(m,chats_keys.get(m+1));
                                    chats_keys.set(m+1,swapKey);
                                }
                            }
                        } catch (Exception e) {
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
                        if (!chat.getLastMessage().get("text").equals(ref.getLastMessage().get("text")) || !chat.getLastMessage().get("timestamp").equals(ref.getLastMessage().get("timestamp")) || !chat.getLastMessage().get("date").equals(ref.getLastMessage().get("date"))) {
                            chats.remove(chats_keys.indexOf(d.getKey()));
                            chats.add(chats_keys.indexOf(d.getKey()), chat);
                        }
                    }
                }

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                Date dl,dr;

                for (int n = 0; n < chats.size(); n++) {
                    for (int m = 0; m < (chats.size()-1) - n; m++) {
                        try {
                            dl = dateFormat.parse(chats.get(m).getLastMessage().get("date"));
                            dr = dateFormat.parse(chats.get(m+1).getLastMessage().get("date"));
                            int i = dl.compareTo(dr);

                            if (i<0) {
                                Chat swapChat = chats.get(m);
                                chats.set(m,chats.get(m+1));
                                chats.set(m+1,swapChat);

                                String swapKey = chats_keys.get(m);
                                chats_keys.set(m,chats_keys.get(m+1));
                                chats_keys.set(m+1,swapKey);
                            } else if (i==0) {
                                dl = timeFormat.parse(chats.get(m).getLastMessage().get("timestamp"));
                                dr = timeFormat.parse(chats.get(m+1).getLastMessage().get("timestamp"));
                                i = dl.compareTo(dr);

                                if (i<0) {
                                    Chat swapChat = chats.get(m);
                                    chats.set(m,chats.get(m+1));
                                    chats.set(m+1,swapChat);

                                    String swapKey = chats_keys.get(m);
                                    chats_keys.set(m,chats_keys.get(m+1));
                                    chats_keys.set(m+1,swapKey);
                                }
                            }
                        } catch (Exception e) {
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
