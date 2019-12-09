package com.example.recipebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class DBHelper extends SQLiteOpenHelper {

	SQLiteDatabase db;
//	private int sortByRatingState = 0;
//	private String[] sortStates = new String[]{"title asc", "rating asc", "rating desc"};

	public DBHelper(Context context) {
		super(context, "recipes", null, 1);
	}

	public void onCreate(SQLiteDatabase database) {
		db = database;
		Log.d("g53mdp", "DBHelper onCreate Creating Tables");
		db.execSQL("CREATE TABLE recipe (" +
				"_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
				"title VARCHAR(128) NOT NULL," +
				"instructions VARCHAR(512) NOT NULL," +
				"rating INTEGER);");

		db.execSQL("CREATE TABLE ingredient (" +
				" _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
				"ingredient_name VARCHAR(128) NOT NULL);");

		db.execSQL("CREATE TABLE recipe_ingredient (" +
				" recipe_id INT NOT NULL," +
				" ingredient_id INT NOT NULL," +
				" CONSTRAINT fk1 FOREIGN KEY (recipe_id) REFERENCES recipes (_id)," +
				" CONSTRAINT fk2 FOREIGN KEY (ingredient_id) REFERENCES ingredients (_id)," +
				" CONSTRAINT _id PRIMARY KEY (recipe_id, ingredient_id) );");

		Log.d("g53mdp", "DBHelper onCreate Created Tables");
	}

//	public int getSortByRatingState(){
//		return sortByRatingState;
//	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

	}


//	public void addRecipe(String title, String rawIngredients, String instructions, int rating){
//		db = getWritableDatabase();
//
//		String[] recipeIngredients = rawIngredients.split("\\r?\\n");
//		ArrayList<String[]> allIngredients = getAllIngredients();
//		int recipe_id = -1;
//
//		Log.d("g53mdp", "DBHelper recipeIngredients: " + recipeIngredients.toString());
//		Log.d("g53mdp", "DBHelper allIngredients: " + allIngredients.toString());
//		Log.d("g53mdp", "DBHelper title: " + title);
//		Log.d("g53mdp", "DBHelper rawIngredients: " + rawIngredients);
//		Log.d("g53mdp", "DBHelper rating: " + rating);
//
//		//Add recipe to recipe table
//		db.execSQL("INSERT INTO recipe (title, instructions, rating)" +
//				"VALUES " +
//				"('" + title + "','" + instructions + "','" + rating + "');");
//
//		recipe_id = getMaxId("recipe");
//
//		//Add ingredient to ingredient table if it doesn't already exist
//
//		//For each ingredient in the new recipe
//		for (String recipeIngredient: recipeIngredients){
//			int ingredient_id = -1;
//
//			//Ingredient set to not exist unless a match is found
//			boolean exists = false;
//			//For each ingredient in the database
//			for (String[] dbIngredient: allIngredients){
//				//If the name of the ingredient in the database is the same is the currentRecipeIngredient in outer for loop:
//				if (dbIngredient[1].equals(recipeIngredient)){
//					exists = true; //Set the ingredient to exist (as it is in the database)
//					ingredient_id = Integer.parseInt(dbIngredient[0]); //Set the id of the ingredient so i can use it in the association
//					break;//Stop looking through the database
//				}
//			}
//			//If ingredient doesn't exist, insert it into ingredient table
//			if (!exists){
//				db.execSQL("INSERT INTO ingredient (ingredient_name)" +
//						"VALUES " +
//						"('" + recipeIngredient + "');");
//				//Get the id of the ingredient that was just added so I can use it in the association
//				ingredient_id = getMaxId("ingredient");
//			}
//
//			//Add association
//			db.execSQL("INSERT INTO recipe_ingredient (recipe_id, ingredient_id)" +
//					"VALUES " +
//					"('" + recipe_id + "','" + ingredient_id  + "');");
//
//		}
//
//	}

	public Cursor getIngredientsCursor(){
		return getWritableDatabase().query("ingredient", new String[] {"_id", "ingredient_name"},
				null, null, null, null, null);
	}

}