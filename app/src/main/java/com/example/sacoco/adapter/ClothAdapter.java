package com.example.sacoco.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sacoco.R;
import com.example.sacoco.cominterface.ViewHolderSelectedCallback;
import com.example.sacoco.models.Cloth;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ClothAdapter extends RecyclerView.Adapter<ClothAdapter.ViewHolder> {
    private ArrayList<Cloth> clothesArrayList;
    private final ViewHolderSelectedCallback viewHolderSelectedCallback;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView clothCardIcon;
        private final TextView clothCardTitle;
        private final TextView clothCardUUID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clothCardIcon = itemView.findViewById(R.id.cardStartIcon);
            clothCardTitle = itemView.findViewById(R.id.cardMainTitle);
            clothCardUUID = itemView.findViewById(R.id.cardSubTitle);
            Button clothConsultButton = itemView.findViewById(R.id.consultButton);
            Button clothRemoveButton = itemView.findViewById(R.id.removeButton);

            View.OnClickListener onConsultBag = view -> viewHolderSelectedCallback.
                    onPositiveViewHolderSelected(getAdapterPosition());
            clothConsultButton.setOnClickListener(onConsultBag);

            View.OnClickListener onRemoveBag = view -> viewHolderSelectedCallback.
                    onNegativeViewHolderSelected(getAdapterPosition());
            clothRemoveButton.setOnClickListener(onRemoveBag);
        }

        public ShapeableImageView getClothCardIcon() {
            return clothCardIcon;
        }

        public TextView getClothCardTitle() {
            return clothCardTitle;
        }

        public TextView getClothCardUUID() {
            return clothCardUUID;
        }
    }

    public ClothAdapter(ViewHolderSelectedCallback cardAction) {
        this.clothesArrayList = new ArrayList<>();
        this.viewHolderSelectedCallback = cardAction;
    }

    @NonNull
    @Override
    public ClothAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_layout_constraint, parent, false);
        return new ClothAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClothAdapter.ViewHolder holder, int position) {
        Cloth cloth = clothesArrayList.get(position);
        ShapeableImageView clothCardIcon = holder.getClothCardIcon();

        switch (cloth.getClothType()) {
            case SHIRT:
                clothCardIcon.setImageDrawable(AppCompatResources.getDrawable(
                        holder.itemView.getContext(), R.drawable.shirt_icon));
                break;
            case PANT:
                clothCardIcon.setImageDrawable(AppCompatResources.getDrawable(
                        holder.itemView.getContext(), R.drawable.pants_icon));
                break;
            case  SOCK:
                clothCardIcon.setImageDrawable(AppCompatResources.getDrawable(
                        holder.itemView.getContext(), R.drawable.sock_icon));
                break;
            case SHOE:
                clothCardIcon.setImageDrawable(AppCompatResources.getDrawable(
                        holder.itemView.getContext(), R.drawable.shoe_icon));
                break;
            default:
                clothCardIcon.setImageDrawable(AppCompatResources.getDrawable(
                        holder.itemView.getContext(), R.drawable.luggage_icon));
                break;
        }

        holder.getClothCardTitle().setText(cloth.getClothName());
        holder.getClothCardUUID().setText(cloth.getClothUUID().toString());
    }

    @Override
    public int getItemCount() {
        return clothesArrayList.size();
    }

    public void setClothesArrayList(ArrayList<Cloth> clothesArrayList) {
        this.clothesArrayList = clothesArrayList;
    }
}
