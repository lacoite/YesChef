package com.example.yeschef;

import static java.lang.Boolean.parseBoolean;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.IngredientRecyclerViewHolder>{
    PantryActivity classObj;
    DatabaseHelper db;
    private ArrayList<IngredientRecyclerModel> ingredients2;
    private ArrayList<IngredientRecyclerModel> items;
    int row_index = -1;


    public IngredientRecyclerAdapter(ArrayList<IngredientRecyclerModel> items) {
        this.ingredients2 = items;
    }

    @NonNull
    @Override
    public IngredientRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_recycler_item, parent, false);
        IngredientRecyclerViewHolder ingredientRecyclerViewHolder = new IngredientRecyclerViewHolder(view);
        return  ingredientRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientRecyclerViewHolder holder, int position) {
        //MainActivity classObj = new MainActivity();
        classObj = new PantryActivity();
        IngredientRecyclerModel currentItem = ingredients2.get(position);
        holder.textView.setText(currentItem.getIngredient());
        holder.checkBox.setChecked(parseBoolean(currentItem.getSelected()));

        holder.checkBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("FIRST SELECTED: ", currentItem.getSelected());
                Log.i("BOX SELECTED:" , String.valueOf(holder.checkBox.isChecked()));
                classObj.updateIngredients(currentItem.getIngredient(), String.valueOf(holder.checkBox.isChecked()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients2.size();
    }

    public static class IngredientRecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        LinearLayout linearLayout;
        CheckBox checkBox;

        public IngredientRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.ingredientText);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            checkBox = itemView.findViewById(R.id.ingredientCheckbox);
        }
    }

}
