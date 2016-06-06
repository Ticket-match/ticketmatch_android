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
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MakeADate_SearchResults extends AppCompatActivity {

    ListView listview;
    ArrayList<String> listitems_makeadate_results= new ArrayList<String>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_adate__search_results);

        listview = (ListView) findViewById(R.id.listview_makeadate_results);
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.makeadate_results_headerlayout, listview, false);
        listview.addHeaderView(header);

        listitems_makeadate_results.add("Max Müller|25");
        listitems_makeadate_results.add("Klaus Kleber|55");
        listitems_makeadate_results.add("Lisa Lustig|35");
        listview.setAdapter(new CustomAdapter(this, listitems_makeadate_results));



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String makeadate_searchresults = listitems_makeadate_results.get(position-1);

                //Intent makeadateresults_message = new Intent(getApplicationContext(), MakeADateResults_Message.class);
                //startActivity(makeadateresults_message);
                ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("makeadate_search_result_message");
            }
        });

    }

    public static class CustomAdapter extends BaseAdapter {
        ArrayList<String> result;
        Context context;
        private static LayoutInflater inflater=null;

        public CustomAdapter(MakeADate_SearchResults mainActivity, ArrayList<String>  makeadate_results) {
            result=makeadate_results;
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
            String name = text.substring(0, text.indexOf("|"));
            String age = text.substring(text.indexOf("|")+1,text.length());


            View rowView = inflater.inflate(R.layout.listitem_makeadate_results, null);

            ((TextView) rowView.findViewById(R.id.makeadate_results_row_name)).setText(name);
            ((TextView) rowView.findViewById(R.id.makeadate_results_row_age)).setText(age);
            return rowView;
        }

    }

    /*
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
        Intent makeadate = new Intent(this, MakeADate.class);
        startActivity(makeadate);
    }
    */

}
