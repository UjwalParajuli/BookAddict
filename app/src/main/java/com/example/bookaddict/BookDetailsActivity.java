package com.example.bookaddict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.bookaddict.adapters.RatingAdapter;
import com.example.bookaddict.models.BookModel;
import com.example.bookaddict.models.DonationModel;
import com.example.bookaddict.models.RatingModel;
import com.example.bookaddict.models.User;
import com.example.bookaddict.utils.ErrorUtils;
import com.example.bookaddict.utils.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookDetailsActivity extends AppCompatActivity {
    private Bundle bundle;
    private BookModel bookModel;
    private User user;
    private ImageView image_view_book_details;
    private TextView text_view_book_title_book_details, text_view_author_name_book_details, text_view_published_date, text_view_total_pages, text_view_genre, text_view_no_reviews;
    private RatingBar average_rating_bar;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recycler_view_reviews;
    private ArrayList<RatingModel> ratingModelArrayList;
    private RatingAdapter ratingAdapter;
    private Button button_download, button_read;
    private long downloadID;

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
        text_view_no_reviews = findViewById(R.id.text_view_no_reviews);

        ratingModelArrayList = new ArrayList<>();
        ratingAdapter = new RatingAdapter(ratingModelArrayList, BookDetailsActivity.this);


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
        getAverageRating();
        getRatings();

        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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
        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(bookModel.getFile_path());

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(bookModel.getBook_name());
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,bookModel.getBook_name() + ".pdf");
        downloadID = downloadmanager.enqueue(request);
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                String url = "http://192.168.1.65:81/bookaddict/insert_downloads.php";
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                final RequestQueue requestQueue = Volley.newRequestQueue(BookDetailsActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if (response.trim().equals("success")) {
                            Toast.makeText(BookDetailsActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                        }
                        else if(response.trim().equals("dbError")){
                            Toast.makeText(BookDetailsActivity.this, "Download Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(BookDetailsActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("book_id", String.valueOf(bookModel.getBook_id()));
                        params.put("user_id", String.valueOf(user.getUser_id()));
                        return params;
                    }
                };
                requestQueue.add(stringRequest);

            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    private void read(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("book_details", bookModel);
        Intent intent = new Intent(BookDetailsActivity.this, PdfViewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void openReviewActivity(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("book_details", bookModel);
        Intent intent = new Intent(BookDetailsActivity.this, ReviewsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getRatings(){
        String url = "http://192.168.1.65:81/bookaddict/feedbacks.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    recycler_view_reviews.setVisibility(View.GONE);
                    text_view_no_reviews.setVisibility(View.VISIBLE);
                }
                else{
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonResponse = jsonArray.getJSONObject(i);
                            String user_name = jsonResponse.getString("full_name");
                            String review_date = jsonResponse.getString("given_date");
                            String review_text = jsonResponse.getString("feedback");
                            double rating = jsonResponse.getDouble("star");
                            float f = (float) rating;

                            RatingModel ratingModel = new RatingModel(user_name, review_date, review_text, f);
                            ratingModelArrayList.add(ratingModel);
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                        recycler_view_reviews.setLayoutManager(linearLayoutManager);
                        recycler_view_reviews.setAdapter(ratingAdapter);
                        //recycler_view_futsals.addItemDecoration(new SpacesItemDecoration(20));
                        ratingAdapter.notifyDataSetChanged();



                    }
                    catch (JSONException e) {
                        Toast.makeText(BookDetailsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookDetailsActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("book_id", String.valueOf(bookModel.getBook_id()));
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

    private void getAverageRating(){
        String url = "http://192.168.1.65:81/bookaddict/average_rating.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                }
                else{
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        jsonResponse = jsonArray.getJSONObject(0);
                        double average_rating = jsonResponse.getDouble("average_rating");

                        float f = (float)average_rating;
                        average_rating_bar.setRating(f);
                    }
                    catch (JSONException e) {

                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookDetailsActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("book_id", String.valueOf(bookModel.getBook_id()));
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }


}