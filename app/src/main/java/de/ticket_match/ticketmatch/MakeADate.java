package de.ticket_match.ticketmatch;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.ticket_match.ticketmatch.entities.MakeDate;

public class MakeADate extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<MakeDate> dates = new ArrayList<MakeDate>(0);
    private ArrayList<String> dates_keys = new ArrayList<String>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_adate);

        mDatabase.child("makedates").orderByChild("user").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()) {
                    MakeDate date = d.getValue(MakeDate.class);
                    dates.add(date);
                    dates_keys.add((String)d.getKey());
                }

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                Date dl,dr;

                for (int n = 0; n < dates.size(); n++) {
                    for (int m = 0; m < (dates.size()-1) - n; m++) {
                        try {
                            dl = dateFormat.parse(dates.get(m).getDate());
                            dr = dateFormat.parse(dates.get(m+1).getDate());
                            int i = dl.compareTo(dr);

                            if (i<0) {
                                MakeDate swapDate = dates.get(m);
                                dates.set(m,dates.get(m+1));
                                dates.set(m+1,swapDate);

                                String swapKey = dates_keys.get(m);
                                dates_keys.set(m,dates_keys.get(m+1));
                                dates_keys.set(m+1,swapKey);
                            } else if (i==0) {
                                dl = timeFormat.parse(dates.get(m).getTime());
                                dr = timeFormat.parse(dates.get(m+1).getTime());
                                i = dl.compareTo(dr);

                                if (i<0) {
                                    MakeDate swapDate = dates.get(m);
                                    dates.set(m,dates.get(m+1));
                                    dates.set(m+1,swapDate);

                                    String swapKey = dates_keys.get(m);
                                    dates_keys.set(m,dates_keys.get(m+1));
                                    dates_keys.set(m+1,swapKey);
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }

                ((((RecyclerView)findViewById(R.id.makedate_overview)).getAdapter())).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final RecyclerView rv = (RecyclerView) findViewById(R.id.makedate_overview);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        final RVAdapter_MakeadateOverview adapter = new RVAdapter_MakeadateOverview(dates, dates_keys);
        rv.setAdapter(adapter);

        ((MainActivityTabHost) getParent()).baseBundle.putSerializable("makeadate_list", dates);
        ((MainActivityTabHost) getParent()).baseBundle.putSerializable("makeadate_list_keys", dates_keys);
    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    public void btn_find_makeadate(View view){
        ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("makeadate_search");

    }

    public void btn_save_makeadate(View view){
        ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("makeadate_new");
    }

    public void btn_date_delete (View view) {
        String date_key = ((TextView)((View)view.getParent()).findViewById(R.id.makeadate_row_key)).getText().toString();
        int date_position = dates_keys.indexOf(date_key);
        dates_keys.remove(date_position);
        dates.remove(date_position);
        (((RecyclerView) findViewById(R.id.makedate_overview)).getAdapter()).notifyDataSetChanged();
        mDatabase.child("makedates").child(date_key).removeValue();
    }
}

/*public class MakeADate extends AppCompatActivity {

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

            if(date.isWithman().equals("true") & date.isWithwoman().equals("false")){
                gender = "man";
            }else if(date.isWithwoman().equals("true") & date.isWithman().equals("false")){
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
        ((MainActivityTabHost) getParent()).baseBundle.putSerializable("makeadate_list", dates);
        ((MainActivityTabHost) getParent()).baseBundle.putSerializable("makeadate_list_keys", dates_keys);


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

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
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
}*/
