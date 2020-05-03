package com.maguresoftwares.meetups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.maguresoftwares.meetups.Models.meetup_questions;
import com.maguresoftwares.meetups.Models.meetups;
import com.maguresoftwares.meetups.Models.user_like;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class Main_Activity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference Mmeetupsreference;

    private ArrayList<meetups> mMeetups;
    private List<meetups> Mmeetups;
    private ListView listview;
    private Customadapter_mainactivity customAdapter;

    private TextView text_nomeetups , nomeetups_create;

    private ArrayList<String> Keys;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("");
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.baseline_more_vert_black_24));
        setSupportActionBar(toolbar);

        text_nomeetups = (TextView) findViewById(R.id.text_nomeetups);
        nomeetups_create = (TextView) findViewById(R.id.nomeetups_create);
        mProgressBar = (ProgressBar) findViewById(R.id.main_progressbar);
        listview = (ListView) findViewById(R.id.mainactivity_listview);

        showDialog();
         Timer t = new Timer();

        t.scheduleAtFixedRate(
                new TimerTask()
                {
                    public void run()
                    {
                        checkInternetConnection();
                    }
                },
                0,
                2000);


        nomeetups_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this , newmeetup_Main2Activity.class);
                startActivity(intent);
            }
        });

        if (checkInternetConnection()) {
            setupFirebaseAuth();
            }
        enableMeetupListener();

    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
    }
    private void setupFirebaseAuth(){

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {


                } else {

                    Intent intent = new Intent(Main_Activity.this, Login_Activity.class);
                    startActivity(intent);
                    finish();
                }

            }
        };
    }

    private void checkAuthenticationState(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){


            Intent intent = new Intent(Main_Activity.this, Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (checkInternetConnection()) {
            FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
        }else {
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
    protected void onResume() {
        super.onResume();
        if (checkInternetConnection()){
        checkAuthenticationState();}else {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null && checkInternetConnection()) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    private void getMeetups(){
        mMeetups = new ArrayList<>();

        Mmeetups = new ArrayList<>();
        if (Mmeetups.size() >0){
            Mmeetups.clear();
            Mmeetups.clear();
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        final Query query = reference.child("Meetups");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                    meetups meetup = new meetups();

                    meetup.setMeetup_title((String)singleSnapshot.getValue(meetups.class).getMeetup_title());
                    meetup.setCreator_id((String)singleSnapshot.getValue(meetups.class).getCreator_id());
                    meetup.setKey((String)singleSnapshot.getValue(meetups.class).getKey());
                    meetup.setMeetup_id((String)singleSnapshot.getValue(meetups.class).getMeetup_id());
                    meetup.setDate((String)singleSnapshot.getValue(meetups.class).getDate());
                    meetup.setTime_from((String)singleSnapshot.getValue(meetups.class).getTime_from());
                    meetup.setTime_till((String)singleSnapshot.getValue(meetups.class).getTime_till());
                    meetup.setDescription((String)singleSnapshot.getValue(meetups.class).getDescription());
                    meetup.setLocation((String)singleSnapshot.getValue(meetups.class).getLocation());
                    meetup.setHost_name((String)singleSnapshot.getValue(meetups.class).getHost_name());
                    meetup.setMeetup_rating((String)singleSnapshot.getValue(meetups.class).getMeetup_rating());
                    meetup.setNumber_of_ratings((String)singleSnapshot.getValue(meetups.class).getNumber_of_ratings());
                    meetup.setHost_occupation((String)singleSnapshot.getValue(meetups.class).getHost_occupation());



                    ArrayList<meetup_questions>questionList = new ArrayList<meetup_questions>();
                    for (DataSnapshot snapshot: singleSnapshot.child(getString(R.string.meetup_question)).getChildren()){
                        meetup_questions question = new meetup_questions();
                        question.setTimestamp((String)snapshot.getValue(meetup_questions.class).getTimestamp());
                        question.setUser_id((String)snapshot.getValue(meetup_questions.class).getUser_id());
                        question.setQuestion((String)snapshot.getValue(meetup_questions.class).getQuestion());
                        question.setNumber_Of_likes((String)snapshot.getValue(meetup_questions.class).getNumber_Of_likes());
                        question.setQuestion_id((String)snapshot.getValue(meetup_questions.class).getQuestion_id());

                        ArrayList<user_like>likeList = new ArrayList<user_like>();
                        for (DataSnapshot snap_shot: snapshot.child(getString(R.string.user_likes)).getChildren()) {

                            user_like userlike = new user_like();
                            userlike.setUser_id((String) snap_shot.getValue(user_like.class).getUser_id());
                            userlike.setCheck_like((boolean) snap_shot.getValue(user_like.class).isCheck_like());
                            likeList.add(userlike);
                        }
                        question.setUser_likes(likeList);
                        questionList.add(question);

                    }
                    meetup.setMeetup_question(questionList);

                   mMeetups.add(meetup);
                }
                Keys = new ArrayList<>();
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                Query query1 = reference1.child("Meetups");
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            Keys.add((String)singleSnapshot.getValue(meetups.class).getKey());
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Snackbar.make(findViewById(android.R.id.content),"Check Your Internet Connection",Snackbar.LENGTH_LONG).show();
                    }
                });
                getmeetupslist();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(findViewById(android.R.id.content),"Check Your Internet Connection",Snackbar.LENGTH_LONG).show();
            }
        });
        getmeetupslist();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.join_themeetup:
                Bundle b = new Bundle();
                b.putStringArrayList("key_list",Keys);
                b.putParcelableArrayList("meetups_list",mMeetups);
                BottomSheetDialogFragment bottomSheetDialogFragment = new keys_bottom_sheet();
                bottomSheetDialogFragment.setArguments(b);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                return true;
            case R.id.create_meetup:
                Intent intent = new Intent(Main_Activity.this , newmeetup_Main2Activity.class);
                startActivity(intent);
                return true;
            case R.id.signout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public void onBackPressed() {
        Intent i=new Intent(Main_Activity.this,Main2Activity.class);
        startActivity(i);

        super.onBackPressed();
    }
    private void enableMeetupListener(){

       Mmeetupsreference = FirebaseDatabase.getInstance().getReference().child("Meetups");

        Mmeetupsreference.addValueEventListener(mValueEventListener);
    }
    ValueEventListener mValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            getMeetups();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void getmeetupslist(){

        modifymeetupslist();

        customAdapter = new Customadapter_mainactivity(Main_Activity.this,R.layout.recyclerview_item, Mmeetups , mMeetups,Keys);
        customAdapter.notifyDataSetChanged();
        listview.setAdapter(customAdapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Main_Activity.this , information_Activity.class);
                intent.putExtra(getString(R.string.intent_meetuproom),Mmeetups.get(position));
                intent.putExtra("main_activity_call",true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Mmeetupsreference.removeEventListener(mValueEventListener);
    }

    private String getTimestamp(){
        DateFormat sdf = new SimpleDateFormat("dd MM yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        return sdf.format(new Date());
    }
    private void modifymeetupslist(){
        String currentdate_year = getTimestamp();
        String currentdate = currentdate_year.substring(0,5);
        String currentyear = currentdate_year.substring(6,10);
        for (int i=0 ; i<mMeetups.size();i++){
            String meetupdate = mMeetups.get(i).getDate().substring(0,5);
            String meetupyear = mMeetups.get(i).getDate().substring(6,10);
            if (Integer.parseInt(meetupyear)>Integer.parseInt(currentyear)){
                Mmeetups.add(mMeetups.get(i));
            }else if (Integer.parseInt(meetupyear)==Integer.parseInt(currentyear)){
                String currentmonth = currentdate.substring(3,5);
                String currentday = currentdate.substring(0,2);
                String meetupmonth = meetupdate.substring(3,5);
                String meetupday = meetupdate.substring(0,2);
                if (Integer.parseInt(currentmonth)<Integer.parseInt(meetupmonth)){
                    Mmeetups.add(mMeetups.get(i));
                }else if (Integer.parseInt(currentmonth)==Integer.parseInt(meetupmonth)){
                    if (Integer.parseInt(currentday)<=Integer.parseInt(meetupday)){
                        Mmeetups.add(mMeetups.get(i));
                    }
                }
            }
        }
        hideDialog();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Mmeetups.size()==0) {
                    listview.setVisibility(View.INVISIBLE);
                    nomeetups_create.setVisibility(View.VISIBLE);
                    text_nomeetups.setVisibility(View.VISIBLE);

                }else {
                    Collections.sort(Mmeetups, new Comparator<meetups>() {
                        @Override
                        public int compare(meetups o1, meetups o2) {

                            String o1date = o1.getDate().substring(0, 5);
                            String o1year = o1.getDate().substring(6, 10);
                            String o2date = o2.getDate().substring(0, 5);
                            String o2year = o2.getDate().substring(6, 10);
                            if (o1year.equals(o2year)) {
                                return o1date.compareTo(o2date);
                            }
                            return o1year.compareTo(o2year);
                        }
                    });
                    listview.setVisibility(View.VISIBLE);
                    nomeetups_create.setVisibility(View.INVISIBLE);
                    text_nomeetups.setVisibility(View.INVISIBLE);

                }
            }
        }, 500);

    }
}
