package com.maguresoftwares.meetups;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.maguresoftwares.meetups.Models.meetups;

public class information_Activity extends AppCompatActivity {

    private meetups mMeetups;

    private Toolbar information_toolbar;
    private CollapsingToolbarLayout information_collapsingtoolbar;

    private ImageButton information_back;
    private ImageView key_icon;

    private TextView information_meetupname , description , information_date , information_timefrom;
    private TextView information_timetill , location , host_name , occupation , information_key;

    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        information_collapsingtoolbar = (CollapsingToolbarLayout) findViewById(R.id.information_collapsingtoolbar);
        information_toolbar = (Toolbar) findViewById(R.id.information_toolbar);

        information_back = (ImageButton) findViewById(R.id.information_back);
        key_icon = (ImageView) findViewById(R.id.key_icon);

        information_meetupname = (TextView) findViewById(R.id.information_meetupname);
        description = (TextView) findViewById(R.id.description);
        information_date = (TextView) findViewById(R.id.information_date);
        information_timefrom = (TextView) findViewById(R.id.information_timefrom);
        information_timetill = (TextView) findViewById(R.id.information_timetill);
        location = (TextView) findViewById(R.id.location);
        host_name = (TextView) findViewById(R.id.host_name);
        occupation = (TextView) findViewById(R.id.occupation);
        information_key = (TextView) findViewById(R.id.information_key);

        Intent i = getIntent();
        final Boolean b = i.hasExtra("main_activity_call");


        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.intent_meetuproom))) {
            meetups meetup = intent.getParcelableExtra(getString(R.string.intent_meetuproom));
            mMeetups = meetup;

        }
        information_meetupname.setText(mMeetups.getMeetup_title());
        if (mMeetups.getDescription().toString().equals("")){
         description.setVisibility(View.GONE);
        }else {
            description.setText(mMeetups.getDescription());
            description.setVisibility(View.VISIBLE);
        }
        location.setText(mMeetups.getLocation());
        host_name.setText(mMeetups.getHost_name());
        occupation.setText(mMeetups.getHost_occupation());
        information_key.setText(mMeetups.getKey());
        if (!b){
        key_icon.setVisibility(View.VISIBLE);
        key_icon.setVisibility(View.VISIBLE);}else {
            information_key.setVisibility(View.GONE);
            key_icon.setVisibility(View.GONE);
        }

        String date = mMeetups.getDate();
        String day = date.substring(1,2);
        String month = date.substring(3,5);
        int mon = Integer.parseInt(month);
        String year = date.substring(6,10);

        information_date.setText(day+" " + MONTHS[mon-1]+","+year);

        String meetup_timefrom = mMeetups.getTime_from();
        String meetup_timeTill = mMeetups.getTime_till();
        String meetuptimefromhour = meetup_timefrom.substring(0, 2);
        String meetup_timefrommin = meetup_timefrom.substring(3, 5);
        String meetuptimetillhour = meetup_timeTill.substring(0, 2);
        String meetup_timetillmin = meetup_timeTill.substring(3, 5);

        int meetup_timefromhour=Integer.parseInt(meetuptimefromhour);
        int meetup_timetillhour=Integer.parseInt(meetuptimetillhour);
        if (meetup_timefromhour > 12) {
            information_timefrom.setText((meetup_timefromhour - 12) + ":" + meetup_timefrommin + " pm");
        }else
        {
            information_timefrom.setText(meetup_timefromhour + ":" + meetup_timefrommin + " am");
        }
        if (meetup_timetillhour > 12) {
            information_timetill.setText((meetup_timetillhour - 12) + ":" + meetup_timetillmin + " pm");
        }else
        {
            information_timetill.setText(meetup_timetillhour + ":" + meetup_timetillmin + " am");
        }


        information_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b){
                    Intent intent = new Intent(information_Activity.this, Main_Activity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(information_Activity.this, Meetup_activity.class);
                    intent.putExtra(getString(R.string.intent_meetuproom), mMeetups);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
