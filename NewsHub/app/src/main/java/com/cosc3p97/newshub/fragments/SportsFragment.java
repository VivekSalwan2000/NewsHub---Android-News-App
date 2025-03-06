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
import com.cosc3p97.newshub.api.ApiUtilities;
import com.cosc3p97.newshub.Constants;
import com.cosc3p97.newshub.models.MainNews;
import com.cosc3p97.newshub.models.Model;
import com.cosc3p97.newshub.R;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SportsFragment extends Fragment {

    String API_KEY = Constants.API_KEY;
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Model> modelArrayList;

    private static final String TAG = "SportsFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sports, null);

        recyclerView = v.findViewById(R.id.sports_recycleView);
        modelArrayList = new ArrayList<>();
        adapter = new Adapter(getContext(), modelArrayList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getNews();

        return v;
    }

    void getNews() {
        String category = "sports";
        int pageSize = 100;
        String language = "en";//getSavedLanguageCode();

        ApiUtilities.getApiInterface().getCategory(language, category, pageSize, API_KEY).enqueue(new Callback<MainNews>() {
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