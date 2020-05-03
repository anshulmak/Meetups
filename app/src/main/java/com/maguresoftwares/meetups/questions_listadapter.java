package com.maguresoftwares.meetups;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.maguresoftwares.meetups.Models.meetup_questions;
import com.maguresoftwares.meetups.Models.user_like;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


public class questions_listadapter extends ArrayAdapter<meetup_questions> {

    private int mLayoutResource , c;
    private Context mContext;
    private ArrayList<user_like> likeList;
    private String meetupid, creatorid ;



   public questions_listadapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<meetup_questions> objects , @NonNull String meetup_id, @NonNull String creator_id ) {
        super(context, resource, objects);
        mContext = context;
        mLayoutResource = resource;
        meetupid = meetup_id;
        creatorid=creator_id;
    }
    public static class ViewHolder{
        TextView name, question , number_of_likes;
        ToggleButton likes , highlight;
        RelativeLayout item_layout;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mLayoutResource, parent, false);
            holder = new ViewHolder();

            holder.item_layout = (RelativeLayout) convertView.findViewById(R.id.listViewitem_background);
            holder.name = (TextView) convertView.findViewById(R.id.user_name);
            holder.question = (TextView) convertView.findViewById(R.id.questions);
            holder.number_of_likes = (TextView) convertView.findViewById(R.id.number_of_likes);
            holder.likes = (ToggleButton) convertView.findViewById(R.id.like_button);
            holder.highlight = (ToggleButton) convertView.findViewById(R.id.highlight_button);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
            holder.name.setText("");
            holder.question.setText("");
            holder.number_of_likes.setText("");
            holder.likes.setClickable(false);
            holder.highlight.setVisibility(View.INVISIBLE);
        }

        try{

            if (position==0){
                holder.likes.setVisibility(View.INVISIBLE);
                holder.highlight.setVisibility(View.INVISIBLE);
            }

            holder.question.setText(getItem(position).getQuestion());
            holder.name.setText(getItem(position).getName());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference
                    .child("Meetups")
                    .child(meetupid)
                    .child("Meetup Questions")
                    .child(getItem(position).getQuestion_id())
                    .child("Likes");

            likeList = new ArrayList<user_like>();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    c = (int) dataSnapshot.getChildrenCount();
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                        user_like userlike = singleSnapshot.getValue(user_like.class);
                        likeList.add(userlike);

                        if (singleSnapshot.getValue(user_like.class).getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                            boolean like = (boolean) singleSnapshot.getValue(user_like.class).isCheck_like();
                            if (like) {
                                holder.likes.setChecked(like);

                        }else{
                                holder.likes.setChecked(like);
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                        .child("Meetups")
                                        .child(meetupid)
                                        .child("Meetup Questions")
                                        .child(getItem(position).getQuestion_id());
                                reference
                                        .child("Likes")
                                        .child(singleSnapshot.getValue(user_like.class).getUser_id())
                                        .setValue(null);
                            }
                        }
                    }
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference()
                            .child("Meetups")
                            .child(meetupid)
                            .child("Meetup Questions")
                            .child(getItem(position).getQuestion_id());
                    reference1.child("number_Of_likes").setValue(Integer.toString(c));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            String no_oflikes= getItem(position).getNumber_Of_likes();
            if (!no_oflikes.equals("0")) {
                holder.number_of_likes.setText(no_oflikes);
            }else {
                holder.number_of_likes.setText("");
            }

            if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(creatorid)) {
                holder.highlight.setVisibility(View.INVISIBLE);
                holder.likes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (holder.likes.isChecked()) {

                           DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                    .child("Meetups")
                                    .child(meetupid)
                                    .child("Meetup Questions")
                                    .child(getItem(position).getQuestion_id());

                            user_like userlike = new user_like();

                            userlike.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            userlike.setCheck_like(true);

                            reference
                                    .child("Likes")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(userlike);

                        } else {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                    .child("Meetups")
                                    .child(meetupid)
                                    .child("Meetup Questions")
                                    .child(getItem(position).getQuestion_id());

                            user_like userlike = new user_like();

                            userlike.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            userlike.setCheck_like(false);

                            reference
                                    .child("Likes")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(userlike);
                        }

                    }
                });
            }else
            {
                holder.highlight.setVisibility(View.VISIBLE);
                holder.highlight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.highlight.isChecked()){
                            holder.highlight.setChecked(true);
                            //holder.item_layout.setBackgroundColor(getContext().getColor(R.color.highlight));
                            holder.item_layout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.highlight));
                        }else{
                            holder.highlight.setChecked(false);
                            holder.item_layout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.grey));
                        }
                    }
                });
            }

        }catch (NullPointerException e){
        }

        return convertView;
    }
}
