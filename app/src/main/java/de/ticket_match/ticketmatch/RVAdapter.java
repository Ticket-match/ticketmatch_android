package de.ticket_match.ticketmatch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.List;

import de.ticket_match.ticketmatch.entities.Chat;
import de.ticket_match.ticketmatch.entities.User;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ChatViewHolder>{

    List<Chat> chats;
    List<String> chat_keys;
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    Activity prevActivity;

    //added context
    RVAdapter(List<Chat> chats, final Activity prevActivity, final List<String> chat_keys){
        this.chats = chats;
        this.prevActivity = prevActivity;
        this.chat_keys = chat_keys;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nametext;
        TextView datetime;
        ImageView pic;

        ChatViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            nametext = (TextView)itemView.findViewById(R.id.listitem_messages_name_text);
            datetime = (TextView)itemView.findViewById(R.id.listitem_messages_date_time);
            pic = (ImageView)itemView.findViewById(R.id.listitem_messages_image);
        }
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewitem_messages, parent, false);
        ChatViewHolder cvh = new ChatViewHolder(v);
        return cvh;
    }

    @Override
    //Changed holder and position to final
    public void onBindViewHolder(final ChatViewHolder holder, final int position) {
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat chat = chats.get(position);
                String fname = (((TextView)v.findViewById(R.id.listitem_messages_name_text))).getText().toString();
                fname = fname.substring(0,fname.indexOf("\n"));

                ((TabHost)prevActivity.getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("messages_chat");

                if(!chats.get(position).getParticipant1().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    ((Message_Chat)((TabHost)prevActivity.getParent().findViewById(R.id.tabHost)).getCurrentView().getContext()).updateList(chat_keys.get(position), chats.get(position).getParticipant1(),fname);
                } else if (!chats.get(position).getParticipant2().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    ((Message_Chat)((TabHost)prevActivity.getParent().findViewById(R.id.tabHost)).getCurrentView().getContext()).updateList(chat_keys.get(position), chats.get(position).getParticipant2(),fname);
                }
            }
        });

        Chat chat = chats.get(position);

        String fuid;
        if (chat.getParticipant1().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            fuid = chat.getParticipant2();
        } else {
            fuid = chat.getParticipant1();
        }

        HashMap<String, String> message = chat.getLastMessage();
        int text_length = 20;
        String text = message.get("text");
        if(text.length() > text_length) {
            text = text.substring(0,text_length) + "...";
        }
        text = text.replaceAll("\n"," ");
        String date_time = message.get("date") + "\n" + message.get("timestamp");

        holder.datetime.setText(date_time);

        holder.nametext.setText(text);
        //mDatabase.child("users").child(fuid).addListenerForSingleValueEvent(
        mDatabase.child("users").child(fuid).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        String text = user.getFirstName() + " " + user.getLastName() + "\n" + holder.nametext.getText().toString();
                        holder.nametext.setText(text);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        final ImageView i = holder.pic;

        mStorage.child("images/"+fuid+".jpg").getBytes(512*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                i.setImageBitmap(bm);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}