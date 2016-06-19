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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ForeignProfileRating extends AppCompatActivity {

    ArrayList<HashMap<String,String>> ratings_list = new ArrayList<HashMap<String,String>>(0);
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();


    public static class RatingListAdapter extends BaseAdapter {
        ArrayList<HashMap<String,String>> ratings_list;
        Context context;
        private static LayoutInflater inflater=null;

        public RatingListAdapter(ForeignProfileRating mainActivity, ArrayList<HashMap<String,String>> ratings_list) {
            this.ratings_list = ratings_list;
            context=mainActivity;
            inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return ratings_list.size();
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
            View rowView = inflater.inflate(R.layout.listitem_ratings, null);
            HashMap<String,String> hm = ratings_list.get(position);

            ((RatingBar)rowView.findViewById(R.id.listitem_rating_stars)).setRating(new Float(hm.get("stars")));
            ((TextView) rowView.findViewById(R.id.listitem_rating_text)).setText(hm.get("date") + " " + hm.get("timestamp") + "\n" + hm.get("author") + "\n" + hm.get("text"));
            return rowView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_profile_rating);

        ((ListView) findViewById(R.id.foreignprofile_ratings)).setAdapter(new RatingListAdapter(this, ratings_list));
    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    public void updateFRating(ArrayList<HashMap<String,String>> ratings) {
        ratings_list.clear();
        for (HashMap<String,String> h :ratings) {
            ratings_list.add(h);
        }
        ((RatingListAdapter)((ListView) findViewById(R.id.foreignprofile_ratings)).getAdapter()).notifyDataSetChanged();
    }


    public void btn_newrating (View view) {
        String rating = Float.toString(((RatingBar)findViewById(R.id.newrating_stars)).getRating()).substring(0,2);
        String rating_text = ((EditText)findViewById(R.id.newrating_text)).getText().toString();

        if (rating.equals("") | rating_text.equals("")) {
            Toast.makeText(getApplicationContext(),"Please fill out the required information!",Toast.LENGTH_SHORT).show();
        } else {
            String foreignUid = ((MainActivityTabHost)getParent()).baseBundle.getString(TicketMatch.FOREIGN_PROFILE_UID);
            Date dDate = Calendar.getInstance().getTime();
            SimpleDateFormat timeDateFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String timestamp = timeDateFormat.format(dDate);
            String sDate = dateFormat.format(dDate);
            String author = TicketMatch.getCurrentUser().getFirstName() + " " + TicketMatch.getCurrentUser().getLastName();

            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("author", author);
            hm.put("date", sDate);
            hm.put("stars", rating);
            hm.put("text", rating_text);
            hm.put("timestamp", timestamp);

            ratings_list.add(hm);

            ((RatingListAdapter)((ListView) findViewById(R.id.foreignprofile_ratings)).getAdapter()).notifyDataSetChanged();

            mDatabase.child("users").child(foreignUid).child("ratings").child(String.valueOf(ratings_list.size()-1)).setValue(hm);

            //String rt = "me|" + new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + "|" + rating_text;
            ((EditText)findViewById(R.id.newrating_text)).setText("");
            ((RatingBar)findViewById(R.id.newrating_stars)).setRating(0);

            Toast.makeText(getApplicationContext(), "Rated successfully!", Toast.LENGTH_SHORT).show();
        }

    }
}
