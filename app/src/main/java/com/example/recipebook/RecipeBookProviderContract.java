package com.example.recipebook;

import android.net.Uri;

public class RecipeBookProviderContract {

	public static final String AUTHORITY = "package com.example.recipebook.RecipeBookProvider";

	public static final Uri RECIPE_WITH_INGREDIENTS = Uri.parse("content://"+AUTHORITY+"/recipe_with_ingredients/#");
	public static final Uri RECIPE_URI = Uri.parse("content://"+AUTHORITY+"/recipe");
	public static final Uri INGREDIENT_URI = Uri.parse("content://"+AUTHORITY+"/ingredient");
	public static final Uri ALL_URI = Uri.parse("content://"+AUTHORITY+"/");

	public static final String _ID = "_id";

	public static final String INGREDIENTS_LIST = "ingredients_list";

	//TABLES
	public static final String RECIPE_TABLE = "recipe";
	public static final String INGREDIENT_TABLE = "ingredient";
	public static final String RECIPE_INGREDIENTS_TABLE = "recipe_ingredient";

	//RECIPE TABLE FIELDS
	public static final String TITLE = "title";
	public static final String INSTRUCTIONS = "instructions";
	public static final String RATING = "rating";

	//INGREDIENT TABLE FIELDS
	public static final String INGREDIENT_NAME = "ingredient_name";

	//RECIPE_INGREDIENTS FIELDS
	public static final String RECIPE_ID = "recipe_id";
	public static final String INGREDIENT_ID = "ingredient_id";

	public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/RecipeBookProvider.data.text";
	public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/RecipeBookProvider.data.text";
}
