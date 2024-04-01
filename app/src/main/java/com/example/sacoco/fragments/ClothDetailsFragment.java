package com.example.sacoco.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.sacoco.MainActivity;
import com.example.sacoco.R;
import com.example.sacoco.models.ClothTypeEnum;
import com.example.sacoco.viewmodels.BagClothViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClothDetailsFragment extends Fragment {

    public ClothDetailsFragment() {
        super(R.layout.fragment_show_cloth_details_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BagClothViewModel bagClothViewModel = new ViewModelProvider(requireActivity()).get(BagClothViewModel.class);

        String clothName = Objects.requireNonNull(bagClothViewModel.getSelectedClothLiveData().
                getValue()).getClothName();

        String[] stringArray = getResources().getStringArray(R.array.items_array);
        ClothTypeEnum[] clothTypeEnums = ClothTypeEnum.values();
        Map<ClothTypeEnum, String> clothTypeEnumStringMap = new HashMap<>();

        if (clothTypeEnums.length == stringArray.length) {
            for (int i = 0; i < clothTypeEnums.length; i++) {
                clothTypeEnumStringMap.put(clothTypeEnums[i], stringArray[i]);
            }

            MainActivity mainActivity = (MainActivity)requireActivity();
            mainActivity.setActivityTitle(clothName);

            TextInputLayout clothNameEditText = view.findViewById(R.id.clothDetailsNameTextInputLayout);
            Objects.requireNonNull(clothNameEditText.getEditText()).setText(clothName);

            TextInputLayout clothTypeEditText = view.findViewById(R.id.clothTypeTextInputLayout);
            Objects.requireNonNull(clothTypeEditText.getEditText()).setText(clothTypeEnumStringMap.
                    get(bagClothViewModel.getSelectedClothLiveData().getValue().getClothType()));

            ShapeableImageView clothImageView = view.findViewById(R.id.clothDetailsImageView);
            Glide.with(this.requireContext())
                    .load(bagClothViewModel.getClothImageBitmap(bagClothViewModel.
                            getSelectedClothLiveData().getValue().getClothUUID()))
                    .into(clothImageView);
        }
        else {
            Log.e(this.getClass().getName(), "ClothType enum length and string array length" +
                    "mismatch!");
        }
    }
}
