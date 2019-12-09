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

import java.util.ArrayList;

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
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "ingredient", 3);
		uriMatcher.addURI(RecipeBookProviderContract.AUTHORITY, "recipe_with_ingredients", 5);
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
			case 1:
				return db.query(RecipeBookProviderContract.RECIPE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
			case 3:
				return db.query(RecipeBookProviderContract.INGREDIENT_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
			case 5:
				return db.rawQuery("select r._id as recipe_id, r.title, r.instructions, r.rating, ri.ingredient_id, i.ingredient_name "+ //Change in future
                		"from recipe r "+
                        "join recipe_ingredient ri on (r._id = ri.recipe_id)"+
                        "join ingredient i on (ri.ingredient_id = i._id) where r._id == ?",
						selectionArgs);
			default:
				return null;
		}

	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
		Log.d("g53mdp", "RecipeBookProvider update");
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		db.update("recipe", contentValues, s, strings);
		getContext().getContentResolver().notifyChange(uri, null);

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
	public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] stringArguments) {
		Log.d("g53mdp", "RecipeBookProvider delete");
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		int id;
		if (stringArguments != null && stringArguments.length != 0) {
			id = Integer.parseInt(stringArguments[0]);
		} else {
			return 0;
		}

		//Delete recipe in recipe table
		if (db.delete(RecipeBookProviderContract.RECIPE_TABLE, RecipeBookProviderContract._ID + "= ?", stringArguments) == 0){
			Log.d("g53mdp","RecipeBookProvider delete id: " + id + "... nothing deleted");
			return 0;
		}

		//Array for the ingredients ids in the recipe
		ArrayList<Integer> recipeIngredientIds = new ArrayList<>();

		//Find the ids of the ingredients for the recipe and populate the array above
		Cursor c = db.query(RecipeBookProviderContract.RECIPE_INGREDIENTS_TABLE, new String[]{RecipeBookProviderContract.INGREDIENT_ID}, "recipe_id = ?", new String[]{""+id}, null, null, null);
		if (c.moveToFirst())
		{
			do
			{
				recipeIngredientIds.add(Integer.parseInt(c.getString(0)));
			} while(c.moveToNext());
		}
		c.close();

		Log.d("g53mdp", "RecipeBookProvider delete ingredientIds: " + recipeIngredientIds.toString());

		//Delete associations with ingredients in recipe_ingredient table
		db.delete(RecipeBookProviderContract.RECIPE_INGREDIENTS_TABLE, RecipeBookProviderContract.RECIPE_ID + "= ?", new String[]{""+id});

		//Check if ingredient still exists in associations table, if not delete ingredient from ingredient table
		for (int recipeIngredientId : recipeIngredientIds) {
			c = db.rawQuery("Select * from " + RecipeBookProviderContract.RECIPE_INGREDIENTS_TABLE + " where " + RecipeBookProviderContract.INGREDIENT_ID + "=?", new String[]{"" + recipeIngredientId});

			//If no recipes use the ingredient, delete it
			if (c.getCount() <= 0) {
				db.delete(RecipeBookProviderContract.INGREDIENT_TABLE, RecipeBookProviderContract._ID + "=?", new String[]{"" + recipeIngredientId});
				c.close();
			}
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return 1;
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
