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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MyProfile extends AppCompatActivity {

    ArrayList<String> listitems = new ArrayList<String>(0);
    int REQUEST_CAMERA = 1;
    int REQUEST_GALLERY = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        listitems.add("Cinema");
        listitems.add("Pick Nick");
        ((ListView) findViewById(R.id.myprofile_interests)).setAdapter(new CustomAdapter(this, listitems));

        ((ImageButton)findViewById(R.id.myprofile_image)).setImageResource(R.drawable.contacts);

        ((RatingBar)findViewById(R.id.myprofile_rating)).setRating(new Float("3.5"));
        ((RatingBar)findViewById(R.id.myprofile_rating)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    Intent myprofile_rating =  new Intent(getApplicationContext(), MyProfileRatings.class);
                    startActivity(myprofile_rating);
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
    }

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
                        FirebaseAuth.getInstance().signOut();
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();

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
                        startActivityForResult(intent_camera, REQUEST_CAMERA);
                        return true;
                    case R.id.upload_photo:
                        Intent intent_upload = new Intent();
                        intent_upload.setType("image/*");
                        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent_upload, "Select File"), REQUEST_GALLERY);
                        return true;
                    case R.id.delete_photo:
                        // Backend: delete photo
                        ((ImageButton)findViewById(R.id.myprofile_image)).setImageResource(R.drawable.contacts);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CAMERA & resultCode==RESULT_OK){

            Bitmap bm_camera = null;
            if (data != null) {
                bm_camera = (Bitmap)data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm_camera.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte [] ba = bytes.toByteArray();
                bm_camera = BitmapFactory.decodeByteArray(ba, 0, ba.length);

            }
            ((ImageButton)findViewById(R.id.myprofile_image)).setImageBitmap(bm_camera);

        } else if(requestCode==REQUEST_GALLERY & resultCode==RESULT_OK){

            Bitmap bm_upload = null;
            if (data != null) {
                try {
                    bm_upload = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                    /*ExifInterface exif = new ExifInterface(data.getData().getPath());
                    int degree = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    Toast.makeText(getApplicationContext(), Integer.toString(degree),Toast.LENGTH_SHORT).show();*/

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ((ImageButton)findViewById(R.id.myprofile_image)).setImageBitmap(bm_upload);
            // Backend save photo

        }
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


    public void btn_newinterest (View view){

        String text = ((EditText)findViewById(R.id.newinterest_text)).getText().toString();
        if(text.equals("")){
            Toast.makeText(getApplicationContext(),"Please insert an interest!",Toast.LENGTH_SHORT).show();
        } else{
            listitems.add(text);
            ((EditText)findViewById(R.id.newinterest_text)).setText("");
            ((CustomAdapter)((ListView) findViewById(R.id.myprofile_interests)).getAdapter()).notifyDataSetChanged();
            // Backend
        }
    }

    public static class ChangeInterestsDialog extends DialogFragment{
        public Dialog onCreateDialog (Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_change_myprofile_interests, null));

            builder.setPositiveButton("Change", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id ){

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id ){
                    dialog.cancel();

                }
            });
            return builder.create();
        }
    }


    public static class ChangePasswordDialog extends DialogFragment{
        public Dialog onCreateDialog (Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_change_password, null));

            builder.setPositiveButton("Change", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id ){
                    Toast.makeText(getActivity(),"Change Password",Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id ){
                    dialog.cancel();

                }
            });
            return builder.create();
        }
    }

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
