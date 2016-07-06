package de.ticket_match.ticketmatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import de.ticket_match.ticketmatch.entities.Ticket;

public class Find extends AppCompatActivity {

    ArrayList<Ticket> listitems_find;
    ListView listview;
    RVAdapter_TicketSearchResults adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        listitems_find = new ArrayList<Ticket>(0);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.find_results);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        adapter = new RVAdapter_TicketSearchResults(listitems_find, this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    public void sortAndDelete(ArrayList<Ticket> tickets) {
        listitems_find.clear();

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date dl,dr, t, today = null;

        try {
            today = dateFormat.parse(dateFormat.format(new Date()));
            for (Ticket ti : tickets) {
                try {
                    t = dateFormat.parse(ti.getDate());
                    if (t.compareTo(today)>=0) {
                        listitems_find.add(ti);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int n = 0; n < listitems_find.size(); n++) {
            for (int m = 0; m < (listitems_find.size()-1) - n; m++) {
                try {
                    dl = dateFormat.parse(listitems_find.get(m).getDate());
                    dr = dateFormat.parse(listitems_find.get(m+1).getDate());
                    int i = dl.compareTo(dr);

                    if (i>0) {
                        Ticket swapTicket = listitems_find.get(m);
                        listitems_find.set(m,listitems_find.get(m+1));
                        listitems_find.set(m+1,swapTicket);
                    } else if (i==0) {
                        dl = timeFormat.parse(listitems_find.get(m).getTime());
                        dr = timeFormat.parse(listitems_find.get(m+1).getTime());
                        i = dl.compareTo(dr);

                        if (i>0) {
                            Ticket swapTicket = listitems_find.get(m);
                            listitems_find.set(m,listitems_find.get(m+1));
                            listitems_find.set(m+1,swapTicket);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

}