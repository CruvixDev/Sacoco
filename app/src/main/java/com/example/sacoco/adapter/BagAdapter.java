package com.example.sacoco.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sacoco.MainActivity;
import com.example.sacoco.R;
import com.example.sacoco.cominterface.CardAction;
import com.example.sacoco.models.Bag;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class BagAdapter extends RecyclerView.Adapter<BagAdapter.ViewHolder> {
    private ArrayList<Bag> bagsArrayList;
    private CardAction cardAction;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView bagCardIcon;
        private final TextView bagCardTitle;
        private final TextView bagCardDateText;
        private final Button bagConsultButton;
        private final Button bagRemoveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bagCardIcon = itemView.findViewById(R.id.cardStartIcon);
            bagCardTitle = itemView.findViewById(R.id.bagName);
            bagCardDateText = itemView.findViewById(R.id.bagDate);
            bagConsultButton = itemView.findViewById(R.id.consultButton);
            bagRemoveButton = itemView.findViewById(R.id.removeButton);

            bagConsultButton.setOnClickListener(onConsultBag);
            bagRemoveButton.setOnClickListener(onRemoveBag);
        }

        private final View.OnClickListener onConsultBag = view -> {
            cardAction.onCardConsultButtonClicked(getAdapterPosition());
        };

        private final View.OnClickListener onRemoveBag = view -> {
            cardAction.onCardRemoveButtonClicked(getAdapterPosition());
        };

        public ShapeableImageView getBagCardIcon() {
            return bagCardIcon;
        }

        public TextView getBagCardTitle() {
            return bagCardTitle;
        }

        public TextView getBagCardDateText() {
            return bagCardDateText;
        }

        public Button getBagConsultButton() {
            return bagConsultButton;
        }

        public Button getBagRemoveButton() {
            return bagRemoveButton;
        }
    }

    public BagAdapter(ArrayList<Bag> bagArrayList, CardAction cardAction) {
        this.bagsArrayList = bagArrayList;
        this.cardAction = cardAction;
    }

    @NonNull
    @Override
    public BagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BagAdapter.ViewHolder holder, int position) {
        String dateText = holder.itemView.getContext().getString(R.string.card_base_text);
        Bag bag = bagsArrayList.get(position);

        holder.getBagCardIcon().setImageDrawable(AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.luggage_icon));
        holder.getBagCardTitle().setText(holder.itemView.getContext().getString(R.string.card_bag_name_title));
        holder.getBagCardDateText().setText(String.format(dateText, bag.getStartDate(), bag.getEndDate()));
    }

    @Override
    public int getItemCount() {
        return bagsArrayList.size();
    }
}
