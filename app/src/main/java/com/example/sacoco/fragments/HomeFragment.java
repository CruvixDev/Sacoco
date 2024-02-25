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
import com.example.sacoco.adapter.BagAdapter;
import com.example.sacoco.cominterface.ViewHolderSelectedCallback;
import com.example.sacoco.dialogs.AddBagDialogFragment;
import com.example.sacoco.viewmodels.BagClothViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class HomeFragment extends Fragment implements ViewHolderSelectedCallback {
    private BagClothViewModel bagClothViewModel;
    private RecyclerView bagsRecyclerView;

    public HomeFragment() {
        super(R.layout.fragment_base_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bagClothViewModel = new ViewModelProvider(requireActivity()).get(BagClothViewModel.class);

        bagsRecyclerView = view.findViewById(R.id.contentListView);
        bagsRecyclerView.setAdapter(new BagAdapter(this));
        bagsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ProgressBar circularProgressBar = view.findViewById(R.id.progressBar);
        if (!bagClothViewModel.isBagsDataFetched()) {
            circularProgressBar.setVisibility(View.VISIBLE);
        }

        //TODO make the update of recycler view with DiffUtils for performances
        bagClothViewModel.getBagsLiveData().observe(this.getViewLifecycleOwner(),
                bagArrayList -> {
                    BagAdapter bagAdapter = (BagAdapter)this.bagsRecyclerView.getAdapter();

                    if (bagAdapter != null) {
                        bagAdapter.setBagsArrayList(bagClothViewModel.getBagsLiveData().getValue());
                    }

                    Objects.requireNonNull(bagsRecyclerView.getAdapter()).
                            notifyItemInserted(bagArrayList.size());

                    circularProgressBar.setVisibility(View.INVISIBLE);
                }
        );

        FloatingActionButton addBagButton = view.findViewById(R.id.addContentButton);
        addBagButton.setOnClickListener(addBagButtonClickedListener);
    }

    //TODO don't open more than twice the dialog
    private final View.OnClickListener addBagButtonClickedListener = view -> {
        AddBagDialogFragment addBagDialogFragment = new AddBagDialogFragment();
        requireActivity().getSupportFragmentManager().
                beginTransaction().
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                setReorderingAllowed(true).
                add(R.id.fragmentContainerView, addBagDialogFragment).
                addToBackStack(null).
                commit();
    };

    @Override
    public void onPositiveViewHolderSelected(int viewHolderSelectedIndex) {
        int weekNumber = Objects.requireNonNull(bagClothViewModel.getBagsLiveData().getValue()).
                get(viewHolderSelectedIndex).getWeekNumber();

        bagClothViewModel.setSelectedBagLiveData(weekNumber);

        MainActivity mainActivityInstance = (MainActivity) requireActivity();
        mainActivityInstance.loadFragment(BagDetailsFragment.class);
    }

    @Override
    public void onNegativeViewHolderSelected(int viewHolderSelectedIndex) {
        int weekNumber = Objects.requireNonNull(bagClothViewModel.getBagsLiveData().getValue()).
                get(viewHolderSelectedIndex).getWeekNumber();

        bagClothViewModel.removeBag(weekNumber);
    }
}
