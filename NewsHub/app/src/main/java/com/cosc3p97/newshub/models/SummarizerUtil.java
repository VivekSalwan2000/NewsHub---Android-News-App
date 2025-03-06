package com.cosc3p97.newshub.models;

import android.os.Handler;
import android.os.Looper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.*;

public class SummarizerUtil {
    private static final String API_URL = " "; // Replace with actual API URL
    private static final String API_KEY = " "; // Replace with actual API Key

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final Handler uiHandler = new Handler(Looper.getMainLooper());

    public interface SummarizationCallback {
        void onSummaryReceived(String summary);
    }

    public static void summarizeWebPage(String url, SummarizationCallback callback) {
        executorService.execute(() -> {
            try {
                Document doc = Jsoup.connect(url).get();
                String extractedText = doc.text();

                // Limit text to avoid API restrictions
                if (extractedText.length() > 1024) {
                    extractedText = extractedText.substring(0, 1024);
                }

                sendToHuggingFace(extractedText, callback);
            } catch (IOException e) {
                uiHandler.post(() -> callback.onSummaryReceived("⚠️ Error fetching webpage: " + e.getMessage()));
            }
        });
    }

    private static void sendToHuggingFace(String inputText, SummarizationCallback callback) {
        executorService.execute(() -> {
            try {
                OkHttpClient client = new OkHttpClient();

                String json = "{"
                        + "\"inputs\": \"" + inputText.replace("\"", "\\\"") + "\","
                        + "\"parameters\": {"
                        + "\"max_length\": 300,"
                        + "\"min_length\": 100,"
                        + "\"do_sample\": false"
                        + "}"
                        + "}";

                RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
                Request request = new Request.Builder()
                        .url(API_URL)
                        .addHeader("Authorization", "Bearer " + API_KEY)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String responseBody = response.body() != null ? response.body().string() : "Error: Empty response";

                // Parse and format the summary text
                String formattedSummary = parseSummary(responseBody);

                uiHandler.post(() -> callback.onSummaryReceived(formattedSummary));

            } catch (IOException e) {
                uiHandler.post(() -> callback.onSummaryReceived("⚠️ Error calling API: " + e.getMessage()));
            }
        });
    }

    /**
     * Parses JSON response and extracts the summary text.
     */
    private static String parseSummary(String jsonResponse) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            if (jsonArray.length() > 0) {
                JSONObject summaryObject = jsonArray.getJSONObject(0);
                return "📝 " + summaryObject.getString("summary_text"); // Clean formatted summary
            }
        } catch (JSONException e) {
            return "⚠️ Error parsing summary: " + e.getMessage();
        }
        return "No summary available.";
    }
}
