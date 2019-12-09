package com.example.recipebook;

import android.net.Uri;

public class RecipeBookProviderContract {

	public static final String AUTHORITY = "package com.example.recipebook.RecipeBookProvider";

	public static final Uri RECIPE_WITH_INGREDIENTS = Uri.parse("content://"+AUTHORITY+"/recipe_with_ingredients/#");
	public static final Uri RECIPE_URI = Uri.parse("content://"+AUTHORITY+"/recipe");
	public static final Uri ALL_URI = Uri.parse("content://"+AUTHORITY+"/");

	public static final String _ID = "_id";
	public static final String TITLE = "title";
	public static final String INSTRUCTIONS = "instructions";
	public static final String RATING = "rating";
	public static final String INGREDIENT = "ingredient";

	public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/RecipeBookProvider.data.text";
	public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/RecipeBookProvider.data.text";
}
