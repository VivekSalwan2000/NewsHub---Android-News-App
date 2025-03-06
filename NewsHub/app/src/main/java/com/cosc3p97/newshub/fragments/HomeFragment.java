package com.cosc3p97.newshub.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    String API_KEY = Constants.API_KEY;
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Model> modelArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: HomeFragment is created");
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = v.findViewById(R.id.home_recycleView);
        modelArrayList = new ArrayList<>();
        adapter = new Adapter(getContext(), modelArrayList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Log.d(TAG, "RecyclerView and Adapter initialized");
        getNews();

        return v;
    }

    /**
     * Fetch news articles from the API
     */
    void getNews() {
        String language = "en";//getSavedLanguageCode();

        ApiUtilities.getApiInterface().getNews(language, API_KEY).enqueue(new Callback<MainNews>() {
            @Override
            public void onResponse(Call<MainNews> call, Response<MainNews> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "API response received. Number of articles: " + response.body().getArticles().size());

                    // Filter out articles that are null or contain no content
                    ArrayList<Model> validArticles = new ArrayList<>();
                    for (Model model : response.body().getArticles()) {
                        // Validate each article to make sure it's not null
                        if (model != null && !Objects.equals(model.getTitle(), "[Removed]") && !model.getTitle().isEmpty()) {
                            validArticles.add(model);
                        } else {
                            Log.d(TAG, "Null or empty article removed: " + model);
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
                    Log.e(TAG, "API response unsuccessful. Status code: " + response.code() + ", message: " + response.message());
                    Toast.makeText(getContext(), "Failed to fetch news.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: HomeFragment paused");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: HomeFragment resumed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: HomeFragment destroyed");
    }
}
