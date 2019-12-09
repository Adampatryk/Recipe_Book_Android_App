package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

    SimpleCursorAdapter simpleCursorAdapter;

    Handler h = new Handler();

    ListView recipeListView;
    TextView ratingColumnHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        setUpViews();

        getContentResolver().
                registerContentObserver(
                        RecipeBookProviderContract.ALL_URI,
                        true,
                        new ChangeObserver(h));

        queryRecipes();
    }

        class ChangeObserver extends ContentObserver {

        public ChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            queryRecipes();
        }
    }

    public void setUpViews(){

        recipeListView = findViewById(R.id.recipeListView);
        ratingColumnHeading = findViewById(R.id.ratingColumnHeading);

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
    }

    public void queryRecipes(){
        String[] columns = new String[] {
                RecipeBookProviderContract._ID,
                RecipeBookProviderContract.TITLE,
                RecipeBookProviderContract.RATING
        };

        int[] colResIds = new int[] {
                R.id._id,
                R.id.rowRecipeTitleText,
                R.id.rowRecipeRatingText
        };

        Cursor cursor = getContentResolver().query(RecipeBookProviderContract.RECIPE_URI, columns, null, null, null);

        simpleCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.row_recipe,
                cursor,
                columns,
                colResIds,
                0);

        recipeListView.setAdapter(simpleCursorAdapter);
    }

//    public void updateRatingColumnLabel(){
//        String text = getString(R.string.label_rating);
//        int sortByRatingState = dbHelper.getSortByRatingState();
//        if(sortByRatingState == 0){
//            text = "- " + text;
//        }
//        else if (sortByRatingState == 1){
//            text = "ASC " + text;
//        } else {
//            text = "DESC " + text;
//        }
//        ratingColumnHeading.setText(text);
//    }



    public void onAddRecipeButton(View v){
        Intent goToAddRecipeIntent = new Intent(this, AddRecipeActivity.class);
        startActivityForResult(goToAddRecipeIntent, REQUEST_CODE_ADD_RECIPE_ACTIVITY);
    }
    public void onIngredientsButton(View v){
        Intent goToIngredientsIntent = new Intent(this, IngredientsListActivity.class);
        startActivityForResult(goToIngredientsIntent, REQUEST_CODE_INGREDIENTS_ACTIVITY);
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        simpleCursorAdapter.swapCursor(dbHelper.getRecipeCursor(false));
//    }

}
