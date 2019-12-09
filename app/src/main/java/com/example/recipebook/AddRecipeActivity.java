package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class AddRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
    }

    //Add new recipe to the database
    public void onAddButton(View v){

        //Collect title
        String title = ((TextView) findViewById(R.id.recipeTitleText)).getText().toString();

        //Collect ingredients and parse
        String rawIngredients = ((TextView) findViewById(R.id.ingredientsText)).getText().toString();

        //Collect instructions
        String instructions = ((TextView) findViewById(R.id.instructionsText)).getText().toString();

        //Collect rating
        int rating = Integer.parseInt(((TextView) findViewById(R.id.addRatingText)).getText().toString());

        //Error checking?

        Log.d("g53mdp", "AddRecipeActivity onAddButton title: " + title);
        Log.d("g53mdp", "AddRecipeActivity onAddButton rawIngredients: " + rawIngredients);
        Log.d("g53mdp", "AddRecipeActivity onAddButton instructions: " + instructions);
        Log.d("g53mdp", "AddRecipeActivity onAddButton rating: " + rating);

        //Pack items into content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeBookProviderContract.TITLE, title);
        contentValues.put(RecipeBookProviderContract.INSTRUCTIONS, instructions);
        contentValues.put(RecipeBookProviderContract.RATING, rating);
        contentValues.put(RecipeBookProviderContract.INGREDIENTS_LIST, rawIngredients);

        // Use content provider
        getContentResolver().insert(RecipeBookProviderContract.RECIPE_WITH_INGREDIENTS, contentValues);

        //Go back to recipe list
        finish();
    }

    public void onCancelButton(View v){
        finish();
    }

}
