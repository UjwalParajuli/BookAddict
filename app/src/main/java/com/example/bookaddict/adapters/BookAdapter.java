package com.example.bookaddict.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookaddict.R;
import com.example.bookaddict.models.BookModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    ArrayList<BookModel> bookModelArrayList;
    Context context;

    public BookAdapter(ArrayList<BookModel> bookModelArrayList, Context context) {
        this.bookModelArrayList = bookModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.item_book, parent, false);
        BookAdapter.ViewHolder viewHolder =new BookAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        BookModel bookModel = bookModelArrayList.get(position);

        Glide.with(context).load(bookModel.getCover_image()).into(holder.image_view_book);
        holder.text_view_book_name.setText(bookModel.getBook_name());
        holder.text_view_book_author.setText("by " + bookModel.getAuthor());

    }

    @Override
    public int getItemCount() {
        return bookModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView text_view_book_name, text_view_book_author;
        public ImageView image_view_book;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_view_book_name = itemView.findViewById(R.id.text_view_book_name);
            text_view_book_author = itemView.findViewById(R.id.text_view_book_author);
            image_view_book = itemView.findViewById(R.id.image_view_book);

        }
    }
}
