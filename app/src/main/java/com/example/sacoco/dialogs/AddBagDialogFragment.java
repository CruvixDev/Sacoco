package com.example.sacoco.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.sacoco.R;

public class AddBagDialogFragment extends DialogFragment {
    public AddBagDialogFragment() {
        super(R.layout.dialog_add_bag_layout);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
