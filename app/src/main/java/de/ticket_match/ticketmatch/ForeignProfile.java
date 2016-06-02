package de.ticket_match.ticketmatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.ArrayList;

public class ForeignProfile extends AppCompatActivity {

    ArrayList<String> listitems_foreignprofile = new ArrayList<String>(0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_profile);

        ((ImageButton)findViewById(R.id.foreignprofile_image)).setImageResource(R.drawable.contacts);

        listitems_foreignprofile.add("Cinema");
        listitems_foreignprofile.add("Pick Nick");
        ((ListView) findViewById(R.id.foreignprofile_interests)).setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listitems_foreignprofile));

        ((RatingBar)findViewById(R.id.foreignprofile_rating)).setRating(new Float("3.5"));
        ((RatingBar)findViewById(R.id.foreignprofile_rating)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    Intent foreignprofile_rating =  new Intent(getApplicationContext(), ForeignProfileRating.class);
                    startActivity(foreignprofile_rating);
                }
                return true;
            }
        });

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
        Toast.makeText(getApplicationContext(),"btn_ticketoffer",Toast.LENGTH_SHORT).show();
    }

    public void btn_search(View view) {
        Toast.makeText(getApplicationContext(),"btn_search",Toast.LENGTH_SHORT).show();
    }

    public void btn_makematch(View view) {
        Toast.makeText(getApplicationContext(),"btn_makematch",Toast.LENGTH_SHORT).show();
    }
}
