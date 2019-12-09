package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        dbHelper = new DBHelper(this);

        ArrayList<ArrayList<String>> recipe = dbHelper.getRecipe(id);

        ArrayList<String> data = recipe.get(0);
        ArrayList<String> ingredients = recipe.get(1);

        String ingredientsTextBuffer = "";

        for (String ingredient: ingredients){
            ingredientsTextBuffer += ingredient + "\n";
        }

        String title = data.get(0);
        String instructions = data.get(1);
        String rating = data.get(2);

        recipeTitleText.setText(title);
        ingredientsText.setText(ingredientsTextBuffer);
        instructionsText.setText(instructions);
        ratingTextView.setText(rating);

        Log.d("g53mdp", "RecipeDetailsActivity onCreate Recipe: " + recipe.toString());
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

        dbHelper.updateRating(id, newRating);

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
