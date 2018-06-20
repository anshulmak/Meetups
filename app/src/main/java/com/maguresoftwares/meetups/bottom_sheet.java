package com.maguresoftwares.meetups;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.TextView;

public class bottom_sheet extends BottomSheetDialogFragment {

    private String successful_key;
    private TextView key_successful;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.meetupactivity_bottomsheet, null);
        dialog.setContentView(contentView);
        key_successful = (TextView) contentView.findViewById(R.id.key_bottomsheet);
        Bundle bundle = this.getArguments();
        successful_key = bundle.getString("key_successful");
        key_successful.setText(successful_key);
    }
}
