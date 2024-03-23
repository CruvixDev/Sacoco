package com.example.sacoco.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class AddBagDialogFragment extends DialogFragment implements ViewHolderSelectedCallback {
    private BagClothViewModel bagClothViewModel;
    private final ArrayList<UUID> clothesToAddUUID;
    private final CompositeDisposable compositeDisposable;

    public AddBagDialogFragment() {
        super(R.layout.dialog_add_bag_layout);
        this.clothesToAddUUID = new ArrayList<>();
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bagClothViewModel = new ViewModelProvider(requireActivity()).get(BagClothViewModel.class);

        RecyclerView clothesInBagRecyclerView = view.findViewById(R.id.clothesListView);
        clothesInBagRecyclerView.setAdapter(new ClothItemAdapter(this));
        clothesInBagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ClothItemAdapter clothItemAdapter = (ClothItemAdapter) clothesInBagRecyclerView.getAdapter();
        if (clothItemAdapter != null) {
            clothItemAdapter.setClothesInBagList(bagClothViewModel.getClothesLiveData().getValue(),
                    null);
        }

        Calendar calendar = Calendar.getInstance();
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((calendarView1, year, month, day) ->
                calendar.set(year, month, day));

        Button saveBagButton = view.findViewById(R.id.addBagButton);
        saveBagButton.setOnClickListener(view1 -> {
            Disposable disposable =
                    bagClothViewModel.addBag(calendar.get(Calendar.WEEK_OF_YEAR), clothesToAddUUID)
                            .subscribe(
                                    () -> Toast.makeText(this.getContext(),
                                            "Sac créé avec succès !", Toast.LENGTH_SHORT).show(),
                                    throwable -> Toast.makeText(this.getContext(),
                                            "Impossible de créer le sac", Toast.LENGTH_SHORT).show()
                            );
            this.compositeDisposable.add(disposable);
            this.dismiss();
        });

        ImageButton closeAddBagDialogFragment =view.findViewById(R.id.closeAddBagDialog);
        closeAddBagDialogFragment.setOnClickListener(view1 -> this.dismiss());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.compositeDisposable.dispose();
    }

    @Override
    public void onPositiveViewHolderSelected(int viewHolderSelectedIndex) {
        UUID clothUUID = Objects.requireNonNull(bagClothViewModel.getClothesLiveData().getValue()).
                get(viewHolderSelectedIndex).getClothUUID();

        this.clothesToAddUUID.add(clothUUID);
    }

    @Override
    public void onNegativeViewHolderSelected(int viewHolderSelectedIndex) {
        UUID clothUUID = Objects.requireNonNull(bagClothViewModel.getClothesLiveData().getValue()).
                get(viewHolderSelectedIndex).getClothUUID();

        this.clothesToAddUUID.remove(clothUUID);
    }
}
