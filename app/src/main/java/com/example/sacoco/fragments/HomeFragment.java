package com.example.sacoco.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.example.sacoco.adapter.BagAdapter;
import com.example.sacoco.cominterface.ViewHolderSelectedCallback;
import com.example.sacoco.dialogs.AddBagDialogFragment;
import com.example.sacoco.viewmodels.BagClothViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class HomeFragment extends Fragment implements ViewHolderSelectedCallback {
    private BagClothViewModel bagClothViewModel;
    private RecyclerView bagsRecyclerView;
    private AddBagDialogFragment addBagDialogFragment;
    private final CompositeDisposable compositeDisposable;

    public HomeFragment() {
        super(R.layout.fragment_base_layout);
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.bagClothViewModel = new ViewModelProvider(requireActivity()).get(BagClothViewModel.class);

        MainActivity mainActivity = (MainActivity)requireActivity();
        mainActivity.setActivityTitle(this.getString(R.string.main_activity_menu_home));

        bagsRecyclerView = view.findViewById(R.id.contentListView);
        bagsRecyclerView.setAdapter(new BagAdapter(this));
        bagsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ProgressBar circularProgressBar = view.findViewById(R.id.progressBar);
        if (!bagClothViewModel.isBagsDataFetched()) {
            circularProgressBar.setVisibility(View.VISIBLE);
        }

        this.bagClothViewModel.getBagsLiveData().observe(this.getViewLifecycleOwner(),
                bagArrayList -> {
                    BagAdapter bagAdapter = (BagAdapter)this.bagsRecyclerView.getAdapter();

                    if (bagAdapter != null) {
                        bagAdapter.setBagsArrayList(bagClothViewModel.getBagsLiveData().getValue());
                    }

                    circularProgressBar.setVisibility(View.INVISIBLE);
                }
        );

        FloatingActionButton addBagButton = view.findViewById(R.id.addContentButton);
        addBagButton.setOnClickListener(addBagButtonClickedListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (this.addBagDialogFragment != null) {
            this.addBagDialogFragment.onDestroy();
        }

        this.compositeDisposable.dispose();
    }

    private final View.OnClickListener addBagButtonClickedListener = view -> {
        FragmentManager fragmentManager = this.requireActivity().getSupportFragmentManager();
        this.addBagDialogFragment = new AddBagDialogFragment();

        fragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setReorderingAllowed(true)
                .add(R.id.fragmentContainerView, this.addBagDialogFragment, "AddBagDialogFragment")
                .addToBackStack(null)
                .commit();
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

        Disposable disposable = bagClothViewModel.removeBag(weekNumber)
                .subscribe(
                        () -> Toast.makeText(this.getContext(),
                                "Successfully remove the bag", Toast.LENGTH_SHORT).show(),
                        throwable -> Toast.makeText(this.getContext(),
                                "Cannot remove the bag", Toast.LENGTH_SHORT).show()
                );
        this.compositeDisposable.add(disposable);
    }
}
