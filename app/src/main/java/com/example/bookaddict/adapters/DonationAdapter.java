package com.example.bookaddict.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookaddict.R;
import com.example.bookaddict.models.DonationModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.ViewHolder> {
    ArrayList<DonationModel> donationModelArrayList;
    Context context;

    public DonationAdapter(ArrayList<DonationModel> donationModelArrayList, Context context) {
        this.donationModelArrayList = donationModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public DonationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.item_donation, parent, false);
        DonationAdapter.ViewHolder viewHolder =new DonationAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DonationAdapter.ViewHolder holder, int position) {
        DonationModel donationModel = donationModelArrayList.get(position);

        holder.text_view_notification_message.setText("You have donated Rs. " + donationModel.getDonation_amount() + " " + "on " + donationModel.getDonation_date());
    }

    @Override
    public int getItemCount() {
        return donationModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView text_view_notification_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_view_notification_message = itemView.findViewById(R.id.text_view_notification_message);

        }
    }
}
