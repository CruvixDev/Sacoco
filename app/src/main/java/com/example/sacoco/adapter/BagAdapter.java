package com.example.sacoco.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sacoco.R;
import com.example.sacoco.cominterface.ViewHolderSelectedCallback;
import com.example.sacoco.models.Bag;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BagAdapter extends RecyclerView.Adapter<BagAdapter.ViewHolder> {
    private final ArrayList<Bag> bagsArrayList;
    private final ViewHolderSelectedCallback viewHolderSelectedCallback;
    private final SimpleDateFormat simpleDateFormat;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView bagCardTitle;
        private final TextView bagCardDateText;
        private final ShapeableImageView bagCheckedStateImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.bagCardTitle = itemView.findViewById(R.id.cardMainTitle);
            this.bagCardDateText = itemView.findViewById(R.id.cardSubTitle);
            Button bagConsultButton = itemView.findViewById(R.id.consultButton);
            Button bagRemoveButton = itemView.findViewById(R.id.removeButton);

            View.OnClickListener onConsultBag = view -> viewHolderSelectedCallback.
                    onPositiveViewHolderSelected(getAdapterPosition());
            bagConsultButton.setOnClickListener(onConsultBag);
            View.OnClickListener onRemoveBag = view -> viewHolderSelectedCallback.
                    onNegativeViewHolderSelected(getAdapterPosition());
            bagRemoveButton.setOnClickListener(onRemoveBag);

            this.bagCheckedStateImage = itemView.findViewById(R.id.bagCheckedState);
        }

        public TextView getBagCardTitle() {
            return this.bagCardTitle;
        }

        public TextView getBagCardDateText() {
            return this.bagCardDateText;
        }

        public ShapeableImageView getBagCheckedStateImage() {
            return this.bagCheckedStateImage;
        }
    }

    public BagAdapter(ViewHolderSelectedCallback viewHolderSelectedCallback) {
        this.bagsArrayList = new ArrayList<>();
        this.viewHolderSelectedCallback = viewHolderSelectedCallback;
        this.simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    }

    @NonNull
    @Override
    public BagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_layout_constraint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BagAdapter.ViewHolder holder, int position) {
        Bag bag = bagsArrayList.get(position);
        int bagWeekNumber = bag.getWeekNumber();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, bagWeekNumber);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date endDate = calendar.getTime();

        String startDateString = this.simpleDateFormat.format(startDate);
        String endDateString = this.simpleDateFormat.format(endDate);

        Context context = holder.itemView.getContext();
        holder.getBagCardTitle().setText(context.getString(R.string.card_bag_name_title));
        holder.getBagCardDateText().setText(context.getString(R.string.card_base_text,
                startDateString, endDateString));

        holder.getBagCheckedStateImage().setVisibility(View.VISIBLE);
        if (bag.isChecked()) {
            holder.getBagCheckedStateImage().setImageDrawable(AppCompatResources.getDrawable(
                    holder.itemView.getContext(), R.drawable.check));
        }
        else {
            holder.getBagCheckedStateImage().setImageDrawable(AppCompatResources.getDrawable(
                    holder.itemView.getContext(), R.drawable.close));
        }
    }

    @Override
    public int getItemCount() {
        return bagsArrayList.size();
    }

    public void setBagsArrayList(ArrayList<Bag> newBagsList) {
        BagDiffCallback diffCallback = new BagDiffCallback(this.bagsArrayList, newBagsList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.bagsArrayList.clear();
        this.bagsArrayList.addAll(newBagsList);
        diffResult.dispatchUpdatesTo(this);
    }
}
