package com.cosc3p97.newshub.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cosc3p97.newshub.models.Model;

import java.util.List;

@Dao
public interface BookmarkDao {
    @Query("SELECT * FROM bookmarks")
    List<Model> getAllBookmarks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBookmark(Model model);

    @Delete
    void deleteBookmark(Model model);

    @Query("SELECT * FROM bookmarks WHERE url = :url LIMIT 1")
    Model getBookmark(String url);

    @Query("DELETE FROM bookmarks")
    void clearAll(); // Method to clear all bookmarks
}

