package com.example.sacoco.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sacoco.R;
import com.example.sacoco.cominterface.ViewHolderSelectedCallback;
import com.example.sacoco.models.Bag;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BagAdapter extends RecyclerView.Adapter<BagAdapter.ViewHolder> {
    private ArrayList<Bag> bagsArrayList;
    private final ViewHolderSelectedCallback viewHolderSelectedCallback;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView bagCardTitle;
        private final TextView bagCardDateText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bagCardTitle = itemView.findViewById(R.id.cardMainTitle);
            bagCardDateText = itemView.findViewById(R.id.cardSubTitle);
            Button bagConsultButton = itemView.findViewById(R.id.consultButton);
            Button bagRemoveButton = itemView.findViewById(R.id.removeButton);

            View.OnClickListener onConsultBag = view -> viewHolderSelectedCallback.
                    onPositiveViewHolderSelected(getAdapterPosition());
            bagConsultButton.setOnClickListener(onConsultBag);
            View.OnClickListener onRemoveBag = view -> viewHolderSelectedCallback.
                    onNegativeViewHolderSelected(getAdapterPosition());
            bagRemoveButton.setOnClickListener(onRemoveBag);
        }

        public TextView getBagCardTitle() {
            return bagCardTitle;
        }

        public TextView getBagCardDateText() {
            return bagCardDateText;
        }
    }

    public BagAdapter(ViewHolderSelectedCallback viewHolderSelectedCallback) {
        this.bagsArrayList = new ArrayList<>();
        this.viewHolderSelectedCallback = viewHolderSelectedCallback;
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
        String dateText = holder.itemView.getContext().getString(R.string.card_base_text);
        String startDateString;
        String endDateString;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, bagWeekNumber);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date endDate = calendar.getTime();

        startDateString = dateFormat.format(startDate);
        endDateString = dateFormat.format(endDate);

        holder.getBagCardTitle().setText(holder.itemView.getContext().getString(R.string.card_bag_name_title));
        holder.getBagCardDateText().setText(String.format(dateText, startDateString, endDateString));
    }

    @Override
    public int getItemCount() {
        return bagsArrayList.size();
    }

    public void setBagsArrayList(ArrayList<Bag> bagsArrayList) {
        this.bagsArrayList = bagsArrayList;
    }
}
