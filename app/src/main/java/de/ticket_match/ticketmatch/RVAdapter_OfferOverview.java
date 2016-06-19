package de.ticket_match.ticketmatch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;

import de.ticket_match.ticketmatch.entities.Chat;
import de.ticket_match.ticketmatch.entities.Ticket;
import de.ticket_match.ticketmatch.entities.User;

public class RVAdapter_OfferOverview extends RecyclerView.Adapter<RVAdapter_OfferOverview.OfferViewHolder>{

    List<Ticket> tickets;
    List<String> tickets_keys;
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    Activity prevActivity;
    private static LayoutInflater inflater = null;

    RVAdapter_OfferOverview(List<Ticket> tickets, final Activity prevActivity, final List<String> tickets_keys){
        this.tickets = tickets;
        this.prevActivity = prevActivity;
        this.tickets_keys = tickets_keys;
        this.inflater = (LayoutInflater) prevActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView type;
        TextView name;
        TextView date;
        TextView key;

        OfferViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            type = (TextView)itemView.findViewById(R.id.row_type);
            name = (TextView) itemView.findViewById(R.id.row_name);
            date = (TextView) itemView.findViewById(R.id.row_date);
            key = (TextView) itemView.findViewById(R.id.row_key);
        }
    }

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewitem_mytickets, parent, false);
        OfferViewHolder ovh = new OfferViewHolder(v);
        return ovh;
    }

    @Override
    //Changed holder and position to final
    public void onBindViewHolder(final OfferViewHolder holder, final int position) {
        Ticket ticket = tickets.get(position);
        String ticket_key = tickets_keys.get(position);
        String type = ticket.getType();
        String name = ticket.getName();
        String date = ticket.getDate();

        holder.type.setText(ticket.getName() + "\n" + ticket.getType());
        holder.name.setText(ticket.getDate() + "\n" + ticket.getLocation());
        holder.date.setText(ticket.getTime() + "\n" + ticket.getQuantity() + "pc. | " + ticket.getPrice().get("value") + " " + ticket.getPrice().get("currency"));
        holder.key.setText(ticket_key);


    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}