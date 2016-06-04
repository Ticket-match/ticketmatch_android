package de.ticket_match.ticketmatch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MakeADate extends AppCompatActivity {

    ListView listview;

    ArrayList<String> listitems_makeadate= new ArrayList<String>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_adate);

        listview = (ListView) findViewById(R.id.listview_makeadate);
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.makeadate_headerlayout, listview, false);
        listview.addHeaderView(header);

        listitems_makeadate.add("Cinema Star Wars 7|Star Wars Late Night Film|2");
        listitems_makeadate.add("Cinema&Picnic|Late Night|5");
        listview.setAdapter(new CustomAdapter(this, listitems_makeadate));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String find_ticket = listitems_makeadate.get(position-1);

                Intent makeadate_detail = new Intent(getApplicationContext(), MakeADate_Detail.class);
                startActivity(makeadate_detail);
            }
        });


    }

    public void btn_find_makeadate(View view){

        Intent search_makeadate = new Intent(getApplicationContext(), Search_MakeADate.class);
        startActivity(search_makeadate);

    }

    public void btn_save_makeadate(View view){

        Intent newmakeadate = new Intent(getApplicationContext(), New_MakeAdate.class);
        startActivity(newmakeadate);
    }

    public static class CustomAdapter extends BaseAdapter {
        ArrayList<String> result;
        Context context;
        private static LayoutInflater inflater=null;

        public CustomAdapter(MakeADate mainActivity, ArrayList<String>  makeadatelist) {
            result=makeadatelist;
            context=mainActivity;
            inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            String eventname = text.substring(0, text.indexOf("|"));
            String date = text.substring(text.indexOf("|")+1,text.indexOf("|",text.indexOf("|")+1));
            String who = text.substring(text.indexOf("|",text.indexOf("|")+1)+1,text.length());

            View rowView = inflater.inflate(R.layout.listitem_makeadate, null);

            ((TextView) rowView.findViewById(R.id.makeadate_row_name)).setText(eventname);
            ((TextView)rowView.findViewById(R.id.makeadate_row_date)).setText(date);
            ((TextView) rowView.findViewById(R.id.makeadate_row_who)).setText(who);
            return rowView;
        }

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
        Intent message = new Intent(this, Message_Overview.class);
        startActivity(message);
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

    }
}
