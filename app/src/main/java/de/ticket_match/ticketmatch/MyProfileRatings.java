package de.ticket_match.ticketmatch;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class MyProfileRatings extends AppCompatActivity {

    //creates ratings from database on screen content
    public static class RatingListAdapter extends BaseAdapter {
        ArrayList<HashMap<String,String>> ratings_list;
        Context context;
        private static LayoutInflater inflater=null;

        public RatingListAdapter(MyProfileRatings mainActivity, ArrayList<HashMap<String,String>> ratings_list) {
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
        setContentView(R.layout.activity_my_profile_ratings);

        ArrayList<HashMap<String,String>> ratings_list = (ArrayList<HashMap<String,String>>)((MainActivityTabHost)getParent()).baseBundle.getSerializable("myprofile_ratings");
        ((ListView) findViewById(R.id.myprofile_ratings)).setAdapter(new RatingListAdapter(this, ratings_list));
    }
}
