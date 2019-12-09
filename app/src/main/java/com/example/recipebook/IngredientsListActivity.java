package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class IngredientsListActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Cursor c;
        SimpleCursorAdapter simpleCursorAdapter;

        ListView ingredientsListView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_list);
        ingredientsListView = findViewById(R.id.ingredientsListView);

        String[] columns = new String[]{
                RecipeBookProviderContract.INGREDIENT_NAME,
                RecipeBookProviderContract._ID
        };

        int[] to = new int[]{
                R.id.rowIngredientText
        };

        c = getContentResolver().query(RecipeBookProviderContract.INGREDIENT_URI, columns, null, null, null);
        Log.d("g53mdp", "IngredientsListActivity cursor: " + c.toString());

        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.row_ingredient, c, columns, to, 0);
        ingredientsListView.setAdapter(simpleCursorAdapter);

    }

    public void onBackButton(View v){
        finish();
    }
}
