package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeDetailsActivity extends AppCompatActivity {
    int id;

    DBHelper dbHelper;
    TextView recipeTitleText;
    TextView ingredientsText;
    TextView instructionsText;
    EditText ratingEditText;
    TextView ratingTextView;

    Button updateRatingButton;
    Button saveRatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        id = Integer.parseInt(getIntent().getExtras().getString("id"));
        Log.d("g53mdp", "RecipeDetailsActivity onCreate ID: " + id);


        updateRatingButton = findViewById(R.id.updateRatingButton);
        saveRatingButton = findViewById(R.id.saveRatingButton);

        ratingEditText = findViewById(R.id.ratingEditText);
        ratingTextView = findViewById(R.id.ratingTextView);
        recipeTitleText = findViewById(R.id.recipeTitleText);
        ingredientsText = findViewById(R.id.ingredientsText);
        instructionsText = findViewById(R.id.instructionsText);

        queryRecipeWithIngredients(id);
    }

    public void queryRecipeWithIngredients(int id){

        ArrayList<String> recordData = new ArrayList<>();
        ArrayList<String> ingredients = new ArrayList<>();

        Cursor cursor = getContentResolver().query(RecipeBookProviderContract.RECIPE_WITH_INGREDIENTS,
                null, RecipeBookProviderContract._ID + "=?", new String[]{""+id}, null);

        Log.d("g53mdp", "RecipeDetailsActivity queryRecipeWithIngredients c.getCount(): " + cursor.getCount());

		if (cursor.moveToFirst())
		{
			do
			{
				if (recordData.isEmpty()){
				    //FUTURE? cursor.getColumnIndex()
					recordData.add(cursor.getString(1)); // Title
					recordData.add(cursor.getString(2)); // Instructions
					recordData.add(cursor.getString(3)); // Rating
				}
				ingredients.add(cursor.getString(5));

			} while(cursor.moveToNext());
		}

        cursor.close();

        String ingredientsTextBuffer = "";

        for (String ingredient: ingredients){
            ingredientsTextBuffer += ingredient + "\n";
        }

        String title = recordData.get(0);
        String instructions = recordData.get(1);
        String rating = recordData.get(2);

        recipeTitleText.setText(title);
        ingredientsText.setText(ingredientsTextBuffer);
        instructionsText.setText(instructions);
        ratingTextView.setText(rating);
    }

    public void onChangeRatingButton(View v){
        updateRatingButton.setVisibility(View.GONE);
        saveRatingButton.setVisibility(View.VISIBLE);

        ratingTextView.setVisibility(View.GONE);
        ratingEditText.setVisibility(View.VISIBLE);
    }

    public void onSaveChangeRatingButton(View v){
        String newRatingText = ratingEditText.getText().toString();

        if (newRatingText.isEmpty()){
            return;
        }

        int newRating = Integer.parseInt(newRatingText);

        ContentValues contentValues = new ContentValues();
        contentValues.put("rating", newRating);

        getContentResolver().update(RecipeBookProviderContract.RECIPE_URI, contentValues, RecipeBookProviderContract._ID + "=?", new String[]{""+id});

        ratingTextView.setText("" + newRating);
        ratingTextView.setVisibility(View.VISIBLE);
        ratingEditText.setVisibility(View.GONE);


        updateRatingButton.setVisibility(View.VISIBLE);
        saveRatingButton.setVisibility(View.GONE);
    }

    public void onBackButton(View v){

        onSaveChangeRatingButton(v);
        setResult(0);
        finish();
    }

    public void onDeleteButton(View v){

        dbHelper.deleteRecipe(id);

        setResult(1);
        finish();
    }
}
