package com.example.sacoco.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sacoco.MainActivity;
import com.example.sacoco.R;
import com.example.sacoco.models.ClothTypeEnum;
import com.example.sacoco.viewmodels.BagClothViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class ClothDetailsFragment extends Fragment {
    private final CompositeDisposable compositeDisposable;

    public ClothDetailsFragment() {
        super(R.layout.fragment_show_cloth_details_layout);
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BagClothViewModel bagClothViewModel = new ViewModelProvider(requireActivity()).
                get(BagClothViewModel.class);

        String clothName = Objects.requireNonNull(bagClothViewModel.getSelectedClothLiveData().
                getValue()).getClothName();

        String[] stringArray = getResources().getStringArray(R.array.items_array);
        ClothTypeEnum[] clothTypeEnums = ClothTypeEnum.values();
        Map<ClothTypeEnum, String> clothTypeEnumStringMap = new HashMap<>();

        if (clothTypeEnums.length == stringArray.length) {
            for (int i = 0; i < clothTypeEnums.length; i++) {
                clothTypeEnumStringMap.put(clothTypeEnums[i], stringArray[i]);
            }

            MainActivity mainActivity = (MainActivity)requireActivity();
            mainActivity.setActivityTitle(clothName);

            TextInputLayout clothNameEditText = view.findViewById(R.id.clothDetailsNameTextInputLayout);
            Objects.requireNonNull(clothNameEditText.getEditText()).setText(clothName);

            TextInputLayout clothTypeEditText = view.findViewById(R.id.clothTypeTextInputLayout);
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)
                    clothTypeEditText.getEditText();
            Objects.requireNonNull(autoCompleteTextView).setText(clothTypeEnumStringMap.get(
                    bagClothViewModel.getSelectedClothLiveData().getValue().getClothType()), false);

            ShapeableImageView clothImageView = view.findViewById(R.id.clothDetailsImageView);
            Disposable disposable = bagClothViewModel.getClothImageBitmap(bagClothViewModel.
                            getSelectedClothLiveData().getValue().getClothUUID()).
                    subscribe(
                            clothImageView::setImageBitmap,
                            throwable -> Toast.makeText(this.requireContext(), "Impossible de " +
                                    "charger l'image", Toast.LENGTH_SHORT).show()
                    );
            compositeDisposable.add(disposable);
        }
        else {
            Log.e(this.getClass().getName(), "ClothType enum length and string array length" +
                    "mismatch!");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.compositeDisposable.dispose();
    }
}
