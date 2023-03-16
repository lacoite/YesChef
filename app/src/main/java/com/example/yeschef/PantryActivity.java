package com.example.yeschef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PantryActivity extends AppCompatActivity implements UpdateIngredientRV{
    private RecyclerView iconRecyclerView;
    private IconRecyclerAdapter iconRecyclerAdapter;


    private RecyclerView ingredientRecyclerView;
    private IngredientRecyclerAdapter ingredientRecyclerAdapter;

    public static Context cont;
    DatabaseHelper db;
    UpdateIngredientRV updateIngredientRV;

    ArrayList<IngredientRecyclerModel> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //will used saved instance to save theme on relaunch me thinks
        //savedInstanceState.get("Theme");
        setContentView(R.layout.activity_pantry_v2);
        cont = getApplicationContext();


        ArrayList<IconRecyclerModel> category = new ArrayList<>();
        category.add(new IconRecyclerModel(R.drawable.vegetables_icon, "Vegetables"));
        category.add(new IconRecyclerModel(R.drawable.fruits_icon, "Fruits"));
        category.add(new IconRecyclerModel(R.drawable.meat_icon, "Meats"));
        category.add(new IconRecyclerModel(R.drawable.hso_icon, "Herbs, Seasonings, & Oils"));
        category.add(new IconRecyclerModel(R.drawable.em_icon, "Eggs & Milk"));
        category.add(new IconRecyclerModel(R.drawable.grain_icon, "Grains"));

        iconRecyclerView = findViewById(R.id.iconRecycler);
        iconRecyclerAdapter = new IconRecyclerAdapter(category, this, this);
        iconRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        iconRecyclerView.setAdapter(iconRecyclerAdapter);

        db = new DatabaseHelper(this);
        try{
            db.createDataBase();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        loadIngredients("Vegetable");
    }

    public void updateIngredients(String ing, String check){
        Log.i("ON MAIN: ", "yes" + ing + check);
        db = new DatabaseHelper(cont);
        db.updateCheckbox(ing, check);
        //db.updateCheckbox("Tomato", "True");
    }

    public ArrayList<IngredientRecyclerModel> refreshIngredients(String IngredientType){
        db = new DatabaseHelper(cont);

        ingredientList = new ArrayList<>();
        ingredientList.addAll(db.getIngredientsByType(IngredientType));
        return ingredientList;
    }

    public void loadIngredients(String IngredientType){
        db = new DatabaseHelper(cont);

        ingredientList = new ArrayList<>();
        ingredientList.addAll(db.getIngredientsByType(IngredientType));


        ingredientRecyclerView = findViewById(R.id.ingredientRecycler);
        ingredientRecyclerAdapter = new IngredientRecyclerAdapter(ingredientList);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(cont, LinearLayoutManager.VERTICAL, false));
        ingredientRecyclerView.setAdapter(ingredientRecyclerAdapter);
        db.getSelectedIngredients();
    }

    public static Context getContext(){
        return cont;
    }

    public void onClickRecipes(View view){
        Intent intent = new Intent(PantryActivity.this, RecipesActivity.class);
        startActivity(intent);
    }

    public void onClickCookBook(View view){
        Intent intent = new Intent(PantryActivity.this, CookbookActivity.class);
        startActivity(intent);
    }

    public void onClickMealPlan(View view){
        Intent intent = new Intent(PantryActivity.this, MealPlanActivity.class);
        startActivity(intent);
    }

    public void onClickSettings(View view){
        Intent intent = new Intent(PantryActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void callback(int position, ArrayList<IngredientRecyclerModel> updatedRecipes) {
        ingredientRecyclerAdapter = new IngredientRecyclerAdapter(updatedRecipes);
        ingredientRecyclerAdapter.notifyDataSetChanged();
        ingredientRecyclerView.setAdapter(ingredientRecyclerAdapter);
    }
}
