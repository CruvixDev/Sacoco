package com.example.sacoco.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sacoco.R;
import com.example.sacoco.cominterface.ViewHolderSelectedCallback;
import com.example.sacoco.models.Cloth;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ClothItemAdapter extends RecyclerView.Adapter<ClothItemAdapter.ViewHolder> {
    private final ArrayList<Cloth> clothesInBagList;
    private ArrayList<Boolean> clothesPresenceStateList;
    private final ViewHolderSelectedCallback viewHolderSelectedCallback;

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

    public ClothItemAdapter(ViewHolderSelectedCallback viewHolderSelectedCallback) {
        this.clothesInBagList = new ArrayList<>();
        this.clothesPresenceStateList = new ArrayList<>();
        this.viewHolderSelectedCallback = viewHolderSelectedCallback;
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
                        R.color.md_theme_light_onErrorContainer));
            }
        }

        holder.getClothImage().setImageDrawable(AppCompatResources.getDrawable(
                holder.itemView.getContext(), R.drawable.shirt_icon));
        holder.getClothName().setText(cloth.getClothName());
        holder.getClothUUID().setText(cloth.getClothUUID().toString());
    }

    @Override
    public int getItemCount() {
        return this.clothesInBagList.size();
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
