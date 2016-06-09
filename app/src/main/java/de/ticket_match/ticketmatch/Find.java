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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Find extends AppCompatActivity {

    ArrayList<Ticket> listitems_find;

    ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        listview = (ListView) findViewById(R.id.find_results);
        //ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.find_headerlayout, listview, false);
        //listview.addHeaderView(header);

        listitems_find = (ArrayList<Ticket>)((MainActivityTabHost) getParent()).baseBundle.getSerializable("tickets_search_result");
        listview.setAdapter(new CustomAdapter(this, listitems_find));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Ticket text = listitems_find.get(position);

                String message = "Hello, " + "\n" + "I am interested in your tickets: " + "\n" + text.getName() + "\n" + text.getType() + "\n" + text.getDate() + " | " + text.getTime() + "\n" + text.getQuantity() + " pc. | " + text.getPrice().get("value") + " " + text.getPrice().get("currency");

                ((MainActivityTabHost) getParent()).baseBundle.putString("tickets_search_message", message);

                //Intent find_vendor = new Intent(getApplicationContext(), Find_Vendor.class);
                //find_vendor.putExtras(values);
                //startActivity(find_vendor);
                ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("search_vendor_message");
            }
        });
    }

    public static class CustomAdapter extends BaseAdapter {
        ArrayList<Ticket> result;
        Context context;
        private static LayoutInflater inflater=null;

        public CustomAdapter(Find mainActivity, ArrayList<Ticket>  findlist) {
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

            Ticket text = result.get(position);

            View rowView = inflater.inflate(R.layout.listitem_find, null);

            ((TextView) rowView.findViewById(R.id.row_type)).setText(text.getName() + "\n" + text.getType());
            ((TextView) rowView.findViewById(R.id.row_date)).setText(text.getDate() + "\n" + text.getLocation());
            ((TextView) rowView.findViewById(R.id.row_time)).setText(text.getTime() + "\n" + text.getQuantity() + "pc. | " + text.getPrice().get("value") + " " + text.getPrice().get("currency"));
            return rowView;
        }
    }
}
