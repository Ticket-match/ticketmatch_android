package de.ticket_match.ticketmatch;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import de.ticket_match.ticketmatch.entities.User;

public class MyProfile extends AppCompatActivity {

    private final static String TAG = "MyProfile";
    private User user = null;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private ArrayList<String> interests;

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
            return result.get(position);
        }
        //return position;

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

        interests = new ArrayList<String>(0);
        final InterestListAdapter adapter = new InterestListAdapter(this,interests);
        ((ListView) findViewById(R.id.myprofile_interests)).setAdapter(adapter);

        //Receive profile information from database and set user data in top of the screen (name, age, gender, location, ratings)
        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                        TicketMatch.setCurrentUser(user);

                        String s = "";
                        if (user.getFirstName()!=null) s = user.getFirstName() + " ";
                        if (user.getLastName()!=null) s = s + user.getLastName();
                        ((TextView) findViewById(R.id.myprofile_name)).setText(s);

                        s = "";
                        if (user.getGender()!=null) s = user.getGender() + " ";
                        if (user.getBirthday()!=null) s = s + user.getBirthday();
                        ((TextView) findViewById(R.id.myprofile_gender_age)).setText(s);

                        s = "";
                        if (user.getLocation()!=null) s = user.getLocation();
                        ((TextView) findViewById(R.id.myprofile_location)).setText(s);

                        if (user.getInterests()!= null) {
                            if (user.getInterests().size()!=0) {
                                interests.clear();
                                interests.addAll(user.getInterests());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            user.setInterests(new ArrayList<String>(0));
                            interests.clear();
                            adapter.notifyDataSetChanged();
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

                        ((MainActivityTabHost)getParent()).baseBundle.putString("myprofile_name", ((TextView) findViewById(R.id.myprofile_name)).getText().toString());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.profilbild);
        ((ImageButton)findViewById(R.id.myprofile_image)).setImageBitmap(bm);
        profileImage();

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

    }

    public void profileImage() {
        //get profile picture from database with a size of maximum 512KB
        mStorage.child("images/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg").getBytes(512*1024).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()) {
                    Bitmap bm = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                    ((ImageButton)findViewById(R.id.myprofile_image)).setImageBitmap(bm);
                } else {
                    profileImage();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    // method for deleting an interest
    public void btn_listitem_delete(View view) {
        user.getInterests().remove(((TextView)((View)view.getParent()).findViewById(R.id.listitem_text)).getText().toString());
        //((InterestListAdapter)((ListView) findViewById(R.id.myprofile_interests)).getAdapter()).notifyDataSetChanged();
        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("interests").setValue(user.getInterests());
    }

    //method for adding a new interest
    public void btn_newinterest (View view){
        String interest_text = ((EditText)findViewById(R.id.newinterest_text)).getText().toString();
        if(interest_text.equals("")) {
            Toast.makeText(getApplicationContext(), "Please insert an interest!", Toast.LENGTH_SHORT).show();
        } else if(containsIgnoreCase(user.getInterests(), interest_text)) {
            ((EditText)findViewById(R.id.newinterest_text)).setText("");
            Toast.makeText(getApplicationContext(), "Interest already exists!", Toast.LENGTH_SHORT).show();
        } else {
            if (TicketMatch.getCurrentUser() != null) {
                user.getInterests().add(interest_text);
                //((InterestListAdapter)((ListView) findViewById(R.id.myprofile_interests)).getAdapter()).notifyDataSetChanged();
                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("interests").setValue(user.getInterests());

                ((EditText) findViewById(R.id.newinterest_text)).setText("");
            }
        }
    }

    public boolean containsIgnoreCase (ArrayList<String> list, String string) {
        for (String s : list) {
            if (string.equalsIgnoreCase(s)) return true;
        }
        return false;
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
                        //Check if the app has permissions to use the Camera
                        int permissionCheckCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                        if(permissionCheckCamera != PackageManager.PERMISSION_GRANTED){
                            //After the permission request, the onRequestPermissionsResult method in the MainActivityTabHost class is called (asynchronous call)
                            ActivityCompat.requestPermissions(getParent(),
                                    new String[]{Manifest.permission.CAMERA},
                                    TicketMatch.MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                        else {
                            //Permissions are already granted - taking a photo is possible
                            TicketMatch.takePhoto(getParent());
                        }
                        return true;
                    case R.id.upload_photo:
                        int permissionCheckStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                        if(permissionCheckStorage != PackageManager.PERMISSION_GRANTED){
                            //After the permission request, the onRequestPermissionsResult method in the MainActivityTabHost class is called (asynchronous call)
                            ActivityCompat.requestPermissions(getParent(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    TicketMatch.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                        else {
                            //Permissions are already granted - uploading a photo is possible
                            TicketMatch.uploadPhoto(getParent());
                        }
                        return true;
                    case R.id.delete_photo:
                        ((ImageButton)findViewById(R.id.myprofile_image)).setImageResource(R.drawable.profilbild);
                        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.profilbild);
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