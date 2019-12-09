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
	private int sortByRatingState = 0;
	private String[] sortStates = new String[]{"title asc", "rating asc", "rating desc"};

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

	public int getSortByRatingState(){
		return sortByRatingState;
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

	}

	public ArrayList<String[]> getAllIngredients() {
		ArrayList<String[]> ingredients = new ArrayList<String[]>();

		Cursor c = getWritableDatabase().query("ingredient", new String[]{"_id", "ingredient_name"},
				null, null, null, null, null);
		if (c.moveToFirst()) {
			do {
				//Add ingredient_name to array
				ingredients.add(
						new String[]{
								c.getString(0),
								c.getString(1)
						}
				);
			} while (c.moveToNext());
		}

		return ingredients;
	}

	public int getMaxId(String table){
		ArrayList<String> tables = new ArrayList<>(Arrays.asList("recipe", "ingredient", "recipe_ingredient"));

		//Check if table exists
		if (!tables.contains(table)){
			return -1;
		}

		String query = "SELECT MAX(_id) AS max_id FROM " + table;
		Cursor cursor = db.rawQuery(query, null);

		int max_id = 0;
		if (cursor.moveToFirst())
		{
			do
			{
				max_id = cursor.getInt(0);
			} while(cursor.moveToNext());
		}

		cursor.close();
		return max_id;
	}

	public void updateRating(int id, int rating){
		db = getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put("rating", rating);

		db.update("recipe", cv, "_id=?", new String[]{""+id});
	}

	public void addRecipe(String title, String rawIngredients, String instructions, int rating){
		db = getWritableDatabase();

		String[] recipeIngredients = rawIngredients.split("\\r?\\n");
		ArrayList<String[]> allIngredients = getAllIngredients();
		int recipe_id = -1;

		Log.d("g53mdp", "DBHelper recipeIngredients: " + recipeIngredients.toString());
		Log.d("g53mdp", "DBHelper allIngredients: " + allIngredients.toString());
		Log.d("g53mdp", "DBHelper title: " + title);
		Log.d("g53mdp", "DBHelper rawIngredients: " + rawIngredients);
		Log.d("g53mdp", "DBHelper rating: " + rating);

		//Add recipe to recipe table
		db.execSQL("INSERT INTO recipe (title, instructions, rating)" +
				"VALUES " +
				"('" + title + "','" + instructions + "','" + rating + "');");

		recipe_id = getMaxId("recipe");

		//Add ingredient to ingredient table if it doesn't already exist

		//For each ingredient in the new recipe
		for (String recipeIngredient: recipeIngredients){
			int ingredient_id = -1;

			//Ingredient set to not exist unless a match is found
			boolean exists = false;
			//For each ingredient in the database
			for (String[] dbIngredient: allIngredients){
				//If the name of the ingredient in the database is the same is the currentRecipeIngredient in outer for loop:
				if (dbIngredient[1].equals(recipeIngredient)){
					exists = true; //Set the ingredient to exist (as it is in the database)
					ingredient_id = Integer.parseInt(dbIngredient[0]); //Set the id of the ingredient so i can use it in the association
					break;//Stop looking through the database
				}
			}
			//If ingredient doesn't exist, insert it into ingredient table
			if (!exists){
				db.execSQL("INSERT INTO ingredient (ingredient_name)" +
						"VALUES " +
						"('" + recipeIngredient + "');");
				//Get the id of the ingredient that was just added so I can use it in the association
				ingredient_id = getMaxId("ingredient");
			}

			//Add association
			db.execSQL("INSERT INTO recipe_ingredient (recipe_id, ingredient_id)" +
					"VALUES " +
					"('" + recipe_id + "','" + ingredient_id  + "');");

		}

	}

    public ArrayList<ArrayList<String>> getRecipe(int id){
        ArrayList<String> recordData = new ArrayList<>();
        ArrayList<String> ingredients = new ArrayList<>();

        String [] recipeId = { "" + id };
        Cursor c;
        c = getWritableDatabase().rawQuery("select r._id as recipe_id, r.title, r.instructions, r.rating, ri.ingredient_id, i.ingredient_name "+
                "from recipe r "+
                        "join recipe_ingredient ri on (r._id = ri.recipe_id)"+
                        "join ingredient i on (ri.ingredient_id = i._id) where r._id == ?",
                recipeId);

        Log.d("g53mdp", "DBHelper getRecipe c.getCount(): " + c.getCount());

		if (c.moveToFirst())
		{
			do
			{
//				Log.d("g53mdp", "DBHelper r.name: " + c.getString(1));
//				Log.d("g53mdp", "DBHelper r.instructions: " + c.getString(2));
//				Log.d("g53mdp", "DBHelper r.rating: " + c.getString(3));
//				Log.d("g53mdp", "DBHelper i.ingredientname: " + c.getString(5));

				if (recordData.isEmpty()){
					recordData.add(c.getString(1)); // Title
					recordData.add(c.getString(2)); // Instructions
					recordData.add(c.getString(3)); // Rating
				}
				ingredients.add(c.getString(5));

			} while(c.moveToNext());
		}

		c.close();

        return new ArrayList<>(Arrays.asList(recordData, ingredients));
    }

    public void deleteRecipe(int id){
		db = getWritableDatabase();

		//Delete recipe in recipe table
		if (db.delete("recipe", "_id = ?", new String[]{"" + id}) == 0){
			Log.d("g53mdp","DBHelper deleteRecipe id: " + id + "... nothing deleted");
			return;
		}

		//Array for the ingredients ids in the recipe
		ArrayList<Integer> recipeIngredientIds = new ArrayList<>();

		//Find the ids of the ingredients for the recipe and populate the array above
		Cursor c = db.query("recipe_ingredient", new String[]{"ingredient_id"}, "recipe_id = ?", new String[]{""+id}, null, null, null);
		if (c.moveToFirst())
		{
			do
			{
				recipeIngredientIds.add(Integer.parseInt(c.getString(0)));
			} while(c.moveToNext());
		}
		c.close();

		Log.d("g53mdp", "DBHelper deleteRecipe ingredientIds: " + recipeIngredientIds.toString());

		//Delete associations with ingredients in recipe_ingredient table
		db.delete("recipe_ingredient", "recipe_id = ?", new String[]{""+id});

		//Check if ingredient still exists in associations table, if not delete ingredient from ingredient table
		for (int recipeIngredientId : recipeIngredientIds){
			c = db.rawQuery("Select * from recipe_ingredient where ingredient_id = ?", new String[]{""+recipeIngredientId});

			//If no recipes use the ingredient, delete it
			if(c.getCount() <= 0){
				db.delete("ingredient", "_id=?", new String[]{""+recipeIngredientId});
				c.close();
			}
		}

	}

	public Cursor getRecipeCursor(boolean sortByRatingChange){

		if (sortByRatingChange){
			if (++sortByRatingState == 3){
				sortByRatingState = 0;
			}
		}

		return getWritableDatabase().query("recipe", new String[] { "_id", "title", "instructions", "rating" },
				null, null, null, null, sortStates[sortByRatingState]);
	}

	public Cursor getIngredientsCursor(){
		return getWritableDatabase().query("ingredient", new String[] {"_id", "ingredient_name"},
				null, null, null, null, null);
	}

}