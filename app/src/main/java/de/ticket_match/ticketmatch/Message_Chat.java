package de.ticket_match.ticketmatch;

import android.content.Context;
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

        Bundle bund = getIntent().getExtras();
        name = bund.getString("message_name");

        ((TextView) findViewById(R.id.message_chat_name)).setText(name);

        chatmessages.add("me|25.12.2016|Hello Nils!");
        chatmessages.add("Nils Z.|26.01.2017|Hello Fred!");

        ((ListView) findViewById(R.id.message_chat)).setAdapter(new CustomAdapter(this, chatmessages, name));

    }

    public void btn_tm_logo(View view) {

        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.change_password:
                        //ChangePasswordDialog cdp = new ChangePasswordDialog();
                        //cdp.show(getFragmentManager(), "cdp");
                        Intent changepassword =  new Intent(getApplicationContext(), ChangePassword.class);
                        startActivity(changepassword);
                        return true;
                    case R.id.logout:
                        Toast.makeText(getApplicationContext(),"logout",Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();

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

    public void btn_profile(View view) {
        Intent myprofile = new Intent(this, MyProfile.class);
        startActivity(myprofile);
    }

    public void btn_message(View view) {
        Intent message_overview = new Intent(this, Message_Overview.class);
        startActivity(message_overview);
    }

    public void btn_ticketoffer(View view) {
        Toast.makeText(getApplicationContext(),"btn_ticketoffer",Toast.LENGTH_SHORT).show();
    }

    public void btn_search(View view) {
        Toast.makeText(getApplicationContext(),"btn_search",Toast.LENGTH_SHORT).show();
        Intent inte = new Intent(this, ForeignProfile.class);
        startActivity(inte);
    }

    public void btn_makematch(View view) {
        Toast.makeText(getApplicationContext(),"btn_makematch",Toast.LENGTH_SHORT).show();
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
}
