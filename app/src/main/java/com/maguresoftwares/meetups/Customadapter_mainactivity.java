package com.maguresoftwares.meetups;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.maguresoftwares.meetups.Models.meetups;

import java.util.ArrayList;
import java.util.List;

public class Customadapter_mainactivity extends ArrayAdapter<meetups> {

    private int mLayoutResource;
    private Context mContext;
    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private ArrayList<String> Keys ;
    private ArrayList<meetups> mMeetups;

    public Customadapter_mainactivity(@NonNull Context context, @LayoutRes int resource, @NonNull List<meetups> objects , ArrayList<meetups> mMeetups , ArrayList<String> Keys ) {
        super(context, resource, objects);
        mContext = context;
        mLayoutResource = resource;
        this.mMeetups = mMeetups;
        this.Keys = Keys;
    }
    public static class ViewHolder{
        TextView meetup_date , meetup_timefrom , meetup_timetill , meetup_title , meetup_location;
        FloatingActionButton key_check_button;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Customadapter_mainactivity.ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mLayoutResource, parent, false);
            holder = new ViewHolder();

            holder.meetup_date = (TextView) convertView.findViewById(R.id.meetup_date);
            holder.meetup_timefrom = (TextView) convertView.findViewById(R.id.meetup_timefrom);
            holder.meetup_timetill = (TextView) convertView.findViewById(R.id.meetup_timetill);
            holder.meetup_title = (TextView) convertView.findViewById(R.id.meetup_title);
            holder.meetup_location = (TextView) convertView.findViewById(R.id.meetup_location);
            holder.key_check_button = (FloatingActionButton) convertView.findViewById(R.id.key_check_button);

            convertView.setTag(holder);
        }else{
            holder = (Customadapter_mainactivity.ViewHolder) convertView.getTag();
            holder.meetup_date.setText("");
            holder.meetup_timefrom.setText("");
            holder.meetup_timetill.setText("");
            holder.meetup_title.setText("");
            holder.meetup_location.setText("");
        }

        try{
            holder.meetup_title.setText(getItem(position).getMeetup_title());
            holder.meetup_location.setText(getItem(position).getLocation());

            String date = getItem(position).getDate();
            String day = date.substring(0,2);
            String month = date.substring(3,5);
            int mon = Integer.parseInt(month);

            holder.meetup_date.setText(MONTHS[mon-1] + "\n"+day);

            String meetup_timefrom = getItem(position).getTime_from();
            String meetup_timeTill = getItem(position).getTime_till();
            String meetuptimefromhour = meetup_timefrom.substring(0, 2);
            String meetup_timefrommin = meetup_timefrom.substring(3, 5);
            String meetuptimetillhour = meetup_timeTill.substring(0, 2);
            String meetup_timetillmin = meetup_timeTill.substring(3, 5);

            int meetup_timefromhour=Integer.parseInt(meetuptimefromhour);
            int meetup_timetillhour=Integer.parseInt(meetuptimetillhour);
            if (meetup_timefromhour > 12) {
                holder.meetup_timefrom.setText((meetup_timefromhour - 12) + ":" + meetup_timefrommin + " pm -");
            }else
            {
                holder.meetup_timefrom.setText(meetup_timefromhour + ":" + meetup_timefrommin + " am -");
            }
            if (meetup_timetillhour > 12) {
                holder.meetup_timetill.setText((meetup_timetillhour - 12) + ":" + meetup_timetillmin + " pm");
            }else
            {
                holder.meetup_timetill.setText(meetup_timetillhour + ":" + meetup_timetillmin + " am");
            }

            //holder.key_check_button.setVisibility(View.INVISIBLE);
            holder.key_check_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putStringArrayList("key_list",Keys);
                    b.putParcelableArrayList("meetups_list",mMeetups);
                    BottomSheetDialogFragment bottomSheetDialogFragment = new keys_bottom_sheet();
                    bottomSheetDialogFragment.setArguments(b);
                    bottomSheetDialogFragment.show(((FragmentActivity)mContext).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                }
            });

        }catch (NullPointerException e){
        }

        return convertView;
    }
}
