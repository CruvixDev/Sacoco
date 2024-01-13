package com.example.sacoco.fragments;

import android.os.Bundle;
import android.view.View;

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
import com.example.sacoco.cominterface.CardAction;
import com.example.sacoco.dialogs.AddBagDialogFragment;
import com.example.sacoco.viewmodels.BagViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.Objects;

public class HomeFragment extends Fragment implements CardAction {
    private BagViewModel bagViewModel;
    private RecyclerView bagsRecyclerView;

    public HomeFragment() {
        super(R.layout.fragment_base_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bagViewModel = new ViewModelProvider(requireActivity()).get(BagViewModel.class);

        bagsRecyclerView = view.findViewById(R.id.contentListView);
        bagsRecyclerView.setAdapter(new BagAdapter(bagViewModel.getBagsLiveData().getValue(), this));
        bagsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bagViewModel.getBagsLiveData().observe(this.getViewLifecycleOwner(),
                bagArrayList -> Objects.requireNonNull(bagsRecyclerView.getAdapter()).notifyItemInserted(bagArrayList.size()));

        FloatingActionButton addBagButton = view.findViewById(R.id.addContentButton);
        addBagButton.setOnClickListener(addBagButtonClickedListener);
    }

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
    public void onCardConsultButtonClicked(int bagSelectedIndex) {
        Date startDate = Objects.requireNonNull(bagViewModel.getBagsLiveData().getValue()).
                get(bagSelectedIndex).getStartDate();
        Date endDate = bagViewModel.getBagsLiveData().getValue().get(bagSelectedIndex).getEndDate();

        bagViewModel.setSelectedBagLiveData(startDate, endDate);

        MainActivity mainActivityInstance = (MainActivity) requireActivity();
        mainActivityInstance.loadFragment(BagDetailsFragment.class);
    }

    @Override
    public void onCardRemoveButtonClicked(int bagSelectedIndex) {
        Date startDate = Objects.requireNonNull(bagViewModel.getBagsLiveData().getValue()).
                get(bagSelectedIndex).getStartDate();
        Date endDate = bagViewModel.getBagsLiveData().getValue().get(bagSelectedIndex).getEndDate();

        bagViewModel.removeBag(startDate, endDate);
    }
}
