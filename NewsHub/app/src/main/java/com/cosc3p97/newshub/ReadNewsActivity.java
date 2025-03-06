package com.cosc3p97.newshub;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cosc3p97.newshub.models.SummarizerUtil;

public class ReadNewsActivity extends AppCompatActivity {
    WebView webView;
    Button summarizeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);

        webView = findViewById(R.id.webView);
        summarizeButton = findViewById(R.id.summarizeButton);

        String url = getIntent().getStringExtra("URL");

        webView.getSettings().setJavaScriptEnabled(true);
        final Activity activity = this;

        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        webView.loadUrl(url);

        // Floating Button Click Listener
        summarizeButton.setOnClickListener(view -> {
            if (url != null && !url.isEmpty()) {
                SummarizerUtil.summarizeWebPage(url, this::showSummaryDialog);
            } else {
                Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Show the summary in a pop-up dialog
    private void showSummaryDialog(String summary) {
        new AlertDialog.Builder(this)
                .setTitle("Summary")
                .setMessage(summary)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
