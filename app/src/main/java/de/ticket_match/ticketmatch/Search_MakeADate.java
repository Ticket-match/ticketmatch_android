package de.ticket_match.ticketmatch;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Calendar;

import de.ticket_match.ticketmatch.entities.MakeDate;

public class Search_MakeADate extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<MakeDate> dates = new ArrayList<MakeDate>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__make_adate);

        //Dropdown Event Type
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.event_type_search, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner)findViewById(R.id.search_makeadate_eventtype)).setAdapter(adapter);

        //Datepicker
        ((TextView)findViewById(R.id.search_makeadate_date)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TicketMatch)getApplication()).minimizeKeyboard(v);
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(getParent(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = dayOfMonth + "." + (monthOfYear+1) + "." + year;
                        ((TextView)((TabHost)((MainActivityTabHost)getParent()).findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.search_makeadate_date)).setText(date);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                return false;
            }
        });
        }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    public void btn_search_makeadate(View view){

        //check if all values are entered, if yes save data in database and delete input fields
            dates.clear();
            final String date = ((TextView)findViewById(R.id.search_makeadate_date)).getText().toString();
            final String location = ((EditText)findViewById(R.id.search_makeadate_eventlocation)).getText().toString();
            final String type = ((Spinner)findViewById(R.id.search_makeadate_eventtype)).getSelectedItem().toString();

            final ArrayList<String[]> command = new ArrayList<String[]>(0);

            if(!date.equals("Date")){
                command.add(new String []{"date", date});
            }
            if(!location.equals("")){
                command.add(new String []{"location", location});
            }
            if(!type.equals("")){
                command.add(new String []{"type", type});
            }
            if(command.size()==0){
                Toast.makeText(getApplicationContext(),"You have to enter at least one search condition",Toast.LENGTH_SHORT).show();
            } else {
                final ProgressDialog progressDialog;
                progressDialog = ProgressDialog.show(Search_MakeADate.this, "Please wait ...", "Loading results ...", true);
                progressDialog.setCancelable(true);

                mDatabase.child("makedates").orderByChild(command.get(0)[0]).equalTo(command.get(0)[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            MakeDate date = d.getValue(MakeDate.class);
                            if (!date.getUser().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                if(command.size() == 1){
                                    dates.add(date);
                                } else if (command.size()==2){
                                    if(command.get(1)[0].equals("date")){
                                        if (date.getDate().equals(command.get(1)[1])){
                                            dates.add(date);
                                        }
                                    } else if(command.get(1)[0].equals("location")) {
                                        if (date.getLocation().equals(command.get(1)[1])){
                                            dates.add(date);
                                        }
                                    } else if(command.get(1)[0].equals("type")) {
                                        if (date.getType().equals(command.get(1)[1])){
                                            dates.add(date);
                                        }
                                    }
                                } else if (command.size() == 3) {
                                    if(command.get(1)[0].equals("date") & command.get(2)[0].equals("type")){
                                        if (date.getDate().equals(command.get(1)[1]) & date.getType().equals(command.get(2)[1])){
                                            dates.add(date);
                                        }
                                    }
                                    else if(command.get(1)[0].equals("type") & command.get(2)[0].equals("date")){
                                        if (date.getType().equals(command.get(1)[1]) & date.getDate().equals(command.get(2)[1])){
                                            dates.add(date);
                                        }
                                    }
                                    else if(command.get(1)[0].equals("location") & command.get(2)[0].equals("type")){
                                        if (date.getLocation().equals(command.get(1)[1]) & date.getType().equals(command.get(2)[1])){
                                            dates.add(date);
                                        }
                                    }
                                    else if(command.get(1)[0].equals("type") & command.get(2)[0].equals("location")){
                                        if (date.getType().equals(command.get(1)[1]) & date.getLocation().equals(command.get(2)[1])){
                                            dates.add(date);
                                        }
                                    }
                                    else if(command.get(1)[0].equals("location") & command.get(2)[0].equals("date")){
                                        if (date.getLocation().equals(command.get(1)[1]) & date.getDate().equals(command.get(2)[1])){
                                            dates.add(date);
                                        }
                                    }
                                    else if(command.get(1)[0].equals("date") & command.get(2)[0].equals("location")){
                                        if (date.getDate().equals(command.get(1)[1]) & date.getLocation().equals(command.get(2)[1])){
                                            dates.add(date);
                                        }
                                    }
                                }
                            }
                        }

                        //set dates in a bundle to push it to the next activity (Find)
                        ((MainActivityTabHost) getParent()).baseBundle.putSerializable("makeadate_search_result", dates);
                        ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("makeadate_search_result");
                        ((MakeADate_SearchResults)((TabHost) getParent().findViewById(R.id.tabHost)).getCurrentView().getContext()).sortAndDelete();
                        ( ((RecyclerView) ((TabHost) getParent().findViewById(R.id.tabHost)).getCurrentView().findViewById(R.id.makedate_searchresults)).getAdapter()).notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                // delete input fields
                ((TextView)findViewById(R.id.search_makeadate_date)).setText("Date");
                ((EditText)findViewById(R.id.search_makeadate_eventlocation)).setText("");
                ((Spinner)findViewById(R.id.search_makeadate_eventtype)).setSelection(0);

                // hide keyboard
                View aview = this.getCurrentFocus();
                if (aview != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(aview.getWindowToken(), 0);
                }
        }
    }
}
