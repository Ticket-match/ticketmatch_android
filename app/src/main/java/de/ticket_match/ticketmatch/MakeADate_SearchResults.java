package de.ticket_match.ticketmatch;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.ticket_match.ticketmatch.entities.Chat;
import de.ticket_match.ticketmatch.entities.MakeDate;

public class MakeADate_SearchResults extends AppCompatActivity {

    private ArrayList<MakeDate> dates_list;
    RVAdapter_MakeadateSearchresults adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_adate__search_results);

        dates_list = new ArrayList<MakeDate>(0);

        RecyclerView rv = (RecyclerView) findViewById(R.id.makedate_searchresults);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        adapter = new RVAdapter_MakeadateSearchresults(dates_list, this);
        rv.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    public void sortAndDelete(ArrayList<MakeDate> dates) {
        dates_list.clear();

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date dl,dr, t, today = null;

        try {
            today = dateFormat.parse(dateFormat.format(new Date()));
            for (MakeDate ti : dates) {
                try {
                    t = dateFormat.parse(ti.getDate());
                    if (t.compareTo(today)>=0) {
                        dates_list.add(ti);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int n = 0; n < dates_list.size(); n++) {
            for (int m = 0; m < (dates_list.size()-1) - n; m++) {
                try {
                    dl = dateFormat.parse(dates_list.get(m).getDate());
                    dr = dateFormat.parse(dates_list.get(m+1).getDate());
                    int i = dl.compareTo(dr);

                    if (i>0) {
                        MakeDate swapTicket = dates_list.get(m);
                        dates_list.set(m,dates_list.get(m+1));
                        dates_list.set(m+1,swapTicket);
                    } else if (i==0) {
                        dl = timeFormat.parse(dates_list.get(m).getTime());
                        dr = timeFormat.parse(dates_list.get(m+1).getTime());
                        i = dl.compareTo(dr);

                        if (i>0) {
                            MakeDate swapTicket = dates_list.get(m);
                            dates_list.set(m,dates_list.get(m+1));
                            dates_list.set(m+1,swapTicket);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

}