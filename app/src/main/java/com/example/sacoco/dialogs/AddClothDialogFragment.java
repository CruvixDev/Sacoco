package com.example.sacoco.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.sacoco.MainActivity;
import com.example.sacoco.R;
import com.example.sacoco.cominterface.DialogInterface;
import com.example.sacoco.fragments.ClothesListFragment;
import com.example.sacoco.models.ClothTypeEnum;
import com.example.sacoco.viewmodels.BagClothViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class AddClothDialogFragment extends DialogFragment {
    private BagClothViewModel bagClothViewModel;
    private final CompositeDisposable compositeDisposable;
    private DialogInterface dialogInterface;

    public AddClothDialogFragment() {
        super(R.layout.dialog_add_cloth_layout);
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.bagClothViewModel = new ViewModelProvider(requireActivity()).get(BagClothViewModel.class);

        ImageButton closeAddClothDialogFragment = view.findViewById(R.id.closeAddClothDialog);
        closeAddClothDialogFragment.setOnClickListener(view1 -> this.dismiss());

        ShapeableImageView clothImageView = view.findViewById(R.id.clothImageView);
        Glide.with(this.requireContext())
                .load(this.bagClothViewModel.getClothImageTemp())
                .into(clothImageView);

        TextInputLayout clothUUIDTextView = view.findViewById(R.id.identifierTextInputLayout);
        Objects.requireNonNull(clothUUIDTextView.getEditText()).setText(
                this.bagClothViewModel.getClothInCreation().getClothUUID().toString());

        TextInputLayout clothNameTextView = view.findViewById(R.id.clothNameTextInputLayout);
        TextInputLayout clothTypeTextView = view.findViewById(R.id.clothTypeTextInputLayout);

        String[] stringArray = getResources().getStringArray(R.array.items_array);
        ClothTypeEnum[] clothTypeEnums = ClothTypeEnum.values();
        Map<String, ClothTypeEnum> clothTypeEnumStringMap = new HashMap<>();

        if (clothTypeEnums.length == stringArray.length) {
            for (int i = 0; i < clothTypeEnums.length; i++) {
                clothTypeEnumStringMap.put(stringArray[i], clothTypeEnums[i]);
            }

            Button addClothButton = view.findViewById(R.id.addClothButton);
            addClothButton.setOnClickListener(parentView -> {
                String clothName = Objects.requireNonNull(
                        clothNameTextView.getEditText()).getText().toString();
                String clothTypeString = Objects.requireNonNull(
                        clothTypeTextView.getEditText()).getText().toString();

                ClothTypeEnum clothTypeEnum = clothTypeEnumStringMap.get(clothTypeString);

                if (!clothName.isEmpty() && !clothTypeString.isEmpty()) {
                    Disposable disposable = this.bagClothViewModel.addCloth(clothName, clothTypeEnum)
                            .subscribe(
                                    () -> {
                                        Toast.makeText(this.getContext(), "Vêtement ajouté avec " +
                                                "succès !", Toast.LENGTH_SHORT).show();
                                        this.dismiss();

                                        MainActivity mainActivity = (MainActivity) this.requireActivity();
                                        mainActivity.loadFragment(ClothesListFragment.class);
                                    },
                                    throwable -> {
                                        Toast.makeText(this.getContext(), "Impossible d' " +
                                                "ajouter le vêtement", Toast.LENGTH_SHORT).show();
                                        this.dismiss();

                                        MainActivity mainActivity = (MainActivity) this.requireActivity();
                                        mainActivity.loadFragment(ClothesListFragment.class);
                                    }
                            );
                    this.compositeDisposable.add(disposable);
                }
                else {
                    Toast.makeText(this.requireContext(), "Le nom du vêtement ou son type " +
                            "n'est pas renseigné !", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Log.e(this.getClass().getName(), "ClothType enum length and string array length" +
                    "mismatch!");
        }
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

    public void setDialogInterface(DialogInterface dialogInterface) {
        this.dialogInterface = dialogInterface;
    }
}
