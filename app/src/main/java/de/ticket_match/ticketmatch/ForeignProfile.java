package de.ticket_match.ticketmatch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import de.ticket_match.ticketmatch.entities.Ticket;
import de.ticket_match.ticketmatch.entities.User;

public class ForeignProfile extends AppCompatActivity {

    private static final String TAG = "ForeignProfile";
    private User user = null;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private ArrayList<String> interests = new ArrayList<String>(0);

    public static class InterestListAdapter extends BaseAdapter {
        ArrayList<String> result;
        Context context;
        private static LayoutInflater inflater=null;

        public InterestListAdapter(ForeignProfile mainActivity, ArrayList<String>  prgmNameList) {
            result=prgmNameList;
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
            View rowView = inflater.inflate(R.layout.listitem_interests, null);
            rowView.findViewById(R.id.listitem_interests_delete).setVisibility(View.INVISIBLE);
            ((TextView) rowView.findViewById(R.id.listitem_text)).setText(result.get(position));
            return rowView;
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_profile);

        ((ListView) findViewById(R.id.foreignprofile_interests)).setAdapter(new InterestListAdapter(this, interests));

        ((RatingBar)findViewById(R.id.foreignprofile_rating)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    //put rating information from user into bundle to give that to new Screen myprofile_ratings
                    if (user.getRatings() != null) {
                        ((TabHost) getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("foreign_profile_ratings");
                        ((ForeignProfileRating)((TabHost)getParent().findViewById(R.id.tabHost)).getCurrentView().getContext()).updateFRating(user.getRatings());
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    public void updateFProfile(String key) {
        mDatabase.child("users").child(key).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                        ((TextView) findViewById(R.id.foreignprofile_name)).setText(user.getFirstName() + " " + user.getLastName());
                        ((TextView) findViewById(R.id.foreignprofile_gender_age)).setText(user.getGender() + " " + user.getBirthday());
                        ((TextView) findViewById(R.id.foreignprofile_location)).setText(user.getLocation());

                        interests.clear();
                        if (user.getInterests()!= null) {
                            if (user.getInterests().size()!=0) {
                                for (String i : user.getInterests()) {
                                    interests.add(i);
                                }
                            }
                        } else {
                            user.setInterests(new ArrayList<String>(0));
                        }
                        ((InterestListAdapter)((ListView) findViewById(R.id.foreignprofile_interests)).getAdapter()).notifyDataSetChanged();


                        if (user.getRatings() != null) {
                            if (user.getRatings().size()!=0) {
                                float f = 0f;
                                for (HashMap<String, String> j : user.getRatings()) {
                                    f = f + new Float(j.get("stars"));
                                }
                                f = f / user.getRatings().size();
                                ((RatingBar) findViewById(R.id.foreignprofile_rating)).setRating(f);
                            }
                        } else {
                            user.setRatings(new ArrayList<HashMap<String, String>>(0));
                            ((RatingBar) findViewById(R.id.foreignprofile_rating)).setRating(0);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        mStorage.child("images/"+key+".jpg").getBytes(512*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ((ImageButton)findViewById(R.id.foreignprofile_image)).setImageBitmap(bm);
            }
        });
    }

    /*
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
        Intent find = new Intent(this, Find.class);
        startActivity(find);
    }

    public void btn_makematch(View view) {
        Intent makeadate = new Intent(this, MakeADate.class);
        startActivity(makeadate);
    }
    */
}
