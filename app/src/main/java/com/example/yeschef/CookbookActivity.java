package com.example.yeschef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CookbookActivity extends AppCompatActivity {

    private RecyclerView CookbookRecipeRecyclerView;
    private CookbookRecipeRecyclerAdapter CookbookRecipeRecyclerAdapter;
    Layout recipeItem;
    ImageButton star;

    public static Context cont;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookbook_v2);
        cont = getApplicationContext();


        loadSavedRecipes();


    }

    public static Context getContext() {
        return cont;
    }

    public Boolean checkSaved(String id){
        //check use db to check if the id is in the table, if it does, return true, else return false
        db = new DatabaseHelper(cont);
        db = new DatabaseHelper(cont);
        Boolean key = db.isInSaved(id);
        return key;
    }

    public void removeSaved(String id){//, String link){
        db = new DatabaseHelper(cont);
        db.removeSaved(id);//, link);
    }
    public void removeFromSaved(String id){
        db = new DatabaseHelper(cont);
        db.removeSaved(id);
    }
    public void addToSaved(String id, String name, String image){
        db = new DatabaseHelper(cont);
        db.addSaved(id, name, image);
    }

    public void updateRecipes(String ing, String check){

        db = new DatabaseHelper(cont);

    }

    public void loadSavedRecipes(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

         DatabaseHelper db = new DatabaseHelper(this);



        ArrayList<RecipeRecyclerModel> savedList = new ArrayList<>();
        //getRecipesByType isn't the one you want to be using right? no idea
        //Your trying to get the savedRecipes, so you're going to want to call getSavedRecipes or whatever it is
        savedList.addAll(db.getSavedRecipes());


        CookbookRecipeRecyclerView = findViewById(R.id.recipeIconRecycler);
        CookbookRecipeRecyclerAdapter =  new CookbookRecipeRecyclerAdapter(CookbookRecipeRecyclerView, this, savedList);
        CookbookRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        CookbookRecipeRecyclerView.setAdapter(CookbookRecipeRecyclerAdapter);

        db.getSavedRecipes();
    }



    public void onClickRecipeImage(View view){
        CharSequence text = "Clicking on this image will take the user to a preview of the online recipe! The user can then navigate to the original recipe online.";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }
    public void onClickRecipeFavorite(View view){
        CharSequence text = "Clicking on this icon will remove the respective recipe from the user's cookbook!";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    public void onClickRecipes(View view){
        Intent intent = new Intent(CookbookActivity.this, RecipesActivity.class);
        startActivity(intent);
    }

    public void onClickPantry(View view){
        Intent intent = new Intent(CookbookActivity.this, PantryActivity.class);
        startActivity(intent);
    }

    public void onClickMealPlan(View view){
        Intent intent = new Intent(CookbookActivity.this, MealPlanActivity.class);
        startActivity(intent);
    }


}