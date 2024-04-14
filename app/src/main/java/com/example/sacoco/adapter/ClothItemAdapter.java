package com.example.sacoco.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sacoco.R;
import com.example.sacoco.cominterface.ViewHolderSelectedCallback;
import com.example.sacoco.models.Cloth;
import com.example.sacoco.viewmodels.BagClothViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class ClothItemAdapter extends RecyclerView.Adapter<ClothItemAdapter.ViewHolder> {
    private final ArrayList<Cloth> clothesInBagList;
    private ArrayList<Boolean> clothesPresenceStateList;
    private final ViewHolderSelectedCallback viewHolderSelectedCallback;
    private final BagClothViewModel bagClothViewModel;
    private final CompositeDisposable compositeDisposable;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView clothImage;
        private final TextView clothName;
        private final TextView clothUUID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clothImage = itemView.findViewById(R.id.clothListImage);
            clothName = itemView.findViewById(R.id.clothListName);
            clothUUID = itemView.findViewById(R.id.clothListUUID);
            CheckBox checkBox = itemView.findViewById(R.id.clothListCheckBox);

            checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    viewHolderSelectedCallback.onPositiveViewHolderSelected(getAdapterPosition());
                }
                else {
                    viewHolderSelectedCallback.onNegativeViewHolderSelected(getAdapterPosition());
                }
            });
        }

        public ShapeableImageView getClothImage() {
            return this.clothImage;
        }

        public TextView getClothName() {
            return this.clothName;
        }

        public TextView getClothUUID() {
            return this.clothUUID;
        }
    }

    public ClothItemAdapter(ViewHolderSelectedCallback viewHolderSelectedCallback,
                            BagClothViewModel bagClothViewModel) {
        this.clothesInBagList = new ArrayList<>();
        this.clothesPresenceStateList = new ArrayList<>();
        this.viewHolderSelectedCallback = viewHolderSelectedCallback;
        this.bagClothViewModel = bagClothViewModel;
        this.compositeDisposable = new CompositeDisposable();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cloth_item_selection_layout, parent, false);
        return new ClothItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cloth cloth = clothesInBagList.get(position);

        if (this.clothesPresenceStateList != null && !this.clothesPresenceStateList.isEmpty()) {
            if (this.clothesPresenceStateList.get(position)) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                        R.color.cloth_present));
            }
            else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                        R.color.cloth_not_present));
            }
        }

        Disposable disposable = this.bagClothViewModel.getClothImageBitmap(cloth.getClothUUID()).
                subscribe(
                        bitmap -> holder.getClothImage().setImageBitmap(bitmap),
                        throwable -> Log.e(this.getClass().getName(), "Cannot load image")
                );
        this.compositeDisposable.add(disposable);

        holder.getClothName().setText(cloth.getClothName());
        holder.getClothUUID().setText(cloth.getClothUUID().toString());
    }

    @Override
    public int getItemCount() {
        return this.clothesInBagList.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.compositeDisposable.dispose();
    }

    public void setClothesInBagList(ArrayList<Cloth> newClothesInBagList,
                                    ArrayList<Boolean> newClothesPresentStateList) {
        ClothDiffCallback diffCallback = new ClothDiffCallback(this.clothesInBagList, newClothesInBagList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.clothesInBagList.clear();
        this.clothesInBagList.addAll(newClothesInBagList);
        this.clothesPresenceStateList = newClothesPresentStateList;
        diffResult.dispatchUpdatesTo(this);
    }
}
