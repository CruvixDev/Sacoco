package com.example.sacoco.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sacoco.MainActivity;
import com.example.sacoco.R;
import com.example.sacoco.adapter.ClothAdapter;
import com.example.sacoco.cominterface.CardAction;
import com.example.sacoco.dialogs.AddClothDialogFragment;
import com.example.sacoco.viewmodels.BagClothViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;
import java.util.UUID;

public class ClothesListFragment extends Fragment implements CardAction {
    private BagClothViewModel bagClothViewModel;
    private RecyclerView clothesRecyclerView;

    public ClothesListFragment() {
        super(R.layout.fragment_base_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bagClothViewModel = new ViewModelProvider(requireActivity()).get(BagClothViewModel.class);

        clothesRecyclerView = view.findViewById(R.id.contentListView);
        clothesRecyclerView.setAdapter(new ClothAdapter(this));
        clothesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ProgressBar circularProgressBar = view.findViewById(R.id.progressBar);
        if (!bagClothViewModel.isClothesDataFetched()) {
            circularProgressBar.setVisibility(View.VISIBLE);
        }

        //TODO make the update of recycler view with DiffUtils for performances
        bagClothViewModel.getClothesLiveData().observe(this.getViewLifecycleOwner(),
                clothArrayList -> {
                    ClothAdapter clothAdapter = (ClothAdapter) this.clothesRecyclerView.getAdapter();

                    if (clothAdapter != null) {
                        clothAdapter.setClothesArrayList(bagClothViewModel.getClothesLiveData().
                                getValue());
                    }

                    Objects.requireNonNull(clothesRecyclerView.getAdapter()).
                            notifyItemInserted(clothArrayList.size());

                    circularProgressBar.setVisibility(View.INVISIBLE);
                }
        );

        FloatingActionButton addClothButton = view.findViewById(R.id.addContentButton);
        addClothButton.setOnClickListener(addClothButtonClickedListener);
    }

    //TODO don't open more than twice the dialog
    private final View.OnClickListener addClothButtonClickedListener = view -> {
        AddClothDialogFragment addClothDialogFragment = new AddClothDialogFragment();
        requireActivity().getSupportFragmentManager().
                beginTransaction().
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                setReorderingAllowed(true).
                add(R.id.fragmentContainerView, addClothDialogFragment).
                addToBackStack(null).
                commit();
    };

    @Override
    public void onCardConsultButtonClicked(int cardSelectedIndex) {
        UUID clothUUID = Objects.requireNonNull(bagClothViewModel.getClothesLiveData().getValue()).
                get(cardSelectedIndex).getClothUUID();

        bagClothViewModel.setSelectedClothLiveData(clothUUID);

        MainActivity mainActivityInstance = (MainActivity) requireActivity();
        mainActivityInstance.loadFragment(ClothDetailsFragment.class);
    }

    @Override
    public void onCardRemoveButtonClicked(int cardSelectedIndex) {
        UUID clothUUID = Objects.requireNonNull(bagClothViewModel.getClothesLiveData().getValue()).
                get(cardSelectedIndex).getClothUUID();

        bagClothViewModel.removeCloth(clothUUID);
    }
}
