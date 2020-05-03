package com.maguresoftwares.meetups;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.maguresoftwares.meetups.Models.User;
import com.maguresoftwares.meetups.Models.meetup_questions;
import com.maguresoftwares.meetups.Models.meetups;
import com.maguresoftwares.meetups.Models.user_like;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class newmeetup_Main2Activity extends AppCompatActivity {

    private TextInputLayout title_layout , location_layout , name_layout , occupation_layout;
    private TextInputEditText title , description , location , name , occupation;
    private Button date , time_from , time_till , meetup_create;

    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    private static final Random random = new Random();
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890";

    private int mYear, mMonth, mDay, mHour, mMinute;

    public String meetup_date , meetup_time_from , meetup_time_till;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmeetup);

        title_layout = (TextInputLayout) findViewById(R.id.input_title);
        title = (TextInputEditText) findViewById(R.id.inputtitle);
        location_layout = (TextInputLayout) findViewById(R.id.input_location);
        location = (TextInputEditText) findViewById(R.id.inputlocation);
        name_layout = (TextInputLayout) findViewById(R.id.input_name);
        name = (TextInputEditText) findViewById(R.id.inputname);
        occupation_layout = (TextInputLayout) findViewById(R.id.input_occupation);
        occupation = (TextInputEditText) findViewById(R.id.inputoccupation);
        description = (TextInputEditText) findViewById(R.id.inputdescription);
        date = (Button) findViewById(R.id.date);
        time_from = (Button) findViewById(R.id.time_from);
        time_till = (Button) findViewById(R.id.time_till);
        meetup_create = (Button) findViewById(R.id.meetup_create);
        mProgressBar = (ProgressBar) findViewById(R.id.newmeetup_progressbar);


        final Calendar c1 = Calendar.getInstance();
        mYear = c1.get(Calendar.YEAR);
        mMonth = c1.get(Calendar.MONTH);
        mDay = c1.get(Calendar.DAY_OF_MONTH);

        date.setText("Select Date\n" + mDay + " " + MONTHS[mMonth]);
        if (mDay<10)
        {
            if (mMonth<10){
                meetup_date = "0"+mDay + " 0" + (mMonth+1) + " " + mYear;
            }
            else{
                meetup_date = "0"+mDay + " " + (mMonth+1) + " " + mYear;
            }
        }else {
            if (mMonth<10){
                meetup_date = mDay + " 0" + (mMonth+1) + " " + mYear;
            }
            else{
                meetup_date = mDay + " " + (mMonth+1) + " " + mYear;
            }
        }

        mHour = c1.get(Calendar.HOUR_OF_DAY);
        mMinute = c1.get(Calendar.MINUTE);

        if (mMinute<10) {
            if (mHour > 12) {
                time_from.setText("From\n" + (mHour - 12) + ":0" + mMinute + " pm");
                time_till.setText("Upto\n" + (mHour - 12) + ":0" + mMinute + " pm");
                meetup_time_from = mHour + " 0" + mMinute;
                meetup_time_till = mHour + " 0" + mMinute;
            }else
            {
                time_from.setText("From\n" + mHour + ":0" + mMinute + " am");
                time_till.setText("Upto\n" + mHour + ":0" + mMinute + " am");
                meetup_time_from = mHour + " 0" + mMinute;
                meetup_time_till = mHour + " 0" + mMinute;
            }
        }else{
            if (mHour > 12) {
                time_from.setText("From\n" + (mHour - 12) + ":" + mMinute + " pm");
                time_till.setText("Upto\n" + (mHour - 12) + ":" + mMinute + " pm");
                meetup_time_from = mHour + " " + mMinute;
                meetup_time_till = mHour + " " + mMinute;
            }else
            {
                time_from.setText("From\n" + mHour + ":" + mMinute + " am");
                time_till.setText("Upto\n" + mHour + ":" + mMinute + " am");
                meetup_time_from = mHour + " " + mMinute;
                meetup_time_till = mHour + " " + mMinute;
            }

        }


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showdatedialog();
            }
        });
        time_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showtimedialog();

            }
        });
        time_till.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showtimedialog1();
            }
        });
       meetup_create.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (checkInternetConnection()) {
                   if (validateTitle() && validateLocation() && validatename() && validateoccupation()) {
                       if (validatetime_interval()) {


                           showDialog();
                           DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                           //get the new chatroom unique id
                           String MeetupId = reference
                                   .child("Meetups")
                                   .push().getKey();

                           meetups meetup = new meetups();
                           meetup.setMeetup_title(title.getText().toString());
                           meetup.setLocation(location.getText().toString());
                           meetup.setHost_name(name.getText().toString());
                           meetup.setHost_occupation(occupation.getText().toString());
                           meetup.setCreator_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                           meetup.setKey(getKey(4));
                           meetup.setMeetup_id(MeetupId);
                           meetup.setDate(meetup_date);
                           meetup.setMeetup_rating("0");
                           meetup.setNumber_of_ratings("0");
                           meetup.setTime_from(meetup_time_from);
                           meetup.setTime_till(meetup_time_till);

                           if (description.getText() == null) {
                               meetup.setDescription("");
                           } else {
                               meetup.setDescription(description.getText().toString());
                           }

                           reference
                                   .child("Meetups")
                                   .child(MeetupId)
                                   .setValue(meetup);

                           String question_Id = reference.child("Meetups")
                                   .push().getKey();

                           meetup_questions question = new meetup_questions();

                           question.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                           question.setQuestion("Welcome to the Meetup Room");
                           question.setQuestion_id(question_Id);
                           question.setName(name.getText().toString());
                           reference
                                   .child("Meetups")
                                   .child(MeetupId)
                                   .child("Meetup Questions")
                                   .child(question_Id)
                                   .setValue(question);

                           hideDialog();
                           Intent intent = new Intent(newmeetup_Main2Activity.this, Meetup_activity.class);
                           intent.putExtra(getString(R.string.intent_meetuproom), meetup);
                           intent.putExtra("meetuproom_created", true);
                           startActivity(intent);
                           finish();
                       }else{
                           Snackbar.make(findViewById(android.R.id.content),"Set your time interval correctly",Snackbar.LENGTH_SHORT).show();
                       }
                   }
               }else {
                   Snackbar.make(findViewById(android.R.id.content) , "Check your Network Connection" , Snackbar.LENGTH_SHORT).show();
               }
           }
       });
    }

    private boolean validateTitle(){
        if (TextUtils.isEmpty(title.getText().toString())){
            title_layout.setError("Cannot be blank");
            return false;
        }
        else{
            title_layout.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateLocation(){
        if (TextUtils.isEmpty(location.getText().toString())){
            location_layout.setError("Cannot be blank");
            return false;
        }
        else{
            location_layout.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatename(){
        if (TextUtils.isEmpty(name.getText().toString())){
            name_layout.setError("Cannot be blank");
            return false;
        }
        else{
            name_layout.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateoccupation(){
        if (TextUtils.isEmpty(occupation.getText().toString())){
            occupation_layout.setError("Cannot be blank");
            return false;
        }
        else{
            occupation_layout.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatetime_interval(){
        String meetup_timefromhour = meetup_time_from.substring(0, 2);
        String meetup_timefrommin = meetup_time_from.substring(3, 5);
        String meetup_timetillhour = meetup_time_till.substring(0, 2);
        String meetup_timetillmin = meetup_time_till.substring(3, 5);
            if (Integer.parseInt(meetup_timefromhour) < Integer.parseInt(meetup_timetillhour)) {
                return true;
            } else if (Integer.parseInt(meetup_timefromhour)==Integer.parseInt(meetup_timetillhour)) {
                if (Integer.parseInt(meetup_timefrommin) < Integer.parseInt(meetup_timetillmin)) {
                return true;
                }
            }
            return false;
    }


    public static String getKey(int length) {
        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return token.toString();
    }

    private String getTimestamp(){
        DateFormat sdf = new SimpleDateFormat("dd MM yyyy HH mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        return sdf.format(new Date());
    }

    private void showdatedialog(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        date.setText(dayOfMonth + " " + MONTHS[(monthOfYear)]);
                        if (dayOfMonth<10)
                        {
                            if (monthOfYear<10){
                                meetup_date = "0"+dayOfMonth + " 0" + (monthOfYear+1) + " " + year;
                            }
                            else{
                                meetup_date = "0"+dayOfMonth + " " + (monthOfYear+1) + " " + year;
                            }
                        }else {
                            if (monthOfYear<10){
                                meetup_date = dayOfMonth + " 0" + (monthOfYear+1) + " " + year;
                            }
                            else{
                                meetup_date = dayOfMonth + " " + (monthOfYear+1) + " " + year;
                            }
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showtimedialog(){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        if (minute<10) {
                            if (hourOfDay > 12) {
                                time_from.setText((hourOfDay - 12) + ":0" + minute + " pm");
                            }else
                            {
                                time_from.setText(hourOfDay + ":0" + minute + " am");
                            }
                        }else{
                            if (hourOfDay > 12) {
                                time_from.setText((hourOfDay - 12) + ":" + minute + " pm");
                            }else
                            {
                                time_from.setText(hourOfDay + ":" + minute + " am");
                            }

                        }
                        if (minute<10) {
                            if (hourOfDay >= 10) {
                                meetup_time_from = hourOfDay + " 0" + minute;
                            } else {
                                meetup_time_from = "0" + hourOfDay + " 0" + minute;
                            }
                        } else{
                            if (hourOfDay >= 10) {
                                meetup_time_from = hourOfDay + " " + minute;
                            } else {
                                meetup_time_from = "0" + hourOfDay + " " + minute;
                            }
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
    private void showtimedialog1(){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        time_till.setText(hourOfDay + " " + minute);
                        if (minute<10) {
                            if (hourOfDay >=12) {
                                time_till.setText((hourOfDay - 12) + ":0" + minute + " pm");
                            }else
                            {
                                time_till.setText(hourOfDay + ":0" + minute + " am");
                            }
                        }else{
                            if (hourOfDay >=12) {
                                time_till.setText((hourOfDay - 12) + ":" + minute + " pm");
                            }else
                            {
                                time_till.setText(hourOfDay + ":" + minute + " am");
                            }
                            }
                        if (minute<10) {
                            if (hourOfDay >= 10) {
                                meetup_time_till = hourOfDay + " 0" + minute;
                            } else {
                                meetup_time_till = "0" + hourOfDay + " 0" + minute;
                            }
                        } else{
                            if (hourOfDay >= 10) {
                                meetup_time_till = hourOfDay + " " + minute;
                            } else {
                                meetup_time_till = "0" + hourOfDay + " " + minute;
                            }
                            }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private boolean checkInternetConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void onStart() {
        super.onStart();
        if (!checkInternetConnection()) {
            Snackbar.make(findViewById(android.R.id.content) , "Check your Network Connection" , Snackbar.LENGTH_LONG).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                }
            }, 3000);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(newmeetup_Main2Activity.this,Main2Activity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
    }


