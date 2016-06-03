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

public class Find_Vendor extends AppCompatActivity {

    Bundle bund;
    String find_vendor = "";
    ArrayList<String> listitems_vendor= new ArrayList<String>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find__vendor);

        bund = getIntent().getExtras();
        find_vendor = bund.getString("find");

        ListView listview = (ListView)findViewById(R.id.find_vendor);
        listitems_vendor.add("Max Mustermann|3|12,00 EUR");
        listitems_vendor.add("Klaus Kleber|15|3 EUR");

        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.vendor_headerlayout, listview, false);
        listview.addHeaderView(header);

        listview.setAdapter(new CustomAdapter(this, listitems_vendor));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String vendor = listitems_vendor.get(position-1);

                Bundle values = new Bundle();
                values.putString("vendor", vendor);
                Intent message_vendor = new Intent(getApplicationContext(), Message_Vendor.class);
                message_vendor.putExtras(values);
                startActivity(message_vendor);
            }
        });

    }

    public static class CustomAdapter extends BaseAdapter {
        ArrayList<String> result;
        Context context;
        private static LayoutInflater inflater=null;

        public CustomAdapter(Find_Vendor mainActivity, ArrayList<String>  vendorlist) {
            result=vendorlist;
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
            String vendorname = text.substring(0, text.indexOf("|"));
            String count = text.substring(text.indexOf("|")+1,text.indexOf("|",text.indexOf("|")+1));
            String price = text.substring(text.indexOf("|",text.indexOf("|")+1)+1,text.length());

            View rowView = inflater.inflate(R.layout.listitem_vendor, null);

            ((TextView) rowView.findViewById(R.id.row_name)).setText(vendorname);
            ((TextView) rowView.findViewById(R.id.row_count)).setText(count);
            ((TextView) rowView.findViewById(R.id.row_price)).setText(price);
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
        Intent search = new Intent(this, Find.class);
        startActivity(search);
    }

    public void btn_makematch(View view) {
        Toast.makeText(getApplicationContext(),"btn_makematch",Toast.LENGTH_SHORT).show();
    }
}
