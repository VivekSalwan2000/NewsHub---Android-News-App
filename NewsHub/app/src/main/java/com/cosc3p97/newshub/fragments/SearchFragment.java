package com.cosc3p97.newshub.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class SearchFragment extends Fragment {

    String API_KEY = Constants.API_KEY;
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Model> modelArrayList;
    private static final String TAG = "SearchFragment";
    private String query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: SearchFragment is created");
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = v.findViewById(R.id.search_recycleView);
        modelArrayList = new ArrayList<>();
        adapter = new Adapter(getContext(), modelArrayList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get search query passed from MainActivity or FilterFragment
        if (getArguments() != null) {
            query = getArguments().getString("query");
            getNews(query);
        }

        return v;
    }

    void getNews(String query) {
        String language = getSavedLanguageCode();

        ApiUtilities.getApiInterface().getNewsFromQuery(language, query, API_KEY).enqueue(new Callback<MainNews>() {
            @Override
            public void onResponse(Call<MainNews> call, Response<MainNews> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "API response received. Number of articles: " + response.body().getArticles().size());

                    ArrayList<Model> validArticles = new ArrayList<>();
                    for (Model model : response.body().getArticles()) {
                        if (model != null && !Objects.equals(model.getTitle(), "[Removed]") && !model.getTitle().isEmpty()) {
                            validArticles.add(model);
                        } else {
                            Log.d(TAG, "Null or empty article removed: " + model);
                        }
                    }

                    if (!validArticles.isEmpty()) {
                        modelArrayList.clear();
                        modelArrayList.addAll(validArticles);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "No valid articles to display");
                    }
                } else {
                    Log.e(TAG, "API response unsuccessful. Status code: " + response.code() + ", message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MainNews> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage(), t);
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
        Log.d(TAG, "onPause: SearchFragment paused");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: SearchFragment resumed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: SearchFragment destroyed");
    }
}
