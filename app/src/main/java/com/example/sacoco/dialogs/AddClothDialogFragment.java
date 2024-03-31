package com.example.sacoco.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.sacoco.R;
import com.example.sacoco.viewmodels.BagClothViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class AddClothDialogFragment extends DialogFragment {
    private BagClothViewModel bagClothViewModel;

    public AddClothDialogFragment() {
        super(R.layout.dialog_add_cloth_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.bagClothViewModel = new ViewModelProvider(requireActivity()).get(BagClothViewModel.class);

        ImageButton closeAddClothDialogFragment = view.findViewById(R.id.closeAddClothDialog);
        closeAddClothDialogFragment.setOnClickListener(view1 -> this.dismiss());

        ShapeableImageView clothImageView = view.findViewById(R.id.clothImageView);
        Glide.with(this.requireContext())
                .load(this.bagClothViewModel.getClothImageTemp())
                .into(clothImageView);

        TextInputLayout clothUUIDTextView = view.findViewById(R.id.identifierTextInputLayout);
        Objects.requireNonNull(clothUUIDTextView.getEditText()).setText(
                this.bagClothViewModel.getClothInCreation().getClothUUID().toString());
    }
}
