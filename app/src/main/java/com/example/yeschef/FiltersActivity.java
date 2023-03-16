package com.example.yeschef;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

//***************************
// INSERT CODE TO ACTUALLY IMPLEMENT USING THE FILTERS IN THE UPDATE FUNCTIONS

public class FiltersActivity extends AppCompatActivity {
    RadioButton vegetarianButton;
    RadioButton veganButton;
    RadioButton pescButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        vegetarianButton = findViewById(R.id.vegetarianButton);
        vegetarianButton.setChecked(false);
        veganButton = findViewById(R.id.veganButton);
        veganButton.setChecked(false);
        pescButton = findViewById(R.id.pescetarianButton);
        pescButton.setChecked(false);
    }

    public void onClickVegetarian(View view){
        if(vegetarianButton.isChecked() == true){
            Log.i("Clicked: ", String.valueOf(vegetarianButton.isChecked()));
            vegetarianButton.setBackgroundResource(R.drawable.filter_selected);
            vegetarianButton.setChecked(true);
        }
        else{
            Log.i("Clicked: ", String.valueOf(vegetarianButton.isChecked()));
            vegetarianButton.setBackgroundResource(R.drawable.filter_default);
            vegetarianButton.setChecked(false);
        }
        Log.i("Clicked: ", String.valueOf(vegetarianButton.isChecked()));
        updateVegan(veganButton);
        updatePescetarian(pescButton);

        CharSequence text = "Implement Vegetarian Category";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();


    }

    public void onClickVegan(View view){
        if(veganButton.isChecked() == true){
            Log.i("Clicked: ", String.valueOf(veganButton.isChecked()));
            veganButton.setBackgroundResource(R.drawable.filter_selected);
            veganButton.setChecked(true);
        }
        else{
            Log.i("Clicked: ", String.valueOf(veganButton.isChecked()));
            veganButton.setBackgroundResource(R.drawable.filter_default);
            veganButton.setChecked(false);
        }
        Log.i("Clicked: ", String.valueOf(veganButton.isChecked()));
        updateVegetarian(vegetarianButton);
        updatePescetarian(pescButton);

        CharSequence text = "Implement Vegan Category";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    public void onClickPescetarian(View view){
        if(pescButton.isChecked() == true){
            Log.i("Clicked: ", String.valueOf(pescButton.isChecked()));
            pescButton.setBackgroundResource(R.drawable.filter_selected);
            pescButton.setChecked(true);
        }
        else{
            Log.i("Clicked: ", String.valueOf(pescButton.isChecked()));
            pescButton.setBackgroundResource(R.drawable.filter_default);
            pescButton.setChecked(false);
        }
        Log.i("Clicked: ", String.valueOf(pescButton.isChecked()));
        updateVegetarian(vegetarianButton);
        updateVegan(veganButton);

        CharSequence text = "Implement Pescetarian Category";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    public void updateVegetarian(View view){
        if(vegetarianButton.isChecked() == true){
            Log.i("Clicked: ", String.valueOf(vegetarianButton.isChecked()));
            vegetarianButton.setBackgroundResource(R.drawable.filter_selected);
            vegetarianButton.setChecked(true);
        }
        else{
            Log.i("Clicked: ", String.valueOf(vegetarianButton.isChecked()));
            vegetarianButton.setBackgroundResource(R.drawable.filter_default);
            vegetarianButton.setChecked(false);
        }
    }

    public void updateVegan(View view) {
        if (veganButton.isChecked() == true) {
            Log.i("Clicked: ", String.valueOf(veganButton.isChecked()));
            veganButton.setBackgroundResource(R.drawable.filter_selected);
            veganButton.setChecked(true);
        } else {
            Log.i("Clicked: ", String.valueOf(veganButton.isChecked()));
            veganButton.setBackgroundResource(R.drawable.filter_default);
            veganButton.setChecked(false);
        }
    }

    public void updatePescetarian(View view){
        if(pescButton.isChecked() == true){
            Log.i("Clicked: ", String.valueOf(pescButton.isChecked()));
            pescButton.setBackgroundResource(R.drawable.filter_selected);
            pescButton.setChecked(true);
        }
        else{
            Log.i("Clicked: ", String.valueOf(pescButton.isChecked()));
            pescButton.setBackgroundResource(R.drawable.filter_default);
            pescButton.setChecked(false);
        }
    }
}