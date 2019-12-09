package com.example.recipebook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecipeBookProvider extends ContentProvider {

	DBHelper dbHelper = null;

	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "recipes", 1);
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "recipe/#", 2);
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "ingredients", 3);
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "ingredient/#", 4);
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "food", 5);
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "food/#", 6);
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "*", 7);
	}

	@Override
	public boolean onCreate() {
		Log.d("g53mdp", "RecipeBookProvider onCreate");
		this.dbHelper = new DBHelper(this.getContext());
		return true;
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
		return null;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
		return 0;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
		return null;
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {

		String contentType;

		if (uri.getLastPathSegment()==null) {
			contentType = RecipeBookProviderContract.CONTENT_TYPE_MULTIPLE;
		} else {
			contentType = RecipeBookProviderContract.CONTENT_TYPE_SINGLE;
		}

		return contentType;
	}
}
