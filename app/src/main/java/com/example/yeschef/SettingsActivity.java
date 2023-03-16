package com.example.yeschef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    PantryActivity classObj;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = new DatabaseHelper(this);
    }

    public void onClickDarkMode(View view){
        int mode = AppCompatDelegate.getDefaultNightMode();
        if(mode == -100){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void onClickClearPantry(View view){
        classObj = new PantryActivity();
        db.resetSelected("all");
        classObj.loadIngredients("Vegetable");
        finish();
    }

    public void onClickClearVegetables(View view){
        classObj = new PantryActivity();
        db.resetSelected("Vegetable");
        classObj.loadIngredients("Vegetable");
        finish();
    }

    public void onClickClearFruits(View view){
        classObj = new PantryActivity();
        db.resetSelected("Fruit");
        classObj.loadIngredients("Fruit");
        finish();
    }

    public void onClickClearMeats(View view){
        classObj = new PantryActivity();
        db.resetSelected("Meat");
        classObj.loadIngredients("Meat");
        finish();
    }

    public void onClickClearDairyEggs(View view){
        classObj = new PantryActivity();
        db.resetSelected("DE");
        classObj.loadIngredients("DE");
        finish();
    }

    public void onClickClearHSO(View view){
        classObj = new PantryActivity();
        db.resetSelected("HSO");
        classObj.loadIngredients("HSO");
        finish();
    }

    public void onClickClearGrains(View view){
        classObj = new PantryActivity();
        db.resetSelected("Grain");
        classObj.loadIngredients("Grain");
        finish();
    }

}