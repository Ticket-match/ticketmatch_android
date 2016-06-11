package de.ticket_match.ticketmatch;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import de.ticket_match.ticketmatch.entities.Message;

public class Message_Chat extends AppCompatActivity implements View.OnClickListener, MessageDataSource.MessagesCallbacks{

    public static final String USER_EXTRA = "USER";

    public static final String TAG = "ChatActivity";

    private ArrayList<Message> mMessages;
    private MessagesAdapter mAdapter;
    private String mRecipient;
    private ListView mListView;
    private Date mLastMessageDate = new Date();
    private String mConvoId;
    private MessageDataSource.MessagesListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__chat);

        mRecipient = "Ashok";

        mListView = (ListView)findViewById(R.id.message_chat);
        mMessages = new ArrayList<>();
        mAdapter = new MessagesAdapter(mMessages);
        mListView.setAdapter(mAdapter);

        //TODO: Set title of chat name
        /*setTitle(mRecipient);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/

        Button sendMessage = (Button)findViewById(R.id.send_message);
        sendMessage.setOnClickListener(this);

        String[] ids = {"Ajay","-", "Ashok"};
        Arrays.sort(ids);
        mConvoId = ids[0]+ids[1]+ids[2];


        //TODO: Change from convoId to messages and then convoId
        mListener = MessageDataSource.addMessagesListener(mConvoId, this);
    }

    public void onClick(View v) {
        EditText newMessageView = (EditText)findViewById(R.id.new_chat_message);
        String newMessage = newMessageView.getText().toString();
        newMessageView.setText("");
        Message msg = new Message();
        msg.setDate(new Date());
        msg.setText(newMessage);
        msg.setSender("Ashok");

        MessageDataSource.saveMessage(msg, mConvoId);
    }

    @Override
    public void onMessageAdded(Message message) {
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageDataSource.stop(mListener);
    }


    private class MessagesAdapter extends ArrayAdapter<Message> {
        MessagesAdapter(ArrayList<Message> messages){
            super(Message_Chat.this, R.layout.message, R.id.message, messages);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            Message message = getItem(position);

            TextView nameView = (TextView)convertView.findViewById(R.id.message);
            nameView.setText(message.getText());

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)nameView.getLayoutParams();

            int sdk = Build.VERSION.SDK_INT;
            if (message.getSender().equals("Ashok")){
                if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                    nameView.setBackground(getDrawable(R.drawable.bubble_right_green));
                } else{
                    nameView.setBackgroundDrawable(getDrawable(R.drawable.bubble_right_green));
                }
                layoutParams.gravity = Gravity.RIGHT;
                //nameView.setGravity(Gravity.RIGHT);

            }else{
                if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                    nameView.setBackground(getDrawable(R.drawable.bubble_left_gray));
                } else{
                    nameView.setBackgroundDrawable(getDrawable(R.drawable.bubble_left_gray));
                }
                layoutParams.gravity = Gravity.LEFT;
                //nameView.setGravity(Gravity.LEFT);
            }

            nameView.setLayoutParams(layoutParams);


            return convertView;
        }
    }
}

/*import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Message_Chat extends AppCompatActivity {



    ArrayList<String> chatmessages = new ArrayList<String>(0);
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__chat);

        Bundle bund = ((MainActivityTabHost)getParent()).baseBundle;
        name = bund.getString("message_name");

        ((TextView) findViewById(R.id.message_chat_name)).setText(name);

        chatmessages.add("me|25.12.2016|Hello Nils!");
        chatmessages.add("Nils Z.|26.01.2017|Hello Fred!");

        ((ListView) findViewById(R.id.message_chat)).setAdapter(new CustomAdapter(this, chatmessages, name));

    }

    public void btn_sendMessage (View view) {
        String message_text = ((EditText)findViewById(R.id.new_chat_message)).getText().toString();
        if (message_text.equals("")) {
            Toast.makeText(getApplicationContext(),"Please fill out the requiered information!",Toast.LENGTH_SHORT).show();
        } else {
            String chat = "me|" + new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + "|" + message_text;
            chatmessages.add(chat);
            ((CustomAdapter)((ListView) findViewById(R.id.message_chat)).getAdapter()).notifyDataSetChanged();
            ((EditText)findViewById(R.id.new_chat_message)).setText("");
        }
    }

    public static class CustomAdapter extends BaseAdapter {
        ArrayList<String> result;
        String sname;
        Context context;
        private static LayoutInflater inflater=null;

        public CustomAdapter(Message_Chat mainActivity, ArrayList<String>  prgmNameList, String name) {
            result=prgmNameList;
            sname = name;
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
            String text = result.get(position);
            String sender = text.substring(0, text.indexOf("|"));
            String sdate = text.substring(text.indexOf("|")+1,text.indexOf("|",text.indexOf("|")+1));
            String smessage = text.substring(text.indexOf("|",text.indexOf("|")+1)+1,text.length());

            View rowView = null;

            if (sender.equals("me")) {
                 rowView = inflater.inflate(R.layout.listitem_message_chat_right, null);
                ((TextView) rowView.findViewById(R.id.listitem_messagechat)).setText(smessage);
                ((TextView) rowView.findViewById(R.id.listitem_messagechat_date)).setText(sdate);
            } else if (sender.equals(sname)) {
                 rowView = inflater.inflate(R.layout.listitem_message_chat_left, null);
                ((TextView) rowView.findViewById(R.id.listitem_messagechat)).setText(smessage);
                ((TextView) rowView.findViewById(R.id.listitem_messagechat_date)).setText(sdate);
            }

            return rowView;
        }

    }
}*/
