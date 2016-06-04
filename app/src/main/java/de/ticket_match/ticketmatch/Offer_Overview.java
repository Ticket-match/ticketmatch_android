package de.ticket_match.ticketmatch;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

public class Offer_Overview extends AppCompatActivity {

    ArrayList<String> listitems_offer = new ArrayList<String>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer__overview);

        ListView listview = (ListView) findViewById(R.id.offeroverview_list);

        ViewGroup header = (ViewGroup)getLayoutInflater().inflate(R.layout.offeroverview_headerlayout, listview, false);
        listview.addHeaderView(header);
//|15:00|14,00€|Mannheim
        listitems_offer.add("Cinema|Star Wars|25.05.2014|15:00");
        listitems_offer.add("Cinema&Picnic|12.05.2013|20:15");
        listview.setAdapter(new CustomAdapter(this, listitems_offer));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String offer_ticket = listitems_offer.get(position-1);

                Bundle values = new Bundle();
                values.putString("offer", offer_ticket);
                Intent offer_detailscreen = new Intent(getApplicationContext(), Offer_Detail.class);
                offer_detailscreen.putExtras(values);
                startActivity(offer_detailscreen);
            }
        });
    }

    public void btn_newoffer (View view){
        Intent newoffer = new Intent(this, NewOffer.class);
        startActivity(newoffer);

    }

    public static class CustomAdapter extends BaseAdapter {
        ArrayList<String> result;
        Context context;
        private static LayoutInflater inflater=null;

        public CustomAdapter(Offer_Overview mainActivity, ArrayList<String>  offerlist) {
            result=offerlist;
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

            String text = result.get(position);
            String type = text.substring(0, text.indexOf("|"));
            String date = text.substring(text.indexOf("|")+1,text.indexOf("|",text.indexOf("|")+1));
            String time = text.substring(text.indexOf("|",text.indexOf("|")+1)+1,text.length());

            View rowView = inflater.inflate(R.layout.listitem_offeroverview, null);

            ((TextView) rowView.findViewById(R.id.row_type)).setText(type);
            ((TextView) rowView.findViewById(R.id.row_date)).setText(date);
            ((TextView) rowView.findViewById(R.id.row_time)).setText(time);
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



    public void btn_search(View view) {
        Intent find = new Intent(this, Find.class);
        startActivity(find);
    }

    public void btn_makematch(View view) {
        Intent makeadate = new Intent(this, MakeADate.class);
        startActivity(makeadate);
    }
}
