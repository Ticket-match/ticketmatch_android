package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Find extends AppCompatActivity {

    ArrayList<String> listitems_find= new ArrayList<String>(0);

    ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        listview = (ListView) findViewById(R.id.find_results);
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.find_headerlayout, listview, false);
        listview.addHeaderView(header);


    }

    public void btn_find (View view){

        listitems_find.removeAll(listitems_find);

        View keyboard = this.getCurrentFocus();
        if (keyboard != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(keyboard.getWindowToken(), 0);
        }

        TextView date = (TextView)findViewById(R.id.search_date);
        EditText location = (EditText)findViewById(R.id.find_eventlocation);

        String dates = date.getText().toString();
        String locations = location.getText().toString();

        if (dates.equals("Date") | locations.equals("")){
            Toast.makeText(getApplicationContext(),"Please fill in all information!",Toast.LENGTH_SHORT).show();
        }else {

            listitems_find.add("Cinema|Star Wars|2");
            listitems_find.add("Cinema&Picnic|Late Night|5");
            listview.setAdapter(new CustomAdapter(this, listitems_find));

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String find_ticket = listitems_find.get(position-1);

                    Bundle values = new Bundle();
                    values.putString("find", find_ticket);
                    Intent find_vendor = new Intent(getApplicationContext(), Find_Vendor.class);
                    find_vendor.putExtras(values);
                    startActivity(find_vendor);
                }
            });
        }
    }

    public static class CustomAdapter extends BaseAdapter {
        ArrayList<String> result;
        Context context;
        private static LayoutInflater inflater=null;

        public CustomAdapter(Find mainActivity, ArrayList<String>  findlist) {
            result=findlist;
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
            String type = text.substring(0, text.indexOf("|"));
            String eventname = text.substring(text.indexOf("|")+1,text.indexOf("|",text.indexOf("|")+1));
            String count = text.substring(text.indexOf("|",text.indexOf("|")+1)+1,text.length());

            View rowView = inflater.inflate(R.layout.listitem_find, null);

            ((TextView) rowView.findViewById(R.id.row_type)).setText(type);
            ((TextView) rowView.findViewById(R.id.row_date)).setText(eventname);
            ((TextView) rowView.findViewById(R.id.row_time)).setText(count);
            return rowView;
        }

    }

    public void find_date(View view) {
        Find.NewFindDate rbd = new Find.NewFindDate();
        rbd.show(getFragmentManager(), "rbd");
    }

    public static class NewFindDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //Use the current date as the default date in the date picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            String date = day + "." + month + "." + year;
            ((TextView)getActivity().findViewById(R.id.search_date)).setText(date);
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

    }

    public void btn_makematch(View view) {
        Intent makeadate = new Intent(this, MakeADate.class);
        startActivity(makeadate);
    }
}
