package de.ticket_match.ticketmatch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MyProfile extends AppCompatActivity {

    private User user = null;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();

    //creates entered interest with delete button in screen content
    public static class InterestListAdapter extends BaseAdapter {
        ArrayList<String> result;
        Context context;
        private static LayoutInflater inflater=null;

        public InterestListAdapter(MyProfile mainActivity, ArrayList<String>  prgmNameList) {
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
            ((TextView) rowView.findViewById(R.id.listitem_text)).setText(result.get(position));
            return rowView;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //Receive profile information from database and set user data in top of the screen (name, age, gender, location, ratings)
        final MyProfile that = this;
        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                        ((TextView) findViewById(R.id.myprofile_name)).setText(user.getFirstName() + " " + user.getLastName());
                        ((TextView) findViewById(R.id.myprofile_gender_age)).setText(user.getGender() + " " + user.getBirthday());
                        ((TextView) findViewById(R.id.myprofile_location)).setText(user.getLocation());
                        if (user.getInterests()!= null) {
                            if (user.getInterests().size()!=0) {
                                ((ListView) findViewById(R.id.myprofile_interests)).setAdapter(new InterestListAdapter(that, user.getInterests()));
                            }
                        } else {
                            user.setInterests(new ArrayList<String>(0));
                            ((ListView) findViewById(R.id.myprofile_interests)).setAdapter(new InterestListAdapter(that, user.getInterests()));
                        }

                        if (user.getRatings() != null) {
                            if (user.getRatings().size()!=0) {
                                float f = 0f;
                                for (HashMap<String, String> j : user.getRatings()) {
                                    f = f + new Float(j.get("stars"));
                                }
                                f = f / user.getRatings().size();
                                ((RatingBar) findViewById(R.id.myprofile_rating)).setRating(f);
                            }
                        } else {
                            user.setRatings(new ArrayList<HashMap<String, String>>(0));
                        }
                        ((MainActivityTabHost)getParent()).baseBundle.putString("myprofile_name", user.getFirstName() + " " + user.getLastName());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w("MyProfile", "getUser:onCancelled", databaseError.toException());
                    }
                });

        //get profile picture from database with a size of maximum 512KB
        mStorage.child("images/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg").getBytes(512*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ((ImageButton)findViewById(R.id.myprofile_image)).setImageBitmap(bm);
            }
        });

        //for Ratingbar: if onclicked --> got to screen myprofile_ratings
        ((RatingBar)findViewById(R.id.myprofile_rating)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    //put rating information from user into bundle to give that to new Screen myprofile_ratings
                    if (user.getRatings() != null) {
                        if (user.getRatings().size() != 0) {
                            ((MainActivityTabHost) getParent()).baseBundle.putSerializable("myprofile_ratings", user.getRatings());
                            ((TabHost) getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("myprofile_ratings");
                        }
                    }
                }
                return true;
            }
        });


        // TODO: Check if the app works on all devices
        /*if(savedInstanceState!=null){
            ((ImageButton)findViewById(R.id.myprofile_image)).setImageBitmap((Bitmap)savedInstanceState.getParcelable("myprofile_image"));
        }*/
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        Bitmap bm = ((BitmapDrawable)((ImageButton)findViewById(R.id.myprofile_image)).getDrawable()).getBitmap();
        outState.putParcelable("myprofile_image", bm);
        super.onSaveInstanceState(outState);
    }*/

    // method for deleting an interest
    public void btn_listitem_delete(View view) {
        user.getInterests().remove(((TextView)((View)view.getParent()).findViewById(R.id.listitem_text)).getText().toString());
        ((InterestListAdapter)((ListView) findViewById(R.id.myprofile_interests)).getAdapter()).notifyDataSetChanged();
        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("interests").setValue(user.getInterests());
    }

    //method for adding a new interest
    public void btn_newinterest (View view){
        String interest_text = ((EditText)findViewById(R.id.newinterest_text)).getText().toString();
        if(interest_text.equals("")) {
            Toast.makeText(getApplicationContext(), "Please insert an interest!", Toast.LENGTH_SHORT).show();
        } else if(user.getInterests().contains(interest_text)) {
            ((EditText)findViewById(R.id.newinterest_text)).setText("");
            Toast.makeText(getApplicationContext(), "Interest already exists!", Toast.LENGTH_SHORT).show();
        } else {
            user.getInterests().add(interest_text);
            ((EditText)findViewById(R.id.newinterest_text)).setText("");
            ((InterestListAdapter)((ListView) findViewById(R.id.myprofile_interests)).getAdapter()).notifyDataSetChanged();
            mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("interests").setValue(user.getInterests());
        }
    }

    //method for image button
    public void btn_myprofile_image (View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu_image, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.take_photo:
                        Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        getParent().startActivityForResult(intent_camera, 1);
                        return true;
                    case R.id.upload_photo:
                        Intent intent_upload = new Intent();
                        intent_upload.setType("image/*");
                        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                        getParent().startActivityForResult(Intent.createChooser(intent_upload, "Select file to upload"), 2);
                        return true;
                    case R.id.delete_photo:
                        //TODO: Change R.drawable.contacts in correct image
                        ((ImageButton)findViewById(R.id.myprofile_image)).setImageResource(R.drawable.contacts);
                        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.contacts);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        byte [] ba = bytes.toByteArray();
                        UploadTask uT = mStorage.child("images/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg").putBytes(ba);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();
    }
}
