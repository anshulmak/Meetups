package com.maguresoftwares.meetups;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Float.NaN;

public class Meetup_activity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mQuestionReference;

    private TextView meetuproom_name ;
    private ListView mListView;
    private TextInputEditText input_question;
    private FloatingActionButton send_question;
    private String key_successful;
    private ImageButton information , back , rating;

    private boolean check_speakerrating , rating_once;

    private String meetup_timefrom , meetup_timetill , meetup_Date , sr,nor;
    private String check_creatorid;

    private meetups mMeetups;
    private List<meetup_questions> mQuestionlist;
    private questions_listadapter mAdapter;

    Runnable r2=new Runnable() {
        @Override
        public void run() {
            if (rating_once) {
                show_ratingbar();
            }
            h2.postDelayed(r2,1000);
        }
    };

    Handler h2=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup);
        meetuproom_name = (TextView) findViewById(R.id.text_meetuproom_name);

        mListView = (ListView) findViewById(R.id.listView);
        input_question = (TextInputEditText) findViewById(R.id.input_question);
        send_question = (FloatingActionButton) findViewById(R.id.send_question);
        information = (ImageButton) findViewById(R.id.information);
        back = (ImageButton) findViewById(R.id.meetup_back);
        rating = (ImageButton) findViewById(R.id.rating);

        mQuestionlist = new ArrayList<>();

        setupFirebaseAuth();
        getMeetuproom();
        hideSoftKeyboard();

        rating_once = true;



        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (check_creatorid.equals(user.getUid()))
        {
            input_question.setVisibility(View.INVISIBLE);
            send_question.setVisibility(View.INVISIBLE);
        }

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showrating();
            }
        });



        Intent intent = getIntent();
        Boolean meetup_justcreated = intent.hasExtra("meetuproom_created");

        if (meetup_justcreated)
        {
            Bundle b = new Bundle();
            b.putString("key_successful", key_successful);
            BottomSheetDialogFragment bottomSheetDialogFragment = new bottom_sheet();
            bottomSheetDialogFragment.setArguments(b);
            bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        }

        input_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.setSelection(mAdapter.getCount()-1);
            }
        });

        send_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!input_question.getText().toString().equals("")) {
                    String Question = input_question.getText().toString();


                    meetup_questions question = new meetup_questions();
                    question.setQuestion(Question);
                    question.setTimestamp(getTimestamp());
                    question.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    question.setNumber_Of_likes("0");

                    String question_time = getTimestamp();
                    String question_date = question_time.substring(0, 10);
                    String question_timehour = question_time.substring(11, 13);
                    String question_timemin = question_time.substring(14, 16);
                    String meetup_timefromhour = meetup_timefrom.substring(0, 2);
                    String meetup_timefrommin = meetup_timefrom.substring(3, 5);
                    String meetup_timetillhour = meetup_timetill.substring(0, 2);
                    String meetup_timetillmin = meetup_timetill.substring(3, 5);
                    if (question_date.equals(meetup_Date)) {

                        if (Integer.parseInt(question_timehour) < Integer.parseInt(meetup_timetillhour) && Integer.parseInt(meetup_timefromhour) < Integer.parseInt(question_timehour)) {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                    .child("Meetups")
                                    .child(mMeetups.getMeetup_id())
                                    .child("Meetup Questions");

                            String newQuestionid = reference.push().getKey();

                            question.setQuestion_id(newQuestionid);
                            reference.child(newQuestionid)
                                    .setValue(question);

                            input_question.setText("");
                            input_question.setFocusableInTouchMode(false);
                            hideSoftKeyboard();

                        } else if (Integer.parseInt(question_timehour)==Integer.parseInt(meetup_timetillhour)) {

                            if (Integer.parseInt(question_timemin) < Integer.parseInt(meetup_timetillmin)) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                        .child("Meetups")
                                        .child(mMeetups.getMeetup_id())
                                        .child("Meetup Questions");

                                String newQuestionid = reference.push().getKey();

                                question.setQuestion_id(newQuestionid);
                                reference.child(newQuestionid)
                                        .setValue(question);

                                input_question.setText("");
                                hideSoftKeyboard();
                            } else {
                                hideSoftKeyboard();
                                Snackbar.make(findViewById(android.R.id.content), "Meetup is not Active", Snackbar.LENGTH_LONG).show();
                                input_question.setText("");
                                input_question.setFocusable(false);
                                input_question.setClickable(false);
                            }

                        } else if (Integer.parseInt(meetup_timefromhour)==Integer.parseInt(question_timehour)) {

                            if (Integer.parseInt(meetup_timefrommin)< Integer.parseInt(question_timemin)) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                        .child("Meetups")
                                        .child(mMeetups.getMeetup_id())
                                        .child("Meetup Questions");

                                String newQuestionid = reference.push().getKey();

                                question.setQuestion_id(newQuestionid);
                                reference.child(newQuestionid)
                                        .setValue(question);

                                input_question.setText("");
                                hideSoftKeyboard();
                            } else {
                                hideSoftKeyboard();
                                Snackbar.make(findViewById(android.R.id.content), "Meetup is not Active", Snackbar.LENGTH_LONG).show();
                                input_question.setText("");
                                input_question.setFocusable(false);
                                input_question.setClickable(false);

                            }

                        }else
                        {
                            hideSoftKeyboard();
                            Snackbar.make(findViewById(android.R.id.content), "Meetup is not Active", Snackbar.LENGTH_LONG).show();
                            input_question.setText("");
                            input_question.setFocusable(false);
                            input_question.setClickable(false);

                        }
                    }else{
                        hideSoftKeyboard();
                        Snackbar.make(findViewById(android.R.id.content), "Meetup is not Active", Snackbar.LENGTH_LONG).show();
                        input_question.setText("");
                        input_question.setFocusable(false);
                        input_question.setClickable(false);
                    }
                    hideSoftKeyboard();
                }
                hideSoftKeyboard();
            }
        });

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Meetup_activity.this , information_Activity.class);
                intent.putExtra(getString(R.string.intent_meetuproom),mMeetups);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Meetup_activity.this,Main2Activity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void getMeetuproomQuestions(){
        mQuestionlist = new ArrayList<>();
        if (mQuestionlist.size() >0){
            mQuestionlist.clear();
            mAdapter.clear();
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Meetups")
                .child(mMeetups.getMeetup_id())
                .child("Meetup Questions");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    DataSnapshot snapshot = singleSnapshot;

                    try {
                        meetup_questions question = new meetup_questions();
                        String userId = snapshot.getValue(meetup_questions.class).getUser_id();
                        if(userId != null){
                            question.setQuestion(snapshot.getValue(meetup_questions.class).getQuestion());
                            question.setUser_id(snapshot.getValue(meetup_questions.class).getUser_id());
                            question.setTimestamp(snapshot.getValue(meetup_questions.class).getTimestamp());
                            question.setNumber_Of_likes(snapshot.getValue(meetup_questions.class).getNumber_Of_likes());
                            question.setQuestion_id(snapshot.getValue(meetup_questions.class).getQuestion_id());

                            ArrayList<user_like>likeList = new ArrayList<user_like>();
                            for (DataSnapshot snap_shot : singleSnapshot.child(getString(R.string.user_likes)).getChildren()) {

                                user_like userlike = new user_like();
                                userlike.setUser_id((String) snap_shot.getValue(user_like.class).getUser_id());
                                userlike.setCheck_like((boolean) snap_shot.getValue(user_like.class).isCheck_like());
                                likeList.add(userlike);
                            }
                            question.setUser_likes(likeList);
                            mQuestionlist.add(question);
                        }else{
                            question.setQuestion(snapshot.getValue(meetup_questions.class).getQuestion());
                            question.setTimestamp(snapshot.getValue(meetup_questions.class).getTimestamp());
                            mQuestionlist.add(question);
                        }

                    } catch (NullPointerException e) {

                    }
                }
                getUserDetails();
                inQuestionList();
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getUserDetails(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        for (int i = 0 ; i<mQuestionlist.size() ; i++){
            final int j=i;
            if (mQuestionlist.get(i).getUser_id() !=null){
                String userId= mQuestionlist.get(i).getUser_id();
                Query query = reference.child("users")
                        .orderByKey().equalTo(userId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                            mQuestionlist.get(j).setName((String) singleSnapshot.getValue(User.class).getName());
                            mAdapter.notifyDataSetChanged();

                        }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    private void inQuestionList(){

        mAdapter = new questions_listadapter(Meetup_activity.this,R.layout.listview_question,mQuestionlist,mMeetups.getMeetup_id(),mMeetups.getCreator_id());
        mAdapter.notifyDataSetChanged();
        mListView.setAdapter(mAdapter);
        mListView.setSelection(mAdapter.getCount()-1);
        }

    private void getMeetuproom(){

        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.intent_meetuproom))){
            meetups meetup = intent.getParcelableExtra(getString(R.string.intent_meetuproom));
            mMeetups = meetup;
            meetuproom_name.setText(mMeetups.getMeetup_title());
            check_creatorid = mMeetups.getCreator_id();
            key_successful = mMeetups.getKey();


            meetup_timefrom = mMeetups.getTime_from();
            meetup_timetill = mMeetups.getTime_till();
            meetup_Date = mMeetups.getDate();

            enableMeetuproomListener();
        }
    }

    private void hideSoftKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void enableMeetuproomListener(){

        mQuestionReference = FirebaseDatabase.getInstance().getReference().child("Meetups")
                .child(mMeetups.getMeetup_id())
                .child("Meetup Questions");

        mQuestionReference.addValueEventListener(mValueEventListener);
    }

    private String getTimestamp(){
        DateFormat ist = new SimpleDateFormat("dd MM yyyy HH mm");
        ist.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        return ist.format(new Date());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mQuestionReference.removeEventListener(mValueEventListener);
    }

    ValueEventListener mValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            getMeetuproomQuestions();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
       h2.postDelayed(r2,1000);
    }

    private void checkAuthenticationState(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){


            Intent intent = new Intent(Meetup_activity.this, Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
        }
    }

    private void setupFirebaseAuth(){

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Intent intent = new Intent(Meetup_activity.this, Login_Activity.class);
                    startActivity(intent);
                    finish();
                }
                // ...
            }
        };

    }

    @Override
    public void onPause(){
        super.onPause();
        h2.removeCallbacks(r2);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        rating_once=true;
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onBackPressed() {
        Intent i=new Intent(Meetup_activity.this,Main2Activity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    private void show_ratingbar() {

        String question_time = getTimestamp();
        String question_date = question_time.substring(0, 10);
        String question_timehour = question_time.substring(11, 13);
        String question_timemin = question_time.substring(14, 16);
        String meetup_timefromhour = meetup_timefrom.substring(0, 2);
        String meetup_timefrommin = meetup_timefrom.substring(3, 5);
        String meetup_timetillhour = meetup_timetill.substring(0, 2);
        String meetup_timetillmin = meetup_timetill.substring(3, 5);

        if (!check_creatorid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            if (question_date.equals(meetup_Date)) {

                if (Integer.parseInt(question_timehour) > Integer.parseInt(meetup_timetillhour)) {

                    final Dialog dialog = new Dialog(this);
                    dialog.setContentView(R.layout.rating_dialog);
                    final RatingBar meetup_rating = (RatingBar) dialog.findViewById(R.id.meetup_rating);
                    Button submit_rating = (Button) dialog.findViewById(R.id.submit_rating);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;


                    dialog.getWindow().setAttributes(lp);

                    submit_rating.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Float rating = meetup_rating.getRating();
                            addrating(rating);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    rating_once = false;

                } else if (Integer.parseInt(question_timehour) == Integer.parseInt(meetup_timetillhour)) {

                    if (Integer.parseInt(question_timemin) >= Integer.parseInt(meetup_timetillmin)) {

                        final Dialog dialog = new Dialog(this);
                        dialog.setContentView(R.layout.rating_dialog);
                        final RatingBar meetup_rating = (RatingBar) dialog.findViewById(R.id.meetup_rating);
                        Button submit_rating = (Button) dialog.findViewById(R.id.submit_rating);

                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.gravity = Gravity.CENTER;

                        dialog.getWindow().setAttributes(lp);
                        submit_rating.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Float rating = meetup_rating.getRating();
                                addrating(rating);

                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        rating_once = false;
                    }
                }
            } else {

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.rating_dialog);
                final RatingBar meetup_rating = (RatingBar) dialog.findViewById(R.id.meetup_rating);
                Button submit_rating = (Button) dialog.findViewById(R.id.submit_rating);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp);
                submit_rating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Float rating = meetup_rating.getRating();
                        addrating(rating);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                rating_once = false;
            }
    }
    }
    private void addrating(final Float rate){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Meetups")
                .orderByKey().equalTo(mMeetups.getMeetup_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                sr =(String) singleSnapshot.getValue(meetups.class).getMeetup_rating();
                nor =(String) singleSnapshot.getValue(meetups.class).getNumber_of_ratings();

                int numratings = Integer.parseInt(nor);
                float rating = Float.parseFloat(sr);

                int noratings = numratings + 1;
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Meetups").child(mMeetups.getMeetup_id());
                reference1.child("meetup_rating").setValue(Float.toString(rating+rate));
                reference1.child("number_of_ratings").setValue(Integer.toString(noratings));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showrating(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.meetup_rating_dialog);
        final RatingBar speaker_meetuprating = (RatingBar) dialog.findViewById(R.id.speaker_meetuprating);
        final TextView rating_meetuptitle = (TextView) dialog.findViewById(R.id.rating_meetuptitle);
        final TextView speaker_rating = (TextView) dialog.findViewById(R.id.speaker_rating);
        final TextView number_of_ratings = (TextView) dialog.findViewById(R.id.number_of_ratings);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Meetups")
                                .orderByKey().equalTo(mMeetups.getMeetup_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                sr =(String) singleSnapshot.getValue(meetups.class).getMeetup_rating();
                nor =(String) singleSnapshot.getValue(meetups.class).getNumber_of_ratings();

                int numratings = Integer.parseInt(nor);

                float rating = Float.parseFloat(sr);

                float f = (float) numratings;
                float meetuprating = rating / f;
                if (f==0.0f && rating==0.0f){
                    speaker_rating.setText("0");
                }else {
                    String mr = String.format(java.util.Locale.US,"%.1f",meetuprating);
                    speaker_rating.setText(mr);
                }
                number_of_ratings.setText((String) singleSnapshot.getValue(meetups.class).getNumber_of_ratings());
                speaker_meetuprating.setRating(meetuprating);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rating_meetuptitle.setText(mMeetups.getMeetup_title());


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }
}

