package com.example.sacoco.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sacoco.MainActivity;
import com.example.sacoco.R;
import com.example.sacoco.viewmodels.BagClothViewModel;

import java.util.Objects;

public class ClothDetailsFragment extends Fragment {
    private BagClothViewModel bagClothViewModel;

    public ClothDetailsFragment() {
        super(R.layout.fragment_show_cloth_details_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bagClothViewModel = new ViewModelProvider(requireActivity()).get(BagClothViewModel.class);

        String clothName = Objects.requireNonNull(bagClothViewModel.getSelectedClothLiveData().
                getValue()).getClothName();
        MainActivity mainActivity = (MainActivity)requireActivity();
        mainActivity.setActivityTitle(clothName);
    }
}
