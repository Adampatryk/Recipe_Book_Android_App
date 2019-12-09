package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class AddRecipeActivity extends AppCompatActivity {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        dbHelper = new DBHelper(this);
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

        // Add to database
        dbHelper.addRecipe(title, rawIngredients, instructions, rating);

        //Go back to recipe list
        finish();
    }

    public void onCancelButton(View v){
        finish();
    }

}
