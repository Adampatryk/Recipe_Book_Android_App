package com.example.recipebook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecipeBookProvider extends ContentProvider {

	DBHelper dbHelper = null;

	private int sortByRatingState = 0;
	private String[] sortStates = new String[]{"title asc", "rating asc", "rating desc"};
	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "recipe", 1);
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "recipe/#", 2);
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "ingredient", 3);
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "ingredient/#", 4);
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "recipe_with_ingredients/#", 5);
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "*", 7);
	}

	@Override
	public boolean onCreate() {
		Log.d("g53mdp", "RecipeBookProvider onCreate");
		this.dbHelper = new DBHelper(this.getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.d("g53mdp", "RecipeBookProvider query");

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		switch(uriMatcher.match(uri)) {
			case 2:
				selection = "_id = " + uri.getLastPathSegment();
			case 1:
				return db.query("recipe", projection, selection, selectionArgs, null, null, sortOrder);
			case 4:
				selection = "_id = " + uri.getLastPathSegment();
			case 3:
				return db.query("ingredient", projection, selection, selectionArgs, null, null, sortOrder);
			case 5:
				String[] recipeId = {uri.getLastPathSegment()};
				return db.rawQuery("select r._id as recipe_id, r.title, r.instructions, r.rating, ri.ingredient_id, i.ingredient_name "+
                "from recipe r "+
                        "join recipe_ingredient ri on (r._id = ri.recipe_id)"+
                        "join ingredient i on (ri.ingredient_id = i._id) where r._id == ?",
                recipeId);

			case 7:
				String q7 = "SELECT * FROM people UNION SELECT * FROM animals";
				return db.rawQuery(q7, selectionArgs);
			default:
				return null;
		}

	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
		Log.d("g53mdp", "RecipeBookProvider update");
		SQLiteDatabase db = dbHelper.getWritableDatabase();


		return 0;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
		Log.d("g53mdp", "RecipeBookProvider insert");
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		return null;
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
		Log.d("g53mdp", "RecipeBookProvider delete");
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		return 0;
	}

	@Override
	public String getType(Uri uri) {
		Log.d("g53mdp", "RecipeBookProvider getType");
		String contentType;

		if (uri.getLastPathSegment()==null) {
			contentType = RecipeBookProviderContract.CONTENT_TYPE_MULTIPLE;
		} else {
			contentType = RecipeBookProviderContract.CONTENT_TYPE_SINGLE;
		}

		return contentType;
	}
}
