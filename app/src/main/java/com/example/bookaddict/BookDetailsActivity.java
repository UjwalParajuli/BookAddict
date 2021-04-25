package com.example.bookaddict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookaddict.adapters.RatingAdapter;
import com.example.bookaddict.models.BookModel;
import com.example.bookaddict.models.DonationModel;
import com.example.bookaddict.models.RatingModel;
import com.example.bookaddict.models.User;
import com.example.bookaddict.utils.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BookDetailsActivity extends AppCompatActivity {
    private Bundle bundle;
    private BookModel bookModel;
    private User user;
    private ImageView image_view_book_details;
    private TextView text_view_book_title_book_details, text_view_author_name_book_details, text_view_published_date, text_view_total_pages, text_view_genre;
    private RatingBar average_rating_bar;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recycler_view_reviews;
    private ArrayList<RatingModel> ratingModelArrayList;
    private RatingAdapter ratingAdapter;
    private Button button_download, button_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        user = SharedPrefManager.getInstance(this).getUser();
        bundle = getIntent().getExtras();
        bookModel = (BookModel) bundle.getSerializable("book_details");

        this.setTitle(bookModel.getBook_name());

        image_view_book_details = findViewById(R.id.image_view_book_details);
        text_view_book_title_book_details = findViewById(R.id.text_view_book_title_book_details);
        text_view_author_name_book_details = findViewById(R.id.text_view_author_name_book_details);
        text_view_published_date = findViewById(R.id.text_view_published_date);
        text_view_total_pages = findViewById(R.id.text_view_total_pages);
        text_view_genre = findViewById(R.id.text_view_genre);
        average_rating_bar = findViewById(R.id.average_rating_bar);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        recycler_view_reviews = findViewById(R.id.recycler_view_reviews);
        button_download = findViewById(R.id.button_download);
        button_read = findViewById(R.id.button_read);

        ratingModelArrayList = new ArrayList<>();


        button_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload();
            }
        });

        button_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReviewActivity();
            }
        });

        setData();
    }

    private void setData(){
        Glide.with(BookDetailsActivity.this).load(bookModel.getCover_image()).into(image_view_book_details);
        text_view_book_title_book_details.setText(bookModel.getBook_name());
        text_view_author_name_book_details.setText("by " + bookModel.getAuthor());
        text_view_published_date.setText("Published On: " + bookModel.getPublished_date());
        text_view_genre.setText("Genre: " + bookModel.getGenre());
        text_view_total_pages.setText("Pages: " + bookModel.getPages());
    }

    private void startDownload(){

    }

    private void read(){

    }

    private void openReviewActivity(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("book_details", bookModel);
        Intent intent = new Intent(BookDetailsActivity.this, ReviewsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}