package com.example.sacoco.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sacoco.MainActivity;
import com.example.sacoco.R;
import com.example.sacoco.adapter.ClothItemAdapter;
import com.example.sacoco.cominterface.ViewHolderSelectedCallback;
import com.example.sacoco.viewmodels.BagClothViewModel;

import java.util.Objects;

public class BagDetailsFragment extends Fragment implements ViewHolderSelectedCallback {
    private BagClothViewModel bagClothViewModel;
    private RecyclerView clothesInBagRecyclerView;

    public BagDetailsFragment() {
        super(R.layout.fragment_bag_content_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bagClothViewModel = new ViewModelProvider(requireActivity()).get(BagClothViewModel.class);

        int bagWeekNumber = Objects.requireNonNull(bagClothViewModel.getSelectedBagLiveData().
                getValue()).getWeekNumber();
        String bagContentTitle = this.getString(R.string.fragment_bag_content_title);
        MainActivity mainActivity = (MainActivity)requireActivity();
        mainActivity.setActivityTitle(String.format(bagContentTitle, bagWeekNumber));

        clothesInBagRecyclerView = view.findViewById(R.id.bagContentListView);
        clothesInBagRecyclerView.setAdapter(new ClothItemAdapter(this));
        clothesInBagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ClothItemAdapter clothItemAdapter = (ClothItemAdapter)clothesInBagRecyclerView.getAdapter();
        Objects.requireNonNull(clothItemAdapter).setClothesInBagList(bagClothViewModel.
                getSelectedBagLiveData().getValue().getClothesList());
    }

    @Override
    public void onPositiveViewHolderSelected(int viewHolderSelectedIndex) {

    }

    @Override
    public void onNegativeViewHolderSelected(int viewHolderSelectedIndex) {

    }
}
