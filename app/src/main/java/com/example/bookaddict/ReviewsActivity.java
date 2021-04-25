package com.example.bookaddict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookaddict.models.BookModel;
import com.example.bookaddict.models.User;
import com.example.bookaddict.utils.ErrorUtils;
import com.example.bookaddict.utils.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;

public class ReviewsActivity extends AppCompatActivity {
    private User user;
    private Bundle bundle;
    private BookModel bookModel;
    private RatingBar rating_bar_review;
    private EditText edit_text_feedback;
    private Button button_send_feedback;
    private float insert_rating;


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

        insert_rating = 0;

        button_send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        rating_bar_review.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                insert_rating = rating;
            }
        });
    }

    private void sendFeedback(){
        final String feedback;
        boolean error = false;
        String url = "http://192.168.1.65:81/bookaddict/insert_feedback.php";
        feedback = edit_text_feedback.getText().toString().trim();

        if (feedback.isEmpty()){
            edit_text_feedback.setError("Please fill this field");
            error = true;
        }
        if (!error) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(ReviewsActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("success")) {
                        Toast.makeText(ReviewsActivity.this, "Thank you for your review", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ReviewsActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (response.trim().equals("dbError")) {
                        Toast.makeText(ReviewsActivity.this, "Error while inserting", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(ReviewsActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("feedback", feedback);
                    params.put("rating", String.valueOf(insert_rating));
                    params.put("user_id", String.valueOf(user.getUser_id()));
                    params.put("book_id", String.valueOf(bookModel.getBook_id()));
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }
}