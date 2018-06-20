package com.maguresoftwares.meetups;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maguresoftwares.meetups.Models.meetups;

import java.util.ArrayList;

public class keys_bottom_sheet extends BottomSheetDialogFragment{

    private ArrayList<String> Keys;
    private ArrayList<meetups> mMeetups;

    private TextView key_error;
    private TextInputEditText code;
    private FloatingActionButton check_key;

    private RelativeLayout keys_relativelayout;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        final View contentView = View.inflate(getContext(), R.layout.activity_keys_bottom_sheet, null);
        dialog.setContentView(contentView);

        code = (TextInputEditText) contentView.findViewById(R.id.code);
        check_key = (FloatingActionButton) contentView.findViewById(R.id.check_key);
        keys_relativelayout = (RelativeLayout) contentView.findViewById(R.id.keys_relativelayout);
        key_error = (TextView) contentView.findViewById(R.id.key_error);

        Keys = new ArrayList<>();
        mMeetups = new ArrayList<>();



        check_key.clearFocus();
        Bundle bundle = this.getArguments();
        Keys = bundle.getStringArrayList("key_list");
        mMeetups = bundle.getParcelableArrayList("meetups_list");

        check_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String input_key = code.getText().toString();

                    int i;
                    int size = Keys.size();

                    for (i = 0; i < size; i++) {
                        if (input_key.equals(Keys.get(i))) {
                            hideSoftKeyboard();
                            key_error.setText("");

                            Intent intent = new Intent(getActivity(), Meetup_activity.class);
                            intent.putExtra(getString(R.string.intent_meetuproom), mMeetups.get(i));
                            startActivity(intent);
                        }
                        else{
                            hideSoftKeyboard();

                        }
                    }
                    hideSoftKeyboard();
                    code.setText("");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        key_error.setText(getString(R.string.key_error));
                    }
                }, 500);

            }

        });

    }
    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(keys_relativelayout.getWindowToken(), 0);
    }

}
