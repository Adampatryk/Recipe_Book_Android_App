package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class RecipeListActivity extends AppCompatActivity {

    int REQUEST_CODE_ADD_RECIPE_ACTIVITY = 1;
    int REQUEST_CODE_INGREDIENTS_ACTIVITY = 2;
    int REQUEST_CODE_RECIPE_DETAILS_ACTIVITY = 3;

    DBHelper dbHelper;
    Cursor c;
    SimpleCursorAdapter simpleCursorAdapter;

    ListView recipeListView;
    TextView ratingColumnHeading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        dbHelper = new DBHelper(this);
        recipeListView = findViewById(R.id.recipeListView);
        ratingColumnHeading = findViewById(R.id.ratingColumnHeading);

        String[] columns = new String[] {
                "_id",
                "title",
                "rating"
        };
        int[] to = new int[] {
                R.id._id,
                R.id.rowRecipeTitleText,
                R.id.rowRecipeRatingText,
        };

        Log.d("g53mdp", "RecipeListActivity onCreate dbHelper: " + dbHelper.toString());
        c = dbHelper.getRecipeCursor(false);
        Log.d("g53mdp", "RecipeListActivity cursor: " + c.toString());

        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.row_recipe, c, columns, to, 0);
        recipeListView.setAdapter(simpleCursorAdapter);

        updateRatingColumnLabel();

        final Intent goToRecipeDetailsActivity = new Intent(this, RecipeDetailsActivity.class);
        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = Integer.parseInt(((TextView) view.findViewById(R.id._id)).getText().toString());
                Log.d("g53mdp","RecipeListActivity onItemClick ID: " + id);

                goToRecipeDetailsActivity.putExtra("id", ""+id);
                startActivityForResult(goToRecipeDetailsActivity, REQUEST_CODE_RECIPE_DETAILS_ACTIVITY);
            }
        });

        ratingColumnHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleCursorAdapter.swapCursor(dbHelper.getRecipeCursor(true));
                updateRatingColumnLabel();
            }
        });

    }

    public void updateRatingColumnLabel(){
        String text = getString(R.string.label_rating);
        int sortByRatingState = dbHelper.getSortByRatingState();
        if(sortByRatingState == 0){
            text = "- " + text;
        }
        else if (sortByRatingState == 1){
            text = "ASC " + text;
        } else {
            text = "DESC " + text;
        }
        ratingColumnHeading.setText(text);
    }

    public void onAddRecipeButton(View v){
        Intent goToAddRecipeIntent = new Intent(this, AddRecipeActivity.class);
        startActivityForResult(goToAddRecipeIntent, REQUEST_CODE_ADD_RECIPE_ACTIVITY);
    }
    public void onIngredientsButton(View v){
        Intent goToIngredientsIntent = new Intent(this, IngredientsListActivity.class);
        startActivityForResult(goToIngredientsIntent, REQUEST_CODE_INGREDIENTS_ACTIVITY);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        simpleCursorAdapter.swapCursor(dbHelper.getRecipeCursor(false));
    }

}
