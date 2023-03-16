package com.example.yeschef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MealPlanEditDummyActivity extends AppCompatActivity {

    private RecyclerView DummyRecyclerView;
    private DummyRecyclerAdapter DummyRecyclerAdapter;
    Layout recipeItem;
    ImageButton star;
    ArrayList<RecipeRecyclerModel> dbList = new ArrayList<>();
    String names[] = new String[7];
    Intent receiving;

    public static Context cont;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_edit_dummy);
        cont = getApplicationContext();

        db = new DatabaseHelper(this);
        try{
            db.createDataBase();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        dbList = (ArrayList<RecipeRecyclerModel>) db.getSavedRecipes();

        ArrayList<DummyRecyclerModel> item = new ArrayList<>();
        for(int i = 0; i < dbList.size(); i++)
        {
            Bitmap imageBit = getBitmapFromURL(dbList.get(i).getLinkToImage());
            item.add(new DummyRecyclerModel(dbList.get(i).getId(), dbList.get(i).getName() , imageBit, dbList.get(i).getLinkToImage()));
        }


        DummyRecyclerView = findViewById(R.id.recipeIconRecycler);
        DummyRecyclerAdapter =  new DummyRecyclerAdapter(item);
        DummyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        DummyRecyclerView.setAdapter(DummyRecyclerAdapter);

    }

    public static Bitmap getBitmapFromURL(String url) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Context getContext() {
        return cont;
    }

    public void onRadioButtonClicked(View v){
        boolean checked = ((RadioButton) v).isChecked();
        int num = -1;
        switch(v.getId()){
            case R.id.dayOne:if (checked){num = 1;}
                break;
            case R.id.dayTwo:if (checked){num = 2;}
                break;
            case R.id.dayThree:if(checked){num = 3;}
                break;
            case R.id.dayFour:if(checked){num = 4;}
                break;
            case R.id.dayFive:if(checked){num = 5;}
                break;
            case R.id.daySix:if(checked){num = 6;}
                break;
            case R.id.daySeven:if(checked){num = 7;}
                break;
        }
        receiving = getIntent();
        String name = receiving.getStringExtra("name");
        String id = receiving.getStringExtra("id");
        String link = receiving.getStringExtra("image");
        Intent intent = new Intent(getApplicationContext(), MealPlanActivity.class);

        db.addMealsToMealPlan(num, name, id, link);
        finish();
        startActivity(intent);
    }

}