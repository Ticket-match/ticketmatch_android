package de.ticket_match.ticketmatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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

    TabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_adate);

        mTabHost = ((TabHost)getParent().findViewById(R.id.tabHost));

        mDatabase.child("makedates").orderByChild("user").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dates.clear();
                dates_keys.clear();

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

                            if (i>0) {
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

                                if (i>0) {
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
        String DateKey = getMakeADateKeyFromCardItem(view);
        deleteMakeDate(DateKey);
    }

    public void btn_date_edit (View view){
        String DateKey = getMakeADateKeyFromCardItem(view);
        MakeDate makeADate = getMakeDateFromCardItem(view);
        mTabHost.setCurrentTabByTag("makeadate_new");
        ((TextView)getParent().findViewById(R.id.headerTitle)).setText("Edit A Date");
        New_MakeAdate newMakeADate = (New_MakeAdate) mTabHost.getCurrentView().getContext();
        newMakeADate.setEditMakeDate(true);
        newMakeADate.setMakeDateKey(DateKey);
        newMakeADate.setMakeDateTextFields(makeADate);
    }

    public String getMakeADateKeyFromCardItem(View view){
        //We have an invisible Textfield in the style file to get the ticket key.
        String date_key = ((TextView)((View)view.getParent().getParent()).findViewById(R.id.makeadate_row_key)).getText().toString();
        return date_key;
    }


    /**
     * Deletes a Date out of the Firebase Database and
     * notifies the Recyclerview to update the list
     * @param FirebaseMakeADateKey Date Key, generated by Firebase
     * @see MakeDate
     */
    public void deleteMakeDate(String FirebaseMakeADateKey){
        mDatabase.child("makedates").child(FirebaseMakeADateKey).removeValue();
    }

    public MakeDate getMakeDateFromCardItem(View view){
        String dateKey = getMakeADateKeyFromCardItem(view);
        int date_position = dates_keys.indexOf(dateKey);
        MakeDate makeADate = dates.get(date_position);
        return makeADate;
    }


}
