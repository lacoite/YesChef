package com.example.yeschef;

import static com.example.yeschef.DatabaseHelper.ID_COLUMN;
import static com.example.yeschef.DatabaseHelper.IMAGE_COLUMN;
import static com.example.yeschef.DatabaseHelper.NAME_COLUMN;
import static com.example.yeschef.DatabaseHelper.SAVED_TABLE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.yeschef.recipeRVinterface.LoadMore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import android.view.View;



public class RecipesActivity extends AppCompatActivity {
    JSONArray jsonResponse;
    JSONObject mealItem;
    List<RecipeRecyclerModel> recipes = new ArrayList<>();
    RecipeRecyclerAdapter recipeRecyclerAdapter;

    String responseText;
    List<String> mealList = new ArrayList<String>();
    ;
    StringBuffer response;
    String imageURL;
    URL url;

    public static Context cont;
    private ProgressDialog progressDialog;
    String apiKey1 = "fbcc9ddb89cf411091505be140f08e17";
    String apiKey = "e1229b49c5fd4ad8ab87f5efdd15d777";

    public DatabaseHelper db = new DatabaseHelper(this);
    public String selectedIngredients = "";

    //Gets selected ingredients from database

        public void getIngredients() {

        List<IngredientRecyclerModel> selected_ingredients = new ArrayList<>();
        selected_ingredients.addAll(db.getSelectedIngredients());

        if (selected_ingredients.size() == 0){
            //selectedIngredients += "Please select ingredients";
        }
        else if (selected_ingredients.size() == 1)
            selectedIngredients += selected_ingredients.get(0).getIngredient();
        else if (selected_ingredients.size() > 1) {
            selectedIngredients += selected_ingredients.get(0).getIngredient();
            for (int i = 1; i < selected_ingredients.size(); i++) {
                selectedIngredients += ",+" + selected_ingredients.get(i).getIngredient();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        // NOTE: Not currently set to activity_recipe.xml, needs to be updated
        //
        setContentView(R.layout.activity_meals);
        cont = getApplicationContext();

        //Gets selectedIngredients from the database
        getIngredients();

        String serverURL = "https://api.spoonacular.com/recipes/findByIngredients?apiKey=" + apiKey + "&ingredients=";
        new GetServerData().execute(serverURL);

    }

    public static Bitmap getBitmapFromURL(String url) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.i("BITMAP RETURNED: ", "BITMAP RETURNED!!!");
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void setArrayToList(Object[] array) {
        String[] stringArray = new String[array.length];
        for (int i = 0; i < stringArray.length; i++) {
            stringArray[i] = array[i].toString();
        }

        RecyclerView recipeRecyclerView = findViewById(R.id.recipeRecyclerView);
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recipeRecyclerAdapter = new RecipeRecyclerAdapter(recipeRecyclerView, this, recipes);
        recipeRecyclerView.setAdapter(recipeRecyclerAdapter);

        recipeRecyclerAdapter.setLoadMore(new LoadMore() {
            @Override
            public void onLoadMore() {
                if (recipes.size() <= 6) {
                    recipes.add(null);
                    recipeRecyclerAdapter.notifyItemInserted(recipes.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadMoreRecipes();
                        }
                    }, 4000);
                } else {
                    Toast.makeText(RecipesActivity.this, "No More Recipes!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Boolean checkSaved(String id){
        //check use db to check if the id is in the table, if it does, return true, else return false
        db = new DatabaseHelper(cont);
        Boolean key = db.isInSaved(id);
        return key;
    }

    public void addToSaved(String id, String name, String image){
        db = new DatabaseHelper(cont);
        db.addSaved(id, name, image);
    }

    public void removeFromSaved(String id){
        db = new DatabaseHelper(cont);
        db.removeSaved(id);
    }
    public static Context getContext(){
        return cont;
    }

    class GetServerData extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(RecipesActivity.this);
            progressDialog.setMessage("Starting Up The Oven");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return getWebServiceResponseData((String) objects[0]);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (mealList != null) {
                setArrayToList(mealList.toArray());
            }

        }
    }

    protected Void loadMoreRecipes() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        try {
            recipes.remove(recipes.size() - 1);
            recipeRecyclerAdapter.notifyItemRemoved(recipes.size());

            int index = recipes.size();
            int end = index + 6;
            for (int i = index; i < end; i++) {
                String id = "";
                String mealName = "";
                String image = "";
                mealItem = jsonResponse.getJSONObject(i);
                Integer countCheck = Integer.valueOf(mealItem.getString("missedIngredientCount"));
                if (countCheck > 0) {
                    id = mealItem.getString("id");
                    mealName = mealItem.getString("title");
                    image = mealItem.getString("image");

                    Bitmap imageBit = getBitmapFromURL(image);
                    recipes.add(new RecipeRecyclerModel(id, mealName, imageBit, image));
                }
            }
            recipeRecyclerAdapter.setLoaded();
            recipeRecyclerAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Void getWebServiceResponseData(String path) {

        try {
            url = new URL("https://api.spoonacular.com/recipes/findByIngredients?apiKey=" + apiKey + "&ingredients=" + selectedIngredients+ "&ranking=2&ignorePantry=true");
            path = "https://api.spoonacular.com/recipes/findByIngredients?apiKey=" + apiKey + "&ingredients=" + selectedIngredients + "&ranking=2&ignorePantry=true";
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();

            Log.d("WebService", "Response code: " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                // Reading response from input Stream
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String output;
                response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        responseText = response.toString();

        try {

            jsonResponse = new JSONArray(responseText);

            //changed to 6 to load 6 items first, changes countCheck > 0 to test more recipes
            for (int i = 0;i< 6;i++ )
            {
                String id = "";
                String mealName = "";
                String imageURL = "";
                mealItem = jsonResponse.getJSONObject(i);
                Integer countCheck = Integer.valueOf(mealItem.getString("missedIngredientCount"));
                if( countCheck > 0){
                     id = mealItem.getString("id");
                     mealName = mealItem.getString("title");
                     imageURL = mealItem.getString("image");
                     Log.i("ID: ", id);
                     Log.i("mealInfo: ", mealName);
                     Log.i("IMAGE LINK: ", imageURL);
                     recipes.add(new RecipeRecyclerModel(id, mealName, getBitmapFromURL(imageURL), imageURL));
                }
            }
        } catch (JSONException e) {
            Log.i("EXCEPTION HERE :", "AAAAAAA");
            e.printStackTrace();
        }

        return null;
    }


    //
    //Bottom Navigation Buttons
    //
    public void onClickFilters(View view){
        Intent intent = new Intent(RecipesActivity.this, FiltersActivity.class);
        startActivity(intent);
    }

    public void onClickPantry(View view){
        Intent intent = new Intent(RecipesActivity.this, PantryActivity.class);
        startActivity(intent);
    }

    public void onClickCookBook(View view){
        Intent intent = new Intent(RecipesActivity.this, CookbookActivity.class);
        startActivity(intent);
    }

    public void onClickMealPlan(View view){
        Intent intent = new Intent(RecipesActivity.this, MealPlanActivity.class);
        startActivity(intent);
    }

    //
    //Give information on recipe
    //
    public void onClickRecipeImage(View view){
        CharSequence text = "Clicking on this image will take the user to a preview of the online recipe! The user can then navigate to the original recipe online.";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    public void onClickRecipeFavorite(View view){
        CharSequence text = "Clicking on this icon will add the respective recipe to the user's cookbook!";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();

    }
}

