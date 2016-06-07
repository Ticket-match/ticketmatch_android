package de.ticket_match.ticketmatch;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MyProfile extends AppCompatActivity {

    ArrayList<String> listitems;
    ArrayList<HashMap<String,String>>ratingitems;
    int REQUEST_CAMERA = 1;
    int REQUEST_GALLERY = 2;

    private final String TAG = "MyProfile";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private String fireUserId;

    MyProfile that = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Set Profile Name, Gender, Age and Location variables
        final TextView myProfileName = (TextView) findViewById(R.id.myprofile_name);
        final TextView myProfileGenderAndAge = (TextView) findViewById(R.id.myprofile_gender_age);
        final TextView myProfileLocation = (TextView) findViewById(R.id.myprofile_location);


        //Receive profile information from database
        //TODO: Catch Nullpointer Exception
        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value and set the Textviews appropriate
                        User user = dataSnapshot.getValue(User.class);
                        myProfileName.setText(user.getFirstName() + " " + user.getLastName());
                        myProfileGenderAndAge.setText(user.getGender() + " " + user.getBirthday());
                        myProfileLocation.setText(user.getLocation());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("interests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listitems = (ArrayList<String>)dataSnapshot.getValue();
                ((ListView) findViewById(R.id.myprofile_interests)).setAdapter(new MyProfile.CustomAdapter(that, listitems));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final long b = 512*1024;
        mStorage.child("images/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg").getBytes(b).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ((ImageButton)findViewById(R.id.myprofile_image)).setImageBitmap(bm);
            }
        });



        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ratings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ratingitems = (ArrayList<HashMap<String,String>>)dataSnapshot.getValue();
                float f = 0f;
                for (HashMap<String,String> j:ratingitems) {
                    f=f+new Float(j.get("stars"));
                }
                f = f / ratingitems.size();
                ((RatingBar)findViewById(R.id.myprofile_rating)).setRating(f);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ((RatingBar)findViewById(R.id.myprofile_rating)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    //Intent myprofile_rating =  new Intent(getApplicationContext(), MyProfileRatings.class);
                    //startActivity(myprofile_rating);
                    ((TabHost)getParent().findViewById(R.id.tabHost)).setCurrentTabByTag("myprofile_ratings");
                }
                return true;
            }
        });

        if(savedInstanceState!=null){
            ((ImageButton)findViewById(R.id.myprofile_image)).setImageBitmap((Bitmap)savedInstanceState.getParcelable("profile_image"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Bitmap bm = ((BitmapDrawable)((ImageButton)findViewById(R.id.myprofile_image)).getDrawable()).getBitmap();
        outState.putParcelable("profile_image", bm);
        super.onSaveInstanceState(outState);
    }

    public void btn_listitem_delete(View view) {
        String del = ((TextView)((View)view.getParent()).findViewById(R.id.listitem_text)).getText().toString();
        listitems.remove(del);
        ((CustomAdapter)((ListView) findViewById(R.id.myprofile_interests)).getAdapter()).notifyDataSetChanged();
        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("interests").setValue(listitems);
    }

    public void btn_newinterest (View view){

        String text = ((EditText)findViewById(R.id.newinterest_text)).getText().toString();
        if(text.equals("")) {
            Toast.makeText(getApplicationContext(), "Please insert an interest!", Toast.LENGTH_SHORT).show();
        } else if(listitems.contains(text)) {
            ((EditText)findViewById(R.id.newinterest_text)).setText("");
            Toast.makeText(getApplicationContext(), "Interest already exists!", Toast.LENGTH_SHORT).show();
        } else {
            listitems.add(text);
            ((EditText)findViewById(R.id.newinterest_text)).setText("");
            ((CustomAdapter)((ListView) findViewById(R.id.myprofile_interests)).getAdapter()).notifyDataSetChanged();
            mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("interests").setValue(listitems);
        }
    }

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
                        getParent().startActivityForResult(intent_camera, REQUEST_CAMERA);
                        return true;
                    case R.id.upload_photo:
                        Intent intent_upload = new Intent();
                        intent_upload.setType("image/*");
                        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                        getParent().startActivityForResult(Intent.createChooser(intent_upload, "Select File"), REQUEST_GALLERY);
                        return true;
                    case R.id.delete_photo:
                        ((ImageButton)findViewById(R.id.myprofile_image)).setImageResource(R.drawable.profile_default);
                        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.profile_default);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        byte [] ba = bytes.toByteArray();
                        UploadTask uT = mStorage.child("images/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg").putBytes(ba);
                        return true;
                    default:
                        return true;
                }
            }
        });

        popup.show();

    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("" + requestCode + requestCode + data);
        if(requestCode==REQUEST_CAMERA & resultCode==RESULT_OK){

            Bitmap bm_camera = null;
            if (data != null) {
                bm_camera = (Bitmap)data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm_camera.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte [] ba = bytes.toByteArray();
                bm_camera = BitmapFactory.decodeByteArray(ba, 0, ba.length);


                UploadTask uT = mStorage.child("images/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg").putBytes(ba);
                uT.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e.getMessage());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        System.out.println(taskSnapshot.getDownloadUrl());
                    }
                });

            }
            ((ImageButton)findViewById(R.id.myprofile_image)).setImageBitmap(bm_camera);

        } else if(requestCode==REQUEST_GALLERY & resultCode==RESULT_OK){

            Bitmap bm_upload = null;
            if (data != null) {
                try {
                    bm_upload = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm_upload.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    byte [] ba = bytes.toByteArray();
                    bm_upload = BitmapFactory.decodeByteArray(ba, 0, ba.length);

                    UploadTask uT = mStorage.child("images/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg").putBytes(ba);

                    //ExifInterface exif = new ExifInterface(data.getData().getPath());
                    //int degree = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    //Toast.makeText(getApplicationContext(), Integer.toString(degree),Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ((ImageButton)findViewById(R.id.myprofile_image)).setImageBitmap(bm_upload);
            // Backend save photo

        }
    }*/

    public static class CustomAdapter extends BaseAdapter {
        ArrayList<String> result;
        Context context;
        private static LayoutInflater inflater=null;

        public CustomAdapter(MyProfile mainActivity, ArrayList<String>  prgmNameList) {
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
            // TODO Auto-generated method stub
            View rowView = inflater.inflate(R.layout.listitem_interests, null);
            ((TextView) rowView.findViewById(R.id.listitem_text)).setText(result.get(position));
            return rowView;
        }

    }

}
