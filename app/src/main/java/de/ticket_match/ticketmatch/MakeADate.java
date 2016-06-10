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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MakeADate extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<MakeDate> dates = new ArrayList<MakeDate>(0);
    private ArrayList<String> dates_keys = new ArrayList<String>(0);

    public static class DateListAdapter extends BaseAdapter {
        ArrayList<MakeDate> dates;
        ArrayList<String> dates_keys;
        Context context;
        private static LayoutInflater inflater=null;

        public DateListAdapter(MakeADate mainActivity, ArrayList<MakeDate>  dates, ArrayList<String> dates_keys) {
            this.dates = dates;
            this.dates_keys = dates_keys;

            context=mainActivity;
            inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return dates.size();
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
            MakeDate date = dates.get(position);
            String key = dates_keys.get(position);

            View rowView = inflater.inflate(R.layout.listitem_makeadate, null);
            String gender = "";

            if(date.isWithman()){
                gender = "man";
            }else if(date.isWithwoman()){
                gender = "woman";
            }else {
                gender = "man | woman";
            }

            ((TextView) rowView.findViewById(R.id.makeadate_row_name)).setText(date.getName() + "\n" + date.getType());
            ((TextView)rowView.findViewById(R.id.makeadate_row_date)).setText(date.getDate() + "\n" + date.getLocation());
            ((TextView) rowView.findViewById(R.id.makeadate_row_who)).setText(date.getTime() + "\n" + gender);
            ((TextView) rowView.findViewById(R.id.row_key)).setText(key);
            return rowView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_adate);

        ListView listview = (ListView) findViewById(R.id.listview_makeadate);

        listview.setAdapter(new DateListAdapter(this, dates, dates_keys));

        mDatabase.child("makedates").orderByChild("user").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()) {
                    MakeDate date = d.getValue(MakeDate.class);
                    dates.add(date);
                    dates_keys.add((String)d.getKey());
                }
                ((DateListAdapter)(((ListView)findViewById(R.id.listview_makeadate)).getAdapter())).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void btn_find_makeadate(View view){
        ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("makeadate_search");

    }

    public void btn_save_makeadate(View view){
        ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("makeadate_new");
    }

    public void btn_date_delete (View view) {
        String date_key = ((TextView)((View)view.getParent()).findViewById(R.id.row_key)).getText().toString();
        int date_position = dates_keys.indexOf(date_key);
        dates_keys.remove(date_position);
        dates.remove(date_position);
        ((DateListAdapter)((ListView) findViewById(R.id.listview_makeadate)).getAdapter()).notifyDataSetChanged();
        mDatabase.child("makedates").child(date_key).removeValue();
    }
}
