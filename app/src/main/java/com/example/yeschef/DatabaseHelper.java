package com.example.yeschef;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    SQLiteDatabase db;
    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Path and User

    public static String DB_PATH = "/data/data/com.example.yeschef/databases/";
    public static final String TB_USER = "Users";

    //Database name
    private static final String DATABASE_NAME = "YesChef_db_original.db";

    //Table names
    public static final String INGREDIENTS_TABLE_NAME = "Ingredients";
    public static final String RECIPE_TABLE_NAME = "Recipes";
    public static final String SAVED_TABLE_NAME = "Saved";
    public static final String MEAL_TABLE_NAME = "MealPlan";

    //Ingredient Table Columns
    public static final String INGREDIENT_COLUMN = "Ingredient";
    public static final String RECIPE_COLUMN = "Recipe";
    public static final String SELECTED_COLUMN = "Selected";
    public static final String TYPE_COLUMN = "Type";

    //Saved Table Columns
    public static final String ID_COLUMN = "Id";
    public static final String NAME_COLUMN = "Name";
    public static final String IMAGE_COLUMN = "Image";
    public static final String LINK_COLUMN = "Link";

    //Meals Table Columns
    public static final String NUMBER_COLUMN = "Number";
    public static final String MEAL_NAME_COLUMN = "Name";
    public static final String MEAL_ID_COLUMN = "Id";
    public static final String MEAL_IMAGE_COLUMN = "Image";

    //closes connections
    @Override
    public synchronized void close() {
        if(db != null)
            db.close();
        super.close();
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Open
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DATABASE_NAME;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    //Check v2
    public boolean checkDataBase(){
        File databaseFile = new File(DB_PATH + DATABASE_NAME);
        return databaseFile.exists();
    }

    //Copy
    public void copyDataBase() throws IOException {
        try {
            InputStream myInput = context.getAssets().open(DATABASE_NAME);
            String outputFileName = DB_PATH + DATABASE_NAME;
            OutputStream myOutput = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;

            while((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            Log.e("tle99 - copyDatabase", e.getMessage());
        }

    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {

        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("tle99 - create", e.getMessage());
            }
        }
    }

    // Creating tables
    @Override
    public void onCreate(SQLiteDatabase db){
        //no code needed for Indgredients db, may need for other tables
    }

    //Updating Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // No code needed for Indgredients db, may need for other tables

    }

    //Update Checkbox
    public void updateCheckbox(String ingredient, String checkedState) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SELECTED_COLUMN, checkedState);

        db.update(INGREDIENTS_TABLE_NAME, values, "Ingredient = \"" + ingredient + "\"", null);


        // close db connection
        db.close();
   }

    //Check if recipe exists in saved
    public boolean isInSaved(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor mCount= db.rawQuery("SELECT COUNT(1) FROM " + SAVED_TABLE_NAME + " WHERE " + ID_COLUMN + " = \""  + id +"\"", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        // close db connection
        db.close();

        if(count == 0){
            return false;
        }
        return true;
    }

    //Adding recipe to saved UPDATE WITH LINK
    public void addSaved(String id, String name, String image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, id);
        values.put(NAME_COLUMN, name);
        values.put(IMAGE_COLUMN, image);

        try {
            //add to saved
            db.insert(SAVED_TABLE_NAME, null, values);
        }
        catch (Exception e){
            db.close();
        }

        // close db connection
        db.close();
        
    }
    
    //Removes a recipe from the saved table
    public void removeSaved(String id){
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(SAVED_TABLE_NAME, "id = " + id, null);

        // close db connection
        db.close();
    }

    public void addMealsToMealPlan(int num, String name, String id, String image)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MEAL_NAME_COLUMN, name);
        values.put(MEAL_ID_COLUMN, id); //FIX THIS
        values.put(MEAL_IMAGE_COLUMN, image); //AND THIS
        db.update(MEAL_TABLE_NAME, values, "Number = " + num, null);
    }

    public ArrayList<MealPlanRecyclerModel> getMealPlan() {
        ArrayList<MealPlanRecyclerModel> mealsInPlan = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + MEAL_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        String name = null;
        if(cursor.moveToFirst()){
            do{
                String idSelected = cursor.getString(cursor.getColumnIndexOrThrow(MEAL_ID_COLUMN));
                String nameSelected = cursor.getString(cursor.getColumnIndexOrThrow(MEAL_NAME_COLUMN));
                String imageSelected = cursor.getString(cursor.getColumnIndexOrThrow(MEAL_IMAGE_COLUMN));

                Bitmap myBitmap = null;
                try {
                    URL urlConnection = new URL(imageSelected);
                    HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mealsInPlan.add(new MealPlanRecyclerModel(idSelected, nameSelected, myBitmap, imageSelected));
            }while(cursor.moveToNext());
      }

        return mealsInPlan;
    }

    //Clearing Saved Table
    public void clearSaved() {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        //if Saved_Table exists, drop it and recreate empty, OR delete all records from table

        // close db connection
        db.close();
    }

    //Resetting Selected Values
    public void resetSelected(String category) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SELECTED_COLUMN, "false");

        try {
            if (category == "all") {
                db.update(INGREDIENTS_TABLE_NAME, values, "Selected = \"true\"", null);

            } else {
                db.update(INGREDIENTS_TABLE_NAME, values, "Type = \"" + category + "\"", null);
            }
        }
        catch (Exception e){
            db.close();
        }

        // close db connection
        db.close();
    }

    //Ingredient by Type
    public List<IngredientRecyclerModel> getIngredientsByType(String ingredientType) {
        List<IngredientRecyclerModel> ingredients_of_type = new ArrayList<>();

        //Select All Where Selected Is True
        String selectQuery = "SELECT * FROM " + INGREDIENTS_TABLE_NAME + " WHERE " + TYPE_COLUMN + " = \"" + ingredientType + "\" ORDER BY " + INGREDIENT_COLUMN;
        //SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all selected rows and adding to list
        if(cursor.moveToFirst()){
            do{
                String ingredientSelected = cursor.getString(cursor.getColumnIndexOrThrow(INGREDIENT_COLUMN));
                String selectedState = cursor.getString(cursor.getColumnIndexOrThrow(SELECTED_COLUMN));
                String typeState = cursor.getString(cursor.getColumnIndexOrThrow(TYPE_COLUMN));
                ingredients_of_type.add(new IngredientRecyclerModel(ingredientSelected, selectedState, typeState));

            }while(cursor.moveToNext());
        }

        //Close db connection
        db.close();

        return ingredients_of_type;
    }

    public List<RecipeRecyclerModel> getSavedRecipes() {
        List<RecipeRecyclerModel> saved_recipes = new ArrayList<>();

        //Select All Where Selected Is True
        String selectQuery = "SELECT * FROM " + SAVED_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);



        //Looping through all selected rows and adding to list
        if(cursor.moveToFirst()){
            do{
                String idSelected = cursor.getString(cursor.getColumnIndexOrThrow(ID_COLUMN));
                String nameSelected = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
                String imageSelected = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_COLUMN));

                Bitmap myBitmap = null;
                try {
                    URL urlConnection = new URL(imageSelected);
                    HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                    //return myBitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                saved_recipes.add(new RecipeRecyclerModel(idSelected, nameSelected, myBitmap, imageSelected));

            }while(cursor.moveToNext());
        }

        //Close db connection
        db.close();

        return saved_recipes;
    }

    //Get list of selected ingredients, Peyton, you'll need to save it to a string/array for the API call! -Latasha
    public List<IngredientRecyclerModel> getSelectedIngredients() {
        List<IngredientRecyclerModel> selected_ingredients = new ArrayList<>();
        
      
            
        //Select All Where Selected Is True
        String selectQuery = "SELECT * FROM " + INGREDIENTS_TABLE_NAME + " WHERE " + SELECTED_COLUMN + " = \"true\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all selected rows and adding to list
        Boolean b = cursor.moveToFirst();

        if(cursor.moveToFirst()){
            do {
                String ingredientSelected = cursor.getString(cursor.getColumnIndexOrThrow(INGREDIENT_COLUMN));
                String selectedState = cursor.getString(cursor.getColumnIndexOrThrow(SELECTED_COLUMN));
                String typeState = cursor.getString(cursor.getColumnIndexOrThrow(TYPE_COLUMN));

                //HERE changes ingredient into better searchable in API
                String _ingredient = "";
                _ingredient = ingredientSelected;

                if (ingredientSelected.indexOf(" ") != -1) {
                    ingredientSelected = _ingredient.replace(' ', '_');
                }

                selected_ingredients.add(new IngredientRecyclerModel(ingredientSelected, selectedState, typeState));


            }while(cursor.moveToNext());
        }

        //Close db connection
        db.close();

        return selected_ingredients;
    }

    //Recipe thingy
    public List<CookbookRecipeRecyclerModel> getRecipesByType(String recipeType) {
        List<CookbookRecipeRecyclerModel> recipe_of_type = new ArrayList<>();

        //Select All Where Selected Is True
        String selectQuery = "SELECT * FROM " + RECIPE_TABLE_NAME; //+ " WHERE " + TYPE_COLUMN + " = \"" + ingredientType + "\"";
        //SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all selected rows and adding to list
        if(cursor.moveToFirst()){
            do{
                int recipeSelected = cursor.getInt(cursor.getColumnIndexOrThrow(RECIPE_COLUMN));
                String selectedState = cursor.getString(cursor.getColumnIndexOrThrow(SELECTED_COLUMN));
                recipe_of_type.add(new CookbookRecipeRecyclerModel(recipeSelected, selectedState));

            }while(cursor.moveToNext());
        }

        //Close db connection
        db.close();

        return recipe_of_type;
    }


}
