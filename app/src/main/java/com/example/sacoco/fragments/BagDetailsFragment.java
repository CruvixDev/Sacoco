package com.example.sacoco.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sacoco.MainActivity;
import com.example.sacoco.R;
import com.example.sacoco.adapter.ClothItemAdapter;
import com.example.sacoco.cominterface.ViewHolderSelectedCallback;
import com.example.sacoco.dialogs.AddClothToBagDialogFragment;
import com.example.sacoco.viewmodels.BagClothViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class BagDetailsFragment extends Fragment implements ViewHolderSelectedCallback {
    private BagClothViewModel bagClothViewModel;
    private RecyclerView clothesInBagRecyclerView;
    private AddClothToBagDialogFragment addClothToBagDialogFragment;

    public BagDetailsFragment() {
        super(R.layout.fragment_bag_content_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.bagClothViewModel = new ViewModelProvider(requireActivity()).get(BagClothViewModel.class);

        int bagWeekNumber = Objects.requireNonNull(bagClothViewModel.getSelectedBagLiveData().
                getValue()).getWeekNumber();
        String bagContentTitle = this.getString(R.string.fragment_bag_content_title);
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.setActivityTitle(String.format(bagContentTitle, bagWeekNumber));

        clothesInBagRecyclerView = view.findViewById(R.id.bagContentListView);
        clothesInBagRecyclerView.setAdapter(new ClothItemAdapter(this));
        clothesInBagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        this.bagClothViewModel.getSelectedBagLiveData().observe(this.getViewLifecycleOwner(),
                bag -> {
                    ClothItemAdapter clothItemAdapter =
                            (ClothItemAdapter) this.clothesInBagRecyclerView.getAdapter();

                    if (clothItemAdapter != null) {
                        clothItemAdapter.setClothesInBagList(
                                this.bagClothViewModel.
                                        getSelectedBagLiveData().
                                        getValue().
                                        getClothesList(),
                                this.bagClothViewModel.
                                        getSelectedBagLiveData().
                                        getValue().
                                        getClothesPresentStateList()
                        );
                    }
                }
        );

        FloatingActionButton addBagButton = view.findViewById(R.id.addClothToBagButton);
        addBagButton.setOnClickListener(this.addClothToBagButtonListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (this.addClothToBagDialogFragment != null) {
            this.addClothToBagDialogFragment.onDestroy();
        }
    }

    private final View.OnClickListener addClothToBagButtonListener = view -> {
        FragmentManager fragmentManager = this.requireActivity().getSupportFragmentManager();
        this.addClothToBagDialogFragment = new AddClothToBagDialogFragment();

        fragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setReorderingAllowed(true)
                .add(R.id.fragmentContainerView, this.addClothToBagDialogFragment, "AddBagDialogFragment")
                .commit();
    };

    @Override
    public void onPositiveViewHolderSelected(int viewHolderSelectedIndex) {

    }

    @Override
    public void onNegativeViewHolderSelected(int viewHolderSelectedIndex) {

    }
}
