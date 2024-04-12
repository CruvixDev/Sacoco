package com.example.sacoco.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.example.sacoco.cominterface.DialogInterface;
import com.example.sacoco.cominterface.ViewHolderSelectedCallback;
import com.example.sacoco.viewmodels.BagClothViewModel;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class AddClothToBagDialogFragment extends DialogFragment implements ViewHolderSelectedCallback {
    private BagClothViewModel bagClothViewModel;
    private final ArrayList<UUID> clothesToAddUUID;
    private final CompositeDisposable compositeDisposable;
    private DialogInterface dialogInterface;

    public AddClothToBagDialogFragment() {
        super(R.layout.dialog_add_cloth_to_bag_layout);
        this.clothesToAddUUID = new ArrayList<>();
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.bagClothViewModel = new ViewModelProvider(requireActivity()).get(BagClothViewModel.class);

        RecyclerView addClothesToBagRecyclerView = view.findViewById(R.id.addClothesToBagRecyclerView);
        addClothesToBagRecyclerView.setAdapter(new ClothItemAdapter(this,
                this.bagClothViewModel));
        addClothesToBagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ClothItemAdapter clothItemAdapter = (ClothItemAdapter) addClothesToBagRecyclerView.getAdapter();
        if (clothItemAdapter != null) {
            clothItemAdapter.setClothesInBagList(Objects.requireNonNull(
                    this.bagClothViewModel.getClothesLiveData().getValue()), null);
        }

        Button insertClothIntoBagButton = view.findViewById(R.id.addClothToBagConfirm);
        insertClothIntoBagButton.setOnClickListener(view1 -> {
            Disposable disposable = this.bagClothViewModel.addClothesToBag(this.clothesToAddUUID)
                    .subscribe(
                            () -> {
                                Toast.makeText(this.getContext(),
                                        "Vêtements ajoutés avec succès !", Toast.LENGTH_SHORT).show();
                                this.dismiss();
                            },
                            throwable -> {
                                Toast.makeText(this.getContext(),
                                        "Impossible d'ajouter les vêtements", Toast.LENGTH_SHORT).show();
                                this.dismiss();
                            }
                    );
            compositeDisposable.add(disposable);
        });

        ImageButton closeAddClothToBagDialogFragment = view.findViewById(R.id.addClothToBagDialogClose);
        closeAddClothToBagDialogFragment.setOnClickListener(view1 -> this.dismiss());
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
        if (this.dialogInterface != null) {
            this.dialogInterface.onDialogDismiss();
        }
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

    public void setDialogInterface(DialogInterface dialogInterface) {
        this.dialogInterface = dialogInterface;
    }
}
