package com.cosc3p97.newshub.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cosc3p97.newshub.Adapter;
import com.cosc3p97.newshub.Constants;
import com.cosc3p97.newshub.R;
import com.cosc3p97.newshub.api.ApiUtilities;
import com.cosc3p97.newshub.models.MainNews;
import com.cosc3p97.newshub.models.Model;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment {

    private static final String TAG = "FilterFragment";
    private String query; // To store the query passed via Bundle
    String API_KEY = Constants.API_KEY;
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Model> modelArrayList;
    Button filterRelevancyButton, filterPopularityButton, filterPublishedAtButton;
    TextView sortArticlesByTextView;

    public static FilterFragment newInstance(String query) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_filter, container, false);

        recyclerView = v.findViewById(R.id.filter_recycleView);
        filterRelevancyButton = v.findViewById(R.id.filterRelevancyButton);
        filterPopularityButton = v.findViewById(R.id.filterPopularityButton);
        filterPublishedAtButton = v.findViewById(R.id.filterPublishedAtButton);
        sortArticlesByTextView = v.findViewById(R.id.SortArticlesBy);

        modelArrayList = new ArrayList<>();
        adapter = new Adapter(getContext(), modelArrayList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get search query passed from MainActivity or FilterFragment
        if (getArguments() != null) {
            query = getArguments().getString("query");
        }

        // Set click listeners for each button
        filterRelevancyButton.setOnClickListener(view -> {
            Log.d(TAG, "Relevancy filter selected. Query: " + query);
            hideButtonsAndFetchNews("relevancy");
        });

        filterPopularityButton.setOnClickListener(view -> {
            Log.d(TAG, "Popularity filter selected. Query: " + query);
            hideButtonsAndFetchNews("popularity");
        });

        filterPublishedAtButton.setOnClickListener(view -> {
            Log.d(TAG, "Published At filter selected. Query: " + query);
            hideButtonsAndFetchNews("publishedAt");
        });

        return v;
    }

    private void hideButtonsAndFetchNews(String sortBy) {
        // Hide the filter buttons

        filterRelevancyButton.setVisibility(View.GONE);
        filterPopularityButton.setVisibility(View.GONE);
        filterPublishedAtButton.setVisibility(View.GONE);
        sortArticlesByTextView.setVisibility(View.GONE);

        // Fetch the news data
        getNews(sortBy);
    }

    private void getNews(String sortBy) {

        String language = getSavedLanguageCode();
        if (query == null || query.trim().isEmpty()) {
            Toast.makeText(getContext(), "Query is empty or null.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Query is null or empty. Cannot make API call.");
            return;
        }

        ApiUtilities.getApiInterface().getNewsWithSortBy(query, language, sortBy, API_KEY).enqueue(new Callback<MainNews>() {
            @Override
            public void onResponse(Call<MainNews> call, Response<MainNews> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MainNews mainNews = response.body();
                    Log.d(TAG, "API Response successful. Articles count: " + mainNews.getArticles().size());

                    // Filter out articles that are null, empty, or contain unwanted content
                    ArrayList<Model> validArticles = new ArrayList<>();
                    for (Model model : mainNews.getArticles()) {
                        // Validate each article to make sure it's not null, empty, or has an unwanted title
                        if (model != null && model.getTitle() != null && !model.getTitle().isEmpty()
                                && !model.getTitle().equals("[Removed]")) {
                            validArticles.add(model);
                        } else {
                            Log.d(TAG, "Null, empty, or unwanted article removed: " + model);
                        }
                    }

                    // Update only the valid articles
                    if (!validArticles.isEmpty()) {
                        modelArrayList.clear();  // Clear old list before adding new data
                        modelArrayList.addAll(validArticles);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "No valid articles to display");
                        Toast.makeText(getContext(), "No valid news found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "API Response unsuccessful. Code: " + response.code() + ", Message: " + response.message());
                    Toast.makeText(getContext(), "Failed to fetch news. Try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MainNews> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getSavedLanguageCode() {
        return requireContext()
                .getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
                .getString("language_code", "en");
    }
}
