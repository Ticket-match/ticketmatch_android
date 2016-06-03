package de.ticket_match.ticketmatch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Message_Overview extends AppCompatActivity {

    ArrayList<Bitmap> images = new ArrayList<Bitmap>(0);
    ArrayList<String> result = new ArrayList<String>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__overview);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.contacts);

        result.add("Nils Z.|Pick Nick am Neckar|12.05.2016");
        images.add(bm);
        ((ListView) findViewById(R.id.messages_list)).setAdapter(new CustomAdapter(this, images, result));
        ((ListView) findViewById(R.id.messages_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String message_name = result.get(position);
                message_name = message_name.substring(0, message_name.indexOf("|"));

                Bundle bund= new Bundle();
                bund.putString("message_name", message_name);
                Intent message_chat = new Intent(getApplicationContext(), Message_Chat.class);
                message_chat.putExtras(bund);
                startActivity(message_chat);
            }
        });

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

    public void btn_profile(View view) {
        Intent myprofile = new Intent(this, MyProfile.class);
        startActivity(myprofile);
    }

    public void btn_message(View view) {

    }

    public void btn_ticketoffer(View view) {
        Intent offeroverview = new Intent(this, Offer_Overview.class);
        startActivity(offeroverview);
    }

    public void btn_search(View view) {
        Intent find = new Intent(this, Find.class);
        startActivity(find);
    }

    public void btn_makematch(View view) {
        Intent makeadate = new Intent(this, MakeADate.class);
        startActivity(makeadate);
    }

    public static class CustomAdapter extends BaseAdapter {
        ArrayList<Bitmap> images;
        ArrayList<String> result;

        Context context;
        private static LayoutInflater inflater=null;

        public CustomAdapter(Message_Overview mainActivity, ArrayList<Bitmap> images, ArrayList<String>  prgmNameList) {
            this.images=images;
            result=prgmNameList;
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
            // TODO Auto-generated method stub
            View rowView = inflater.inflate(R.layout.listitem_messages, null);
            String text = result.get(position);
            String date;
            String name;
            name = text.substring(0, text.indexOf("|")) + "\n" + text.substring(text.indexOf("|")+1,text.indexOf("|",text.indexOf("|")+1));
            date = text.substring(text.indexOf("|",text.indexOf("|")+1)+1,text.length());
            ((ImageView) rowView.findViewById(R.id.listitem_message_image)).setImageBitmap(images.get(position));
            ((TextView) rowView.findViewById(R.id.listitem_messages_name_subject)).setText(name);
            ((TextView) rowView.findViewById(R.id.listitem_messages_date)).setText(date);
            return rowView;
        }

    }
}
