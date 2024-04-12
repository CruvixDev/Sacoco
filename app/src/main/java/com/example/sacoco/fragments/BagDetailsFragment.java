package com.example.sacoco.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
import com.example.sacoco.adapter.ClothItemAdapter;
import com.example.sacoco.cominterface.DialogInterface;
import com.example.sacoco.cominterface.ViewHolderSelectedCallback;
import com.example.sacoco.dialogs.AddClothToBagDialogFragment;
import com.example.sacoco.viewmodels.BagClothViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class BagDetailsFragment extends Fragment implements ViewHolderSelectedCallback, DialogInterface {
    private BagClothViewModel bagClothViewModel;
    private RecyclerView clothesInBagRecyclerView;
    private AddClothToBagDialogFragment addClothToBagDialogFragment;
    private ImageButton deleteClothButton;
    private final ArrayList<UUID> selectedClothesUUIDList;
    private final CompositeDisposable compositeDisposable;

    public BagDetailsFragment() {
        super(R.layout.fragment_bag_content_layout);
        this.selectedClothesUUIDList = new ArrayList<>();
        this.compositeDisposable = new CompositeDisposable();
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

        this.clothesInBagRecyclerView = view.findViewById(R.id.bagContentListView);
        this.clothesInBagRecyclerView.setAdapter(new ClothItemAdapter(this,
                this.bagClothViewModel));
        this.clothesInBagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

        this.deleteClothButton = view.findViewById(R.id.deleteClothIntoBagButton);
        this.deleteClothButton.setOnClickListener(v -> {
            Disposable disposable =
                    this.bagClothViewModel.removeClothesInBag(this.selectedClothesUUIDList)
                    .subscribe(
                            () -> {
                                Toast.makeText(this.getContext(), "Successfully remove " +
                                        "clothes in bag!", Toast.LENGTH_SHORT).show();
                                this.selectedClothesUUIDList.clear();
                            },
                            throwable -> {
                                Toast.makeText(this.getContext(), "Cannot remove " +
                                        "clothes in bag!", Toast.LENGTH_SHORT).show();
                                this.selectedClothesUUIDList.clear();
                            }
                    );
            this.compositeDisposable.add(disposable);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (this.addClothToBagDialogFragment != null) {
            this.addClothToBagDialogFragment.onDestroy();
        }

        this.compositeDisposable.dispose();
    }

    private final View.OnClickListener addClothToBagButtonListener = view -> {
        FragmentManager fragmentManager = this.requireActivity().getSupportFragmentManager();
        this.addClothToBagDialogFragment = new AddClothToBagDialogFragment();
        this.addClothToBagDialogFragment.setDialogInterface(this);

        fragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setReorderingAllowed(true)
                .add(R.id.fragmentContainerView, this.addClothToBagDialogFragment, "AddBagDialogFragment")
                .commit();
    };

    @Override
    public void onPositiveViewHolderSelected(int viewHolderSelectedIndex) {
        this.deleteClothButton.setVisibility(View.VISIBLE);

        UUID selectedClothUUID = Objects.requireNonNull(
                this.bagClothViewModel.
                        getSelectedBagLiveData().
                        getValue()).
                        getClothesList().
                        get(viewHolderSelectedIndex).
                        getClothUUID();
        this.selectedClothesUUIDList.add(selectedClothUUID);
    }

    @Override
    public void onNegativeViewHolderSelected(int viewHolderSelectedIndex) {
        UUID deselectedClothUUID = Objects.requireNonNull(
                this.bagClothViewModel.
                        getSelectedBagLiveData().
                        getValue()).
                        getClothesList().
                        get(viewHolderSelectedIndex).
                        getClothUUID();
        this.selectedClothesUUIDList.remove(deselectedClothUUID);

        if (this.selectedClothesUUIDList.isEmpty()) {
            this.deleteClothButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDialogDismiss() {
        this.deleteClothButton.setVisibility(View.GONE);
        this.selectedClothesUUIDList.clear();
    }
}
