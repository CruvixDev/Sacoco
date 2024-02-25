package com.example.sacoco.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sacoco.R;
import com.example.sacoco.adapter.ClothItemAdapter;
import com.example.sacoco.cominterface.ViewHolderSelectedCallback;
import com.example.sacoco.viewmodels.BagClothViewModel;

public class AddBagDialogFragment extends DialogFragment implements ViewHolderSelectedCallback {
    private BagClothViewModel bagClothViewModel;
    private RecyclerView clothesInBagRecyclerView;

    public AddBagDialogFragment() {
        super(R.layout.dialog_add_bag_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bagClothViewModel = new ViewModelProvider(requireActivity()).get(BagClothViewModel.class);

        clothesInBagRecyclerView = view.findViewById(R.id.clothesListView);
        clothesInBagRecyclerView.setAdapter(new ClothItemAdapter(this));
        clothesInBagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ClothItemAdapter clothItemAdapter = (ClothItemAdapter)clothesInBagRecyclerView.getAdapter();
        clothItemAdapter.setClothesInBagList(bagClothViewModel.getClothesLiveData().getValue());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onPositiveViewHolderSelected(int viewHolderSelectedIndex) {

    }

    @Override
    public void onNegativeViewHolderSelected(int viewHolderSelectedIndex) {

    }
}
