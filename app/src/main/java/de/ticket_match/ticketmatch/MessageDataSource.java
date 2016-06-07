package de.ticket_match.ticketmatch;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MessageDataSource {
    private static final Firebase sRef = new Firebase("https://project-6427965941977376836.firebaseio.com/");
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddmmss");
    private static final String TAG = "MessageDataSource";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_SENDER = "sender";


    public static void saveMessage(Message message, String convoId){
        Date date = message.getDate();
        String key = sDateFormat.format(date);
        HashMap<String, String> msg = new HashMap<>();
        msg.put(COLUMN_TEXT, message.getText());
        msg.put(COLUMN_SENDER,"Ajay");
        //TODO: Change from convoId to messages and then convoId
        //sRef.child("messages").child(convoId).child(key).setValue(msg);
        sRef.child(convoId).child(key).setValue(msg);

        //sRef.child("messages").child(convoId).child(sDateFormat.format(message.getDate())).setValue(message);
    }

    public static MessagesListener addMessagesListener(String convoId, final MessagesCallbacks callbacks){
        MessagesListener listener = new MessagesListener(callbacks);
        //TODO: Change from convoId to messages and then convoId
        sRef.child(convoId).addChildEventListener(listener);
        return listener;

    }

    public static void stop(MessagesListener listener){
        sRef.removeEventListener(listener);
    }

    public static class MessagesListener implements ChildEventListener {
        private MessagesCallbacks callbacks;
        MessagesListener(MessagesCallbacks callbacks){
            this.callbacks = callbacks;
        }
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            /*HashMap<String,String> msg = (HashMap)dataSnapshot.getValue();
            Message message = new Message();
            message.setSender(msg.get(COLUMN_SENDER));
            message.setText(msg.get(COLUMN_TEXT));
            try {
                message.setDate(sDateFormat.parse(dataSnapshot.getKey()));
            }catch (Exception e){
                Log.d(TAG, "Couldn't parse date"+e);
            }
            if(callbacks != null){
                callbacks.onMessageAdded(message);
            }*/
            Message rMessage = dataSnapshot.getValue(Message.class);
            try {
                rMessage.setDate(sDateFormat.parse(dataSnapshot.getKey()));
            }catch (Exception e){
                Log.d(TAG, "Couldn't parse date"+e);
            }
            if(callbacks != null){
                callbacks.onMessageAdded(rMessage);
            }

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
        public void onCancelled(FirebaseError firebaseError) {

        }
    }


    public interface MessagesCallbacks{
        public void onMessageAdded(Message message);
    }
}
