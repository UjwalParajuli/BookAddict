package com.example.bookaddict.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookaddict.BookDetailsActivity;
import com.example.bookaddict.R;
import com.example.bookaddict.adapters.BookAdapter;
import com.example.bookaddict.models.BookModel;
import com.example.bookaddict.utils.ErrorUtils;
import com.example.bookaddict.utils.ItemClickSupport;
import com.example.bookaddict.utils.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFragment extends Fragment {
    private SearchView search_view_search_fragment;
    private RecyclerView recycler_view_search_results;
    private LinearLayout linear_layout_search_results;
    private ArrayList<BookModel> bookModelArrayList;
    private BookAdapter bookAdapter;
    private ProgressBar progress_bar_search_fragment;
    private TextView text_view_search_not_found;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        search_view_search_fragment = view.findViewById(R.id.search_view_search_fragment);
        recycler_view_search_results = view.findViewById(R.id.recycler_view_search_results);
        linear_layout_search_results = view.findViewById(R.id.linear_layout_search_results);
        progress_bar_search_fragment = view.findViewById(R.id.progress_bar_search_fragment);
        text_view_search_not_found = view.findViewById(R.id.text_view_search_not_found);

        bookModelArrayList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookModelArrayList, getContext());

        search_view_search_fragment.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    private void search(String keyword){
        String url = "http://192.168.1.65:81/bookaddict/search_query.php";
        progress_bar_search_fragment.setVisibility(View.VISIBLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress_bar_search_fragment.setVisibility(View.GONE);
                if (response.trim().equals("not_found")) {
                    linear_layout_search_results.setVisibility(View.GONE);
                    text_view_search_not_found.setVisibility(View.VISIBLE);
                }
                else{
                    bookModelArrayList.clear();
                    text_view_search_not_found.setVisibility(View.GONE);
                    linear_layout_search_results.setVisibility(View.VISIBLE);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonResponse = jsonArray.getJSONObject(i);

                            int book_id = jsonResponse.getInt("book_id");
                            int pages = jsonResponse.getInt("pages");
                            String book_name = jsonResponse.getString("book_name");
                            String author = jsonResponse.getString("author");
                            String genre = jsonResponse.getString("genre");
                            String published_date = jsonResponse.getString("published_date");
                            String file = jsonResponse.getString("file");
                            String cover_image = jsonResponse.getString("cover_image");

                            BookModel bookModel = new BookModel(book_id, pages, book_name, author, genre, published_date, file, cover_image);
                            bookModelArrayList.add(bookModel);
                        }

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                        recycler_view_search_results.setLayoutManager(gridLayoutManager);

                        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
                        recycler_view_search_results.addItemDecoration(itemDecoration);
                        recycler_view_search_results.setAdapter(bookAdapter);
                        bookAdapter.notifyDataSetChanged();

                        ItemClickSupport.addTo(recycler_view_search_results).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                BookModel bookModel = bookModelArrayList.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("book_details", bookModel);
                                Intent intent = new Intent(getContext(), BookDetailsActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }

                    catch (JSONException e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress_bar_search_fragment.setVisibility(View.GONE);
                Toast.makeText(getContext(), ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("search_query", keyword);
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }
}
