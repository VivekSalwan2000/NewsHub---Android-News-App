package com.cosc3p97.newshub.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cosc3p97.newshub.dao.AppDatabase;
import com.cosc3p97.newshub.R;

public class SettingsFragment extends Fragment {

    private TextView setLanguage;
    private TextView clearBookmarks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        // Bind UI elements
        setLanguage = v.findViewById(R.id.setLanguage);
        clearBookmarks = v.findViewById(R.id.clearBookmarks);

        // Set up listeners for the options
        setLanguage.setOnClickListener(view -> showLanguageDialog());
        clearBookmarks.setOnClickListener(view -> clearAllBookmarks());

        return v;
    }

    /**
     * Show a dialog to allow the user to change the app's language.
     */
    private void showLanguageDialog() {
        // Mapping of ISO-639-1 language codes to human-readable language names
        String[] languageNames = {
                "Arabic", "German", "English", "Spanish", "French",
                "Hebrew", "Italian", "Dutch", "Norwegian", "Portuguese",
                "Russian", "Swedish", "Chinese"
        };

        String[] languageCodes = {
                "ar", "de", "en", "es", "fr",
                "he", "it", "nl", "no", "pt",
                "ru", "sv", "zh"
        };

        new AlertDialog.Builder(requireContext())
                .setTitle("Choose Language")
                .setItems(languageNames, (dialog, which) -> {
                    // Get selected language code
                    String selectedLanguageCode = languageCodes[which];

                    // Save the language code in preferences (optional)
                    saveSelectedLanguage(selectedLanguageCode);

                    // Notify the user
                    Toast.makeText(getContext(),
                            "Language set to " + languageNames[which],
                            Toast.LENGTH_SHORT).show();

                    // Update the app's language (placeholder logic)
                    updateAppLanguage(selectedLanguageCode);
                })
                .show();
    }

    /**
     * Saves the selected language code in SharedPreferences.
     */
    private void saveSelectedLanguage(String languageCode) {
        requireContext()
                .getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
                .edit()
                .putString("language_code", languageCode)
                .apply();

    }

    /**
     * Updates the app's language (placeholder method).
     */
    private void updateAppLanguage(String languageCode) {
        // Implement logic to update app localization or restart required activities.
        // For full implementation, consider using a library like `Android Localization` or restart the app.
    }


    /**
     * Clear all bookmarks from the Room database.
     */
    private void clearAllBookmarks() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Clear All Bookmarks")
                .setMessage("Are you sure you want to clear all bookmarks?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    new Thread(() -> {
                        AppDatabase database = AppDatabase.getDatabase(requireContext());
                        database.bookmarkDao().clearAll();

                        // UI Feedback
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "All bookmarks cleared", Toast.LENGTH_SHORT).show()
                        );
                    }).start();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
