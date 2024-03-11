package com.example.sacoco.dialogs;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sacoco.R;
import com.example.sacoco.adapter.ClothItemAdapter;
import com.example.sacoco.cominterface.ViewHolderSelectedCallback;
import com.example.sacoco.models.Cloth;
import com.example.sacoco.viewmodels.BagClothViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class AddClothToBagDialogFragment extends DialogFragment implements ViewHolderSelectedCallback {
    private BagClothViewModel bagClothViewModel;

    public AddClothToBagDialogFragment() {
        super(R.layout.dialog_add_cloth_to_bag_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.bagClothViewModel = new ViewModelProvider(requireActivity()).get(BagClothViewModel.class);

        RecyclerView addClothesToBagRecyclerView = view.findViewById(R.id.addClothesToBagRecyclerView);
        addClothesToBagRecyclerView.setAdapter(new ClothItemAdapter(this));
        addClothesToBagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ClothItemAdapter clothItemAdapter = (ClothItemAdapter)addClothesToBagRecyclerView.getAdapter();
        if (clothItemAdapter != null) {
            ArrayList<Cloth> clothesArrayList = new ArrayList<>(
                    Objects.requireNonNull(this.bagClothViewModel.getClothesLiveData().getValue()));

            clothesArrayList.removeAll(Objects.requireNonNull(
                    this.bagClothViewModel.getSelectedBagLiveData().getValue()).getClothesList());

            clothItemAdapter.setClothesInBagList(clothesArrayList);
        }
    }

    @Override
    public void onPositiveViewHolderSelected(int viewHolderSelectedIndex) {

    }

    @Override
    public void onNegativeViewHolderSelected(int viewHolderSelectedIndex) {

    }
}
