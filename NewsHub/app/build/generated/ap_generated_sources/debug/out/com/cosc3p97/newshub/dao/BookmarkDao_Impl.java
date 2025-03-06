package com.cosc3p97.newshub.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.cosc3p97.newshub.models.Model;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class BookmarkDao_Impl implements BookmarkDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Model> __insertionAdapterOfModel;

  private final EntityDeletionOrUpdateAdapter<Model> __deletionAdapterOfModel;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public BookmarkDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfModel = new EntityInsertionAdapter<Model>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `bookmarks` (`url`,`author`,`title`,`description`,`urlToImage`,`publishedAt`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Model entity) {
        if (entity.getUrl() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getUrl());
        }
        if (entity.getAuthor() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getAuthor());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTitle());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        if (entity.getUrlToImage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getUrlToImage());
        }
        if (entity.getPublishedAt() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPublishedAt());
        }
      }
    };
    this.__deletionAdapterOfModel = new EntityDeletionOrUpdateAdapter<Model>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `bookmarks` WHERE `url` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Model entity) {
        if (entity.getUrl() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getUrl());
        }
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM bookmarks";
        return _query;
      }
    };
  }

  @Override
  public void insertBookmark(final Model model) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfModel.insert(model);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteBookmark(final Model model) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfModel.handle(model);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void clearAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfClearAll.release(_stmt);
    }
  }

  @Override
  public List<Model> getAllBookmarks() {
    final String _sql = "SELECT * FROM bookmarks";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
      final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfUrlToImage = CursorUtil.getColumnIndexOrThrow(_cursor, "urlToImage");
      final int _cursorIndexOfPublishedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "publishedAt");
      final List<Model> _result = new ArrayList<Model>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Model _item;
        final String _tmpUrl;
        if (_cursor.isNull(_cursorIndexOfUrl)) {
          _tmpUrl = null;
        } else {
          _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
        }
        final String _tmpAuthor;
        if (_cursor.isNull(_cursorIndexOfAuthor)) {
          _tmpAuthor = null;
        } else {
          _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
        }
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        final String _tmpUrlToImage;
        if (_cursor.isNull(_cursorIndexOfUrlToImage)) {
          _tmpUrlToImage = null;
        } else {
          _tmpUrlToImage = _cursor.getString(_cursorIndexOfUrlToImage);
        }
        final String _tmpPublishedAt;
        if (_cursor.isNull(_cursorIndexOfPublishedAt)) {
          _tmpPublishedAt = null;
        } else {
          _tmpPublishedAt = _cursor.getString(_cursorIndexOfPublishedAt);
        }
        _item = new Model(_tmpAuthor,_tmpTitle,_tmpDescription,_tmpUrl,_tmpUrlToImage,_tmpPublishedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Model getBookmark(final String url) {
    final String _sql = "SELECT * FROM bookmarks WHERE url = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (url == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, url);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
      final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfUrlToImage = CursorUtil.getColumnIndexOrThrow(_cursor, "urlToImage");
      final int _cursorIndexOfPublishedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "publishedAt");
      final Model _result;
      if (_cursor.moveToFirst()) {
        final String _tmpUrl;
        if (_cursor.isNull(_cursorIndexOfUrl)) {
          _tmpUrl = null;
        } else {
          _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
        }
        final String _tmpAuthor;
        if (_cursor.isNull(_cursorIndexOfAuthor)) {
          _tmpAuthor = null;
        } else {
          _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
        }
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        final String _tmpUrlToImage;
        if (_cursor.isNull(_cursorIndexOfUrlToImage)) {
          _tmpUrlToImage = null;
        } else {
          _tmpUrlToImage = _cursor.getString(_cursorIndexOfUrlToImage);
        }
        final String _tmpPublishedAt;
        if (_cursor.isNull(_cursorIndexOfPublishedAt)) {
          _tmpPublishedAt = null;
        } else {
          _tmpPublishedAt = _cursor.getString(_cursorIndexOfPublishedAt);
        }
        _result = new Model(_tmpAuthor,_tmpTitle,_tmpDescription,_tmpUrl,_tmpUrlToImage,_tmpPublishedAt);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
