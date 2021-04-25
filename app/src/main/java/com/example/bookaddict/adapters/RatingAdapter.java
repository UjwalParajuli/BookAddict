package com.example.bookaddict.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookaddict.R;
import com.example.bookaddict.models.RatingModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    ArrayList<RatingModel> ratingModelArrayList;
    Context context;

    public RatingAdapter(ArrayList<RatingModel> ratingModelArrayList, Context context) {
        this.ratingModelArrayList = ratingModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.item_review, parent, false);
        RatingAdapter.ViewHolder viewHolder =new RatingAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.ViewHolder holder, int position) {
        RatingModel ratingModel = ratingModelArrayList.get(position);

        holder.rating_user_name.setText(ratingModel.getUser_name());
        holder.text_view_feedback.setText(ratingModel.getReview_text());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date review_date = null;
        long _review_date = 0;
        try {
            review_date = format.parse(ratingModel.getReview_date());
            _review_date = review_date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String year = (String) DateFormat.format("yyyy", _review_date); // 2020
        String day          = (String) DateFormat.format("dd",   _review_date); // 20
        String monthString  = (String) DateFormat.format("MMMM",  _review_date); // Jun

        holder.text_view_review_date.setText(monthString + " " + day + "," + " " + year);

        holder.individual_rating_bar.setRating(ratingModel.getRating());

    }

    @Override
    public int getItemCount() {
        return ratingModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView rating_user_name, text_view_review_date, text_view_feedback;
        public RatingBar individual_rating_bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rating_user_name = itemView.findViewById(R.id.rating_user_name);
            text_view_review_date = itemView.findViewById(R.id.text_view_review_date);
            text_view_feedback = itemView.findViewById(R.id.text_view_feedback);
            individual_rating_bar = itemView.findViewById(R.id.individual_rating_bar);

        }
    }
}
