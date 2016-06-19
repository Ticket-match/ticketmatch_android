package de.ticket_match.ticketmatch;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import de.ticket_match.ticketmatch.entities.Chat;
import de.ticket_match.ticketmatch.entities.User;

public class MessageNotifications extends Service {
    SharedPreferences settings;
    private static MessageNotifications instance = null;
    private static boolean check = false;

    public static MessageNotifications getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Gets extra from the incoming Intent
        settings = getSharedPreferences("TicketMatch", 0);
        final String dataString = settings.getString("UID","");

        if (!dataString.equals("")) {
            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase.child("users").child(dataString).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    final String name = user.getFirstName() + " " + user.getLastName();

                    mDatabase.child("chats").orderByChild("participant1").equalTo(dataString).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            if (check) {
                                Chat c = dataSnapshot.getValue(Chat.class);
                                HashMap<String, String> hm = c.getLastMessage();
                                if (!hm.get("author").equals(name)) {
                                    int text_length = 20;
                                    String text = hm.get("text");
                                    if (text.length() > text_length) {
                                        text = text.substring(0, text_length) + "...";
                                    }
                                    NotificationCompat.Builder mBuilder =
                                            new NotificationCompat.Builder(getApplicationContext())
                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                    .setColor(getResources().getColor(R.color.colorAccent))
                                                    .setContentTitle("From "+hm.get("author"))
                                                    .setContentText(text);

                                    NotificationManager mNotificationManager =
                                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    mNotificationManager.notify(0, mBuilder.build());
                                }
                                check = false;
                            } else {
                                check = true;
                            }
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

                    mDatabase.child("chats").orderByChild("participant2").equalTo(dataString).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            if (check) {
                                Chat c = dataSnapshot.getValue(Chat.class);
                                HashMap<String, String> hm = c.getLastMessage();
                                if (!hm.get("author").equals(name)) {
                                    int text_length = 25;
                                    String text = hm.get("text");
                                    if (text.length() > text_length) {
                                        text = text.substring(0, text_length) + " ...";
                                    }
                                    NotificationCompat.Builder mBuilder =
                                            new NotificationCompat.Builder(getApplicationContext())
                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                    .setColor(getResources().getColor(R.color.bgColor))
                                                    .setContentTitle(hm.get("author"))
                                                    .setContentText(text);

                                    NotificationManager mNotificationManager =
                                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    mNotificationManager.notify(0, mBuilder.build());
                                }
                                check = false;
                            } else {
                                check = true;
                            }
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
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
