package de.ticket_match.ticketmatch;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.ticket_match.ticketmatch.entities.MakeDate;

/**
 * Created by D060450 on 22.06.2016.
 */
public class RVAdapter_MakeadateOverview extends RecyclerView.Adapter<RVAdapter_MakeadateOverview.MakeadateViewholder> {

    List<MakeDate> dates;
    List<String> dates_keys;

    public RVAdapter_MakeadateOverview(List<MakeDate> dates, List<String> dates_keys) {
        this.dates = dates;
        this.dates_keys = dates_keys;
    }

    @Override
    public MakeadateViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewitem_makeadate, parent, false);
        MakeadateViewholder madvh = new MakeadateViewholder(v);
        return madvh;
    }

    @Override
    public void onBindViewHolder(MakeadateViewholder holder, int position) {
        MakeDate date = dates.get(position);
        String key = dates_keys.get(position);

        String gender = "";

        if(date.isWithman().equals("true") & date.isWithwoman().equals("false")){
            gender = "man";
        }else if(date.isWithwoman().equals("true") & date.isWithman().equals("false")){
            gender = "woman";
        }else {
            gender = "man | woman";
        }

        holder.name.setText(date.getName() + "\n" + date.getType());
        holder.date.setText(date.getDate() + "\n" + date.getLocation());
        holder.who.setText(date.getTime() + "\n" + gender);
        holder.key.setText(key);

    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public static class MakeadateViewholder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView date;
        TextView who;
        TextView key;

        MakeadateViewholder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_makeadate);
            name = (TextView) itemView.findViewById(R.id.makeadate_row_name);
            date = (TextView) itemView.findViewById(R.id.makeadate_row_date);
            who = (TextView) itemView.findViewById(R.id.makeadate_row_who);
            key = (TextView) itemView.findViewById(R.id.makeadate_row_key);
        }

    }
}
