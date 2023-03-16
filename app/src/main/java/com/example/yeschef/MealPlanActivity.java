package com.example.yeschef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MealPlanActivity extends AppCompatActivity {

    private RecyclerView recyclerView1;
    private MealPlanRecyclerAdapter recyclerAdapter1;

    ArrayList<MealPlanRecyclerModel> items;

    MealPlanRecyclerModel defaultMeal;


    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        db = new DatabaseHelper(this);

    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();

    }

    public void updateUI(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        db.getWritableDatabase();
        
        ArrayList<MealPlanRecyclerModel> temp = db.getMealPlan();


        ArrayList<MealPlanRecyclerModel> items = new ArrayList<>();


        for(int i = 0; i < temp.size(); i++)
        {

            if(temp.get(i).getName().contains("NORECIPE")) {

                Bitmap defaultBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.app_icon);
                defaultMeal = new MealPlanRecyclerModel("", "Edit To Add Meal", defaultBitmap, "" + "");
                items.add(defaultMeal);
            }
            else{
                items.add(temp.get(i));
            }
        }
        


        recyclerView1 = findViewById(R.id.recyclerViewTop);
        recyclerAdapter1 = new MealPlanRecyclerAdapter(items);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(recyclerAdapter1);
    }

    public void onClickMealPlanEdit(View view) {
        Intent intent = new Intent(getApplicationContext(), MealPlanEditDummyActivity.class);
        startActivity(intent);
    }

    public void onClickRecipes(View view){
        Intent intent = new Intent(MealPlanActivity.this, RecipesActivity.class);
        startActivity(intent);
    }

    public void onClickPantry(View view){
        Intent intent = new Intent(MealPlanActivity.this, PantryActivity.class);
        startActivity(intent);
    }

    public void onClickCookBook(View view){
        Intent intent = new Intent(MealPlanActivity.this, CookbookActivity.class);
        startActivity(intent);
    }
}