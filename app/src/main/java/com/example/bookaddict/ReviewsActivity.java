package com.example.bookaddict;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.bookaddict.models.BookModel;
import com.example.bookaddict.models.User;
import com.example.bookaddict.utils.SharedPrefManager;

public class ReviewsActivity extends AppCompatActivity {
    private User user;
    private Bundle bundle;
    private BookModel bookModel;
    private RatingBar rating_bar_review;
    private EditText edit_text_feedback;
    private Button button_send_feedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        user = SharedPrefManager.getInstance(this).getUser();
        bundle = getIntent().getExtras();
        bookModel = (BookModel) bundle.getSerializable("book_details");

        this.setTitle(bookModel.getBook_name());

        rating_bar_review = findViewById(R.id.rating_bar_review);
        edit_text_feedback = findViewById(R.id.edit_text_feedback);
        button_send_feedback = findViewById(R.id.button_send_feedback);

        button_send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });
    }

    private void sendFeedback(){

    }
}