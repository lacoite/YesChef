package com.example.yeschef;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IconRecyclerAdapter extends RecyclerView.Adapter<IconRecyclerAdapter.IconRecyclerViewHolder>{
    UpdateIngredientRV updateIngredientRV;
    Activity activity;
    boolean check = true;
    boolean select = true;
    PantryActivity classObj;


    private ArrayList<IconRecyclerModel> items;
    int row_index = 0;

    public IconRecyclerAdapter(ArrayList<IconRecyclerModel> items, Activity activity, UpdateIngredientRV updateIngredientRV) {
        this.items = items;
        this.activity = activity;
        this.updateIngredientRV = updateIngredientRV;
    }

    @NonNull
    @Override
    public IconRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_recycler_item, parent, false);
        IconRecyclerViewHolder iconRecyclerViewHolder = new IconRecyclerViewHolder(view);
        return  iconRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IconRecyclerViewHolder holder, int position) {
        classObj = new PantryActivity();
        IconRecyclerModel currentItem = items.get(position);
        holder.imageView.setImageResource(currentItem.getImage());
        holder.textView.setText(currentItem.getText());

        if(check){
            ArrayList<IngredientRecyclerModel> ingredientsUpdate= new ArrayList<>();
            ingredientsUpdate = classObj.refreshIngredients("Vegetable");
            updateIngredientRV.callback(position,ingredientsUpdate);
            check = false;
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                row_index = position;
                if(position == 0){
                    ArrayList<IngredientRecyclerModel> ingredientsUpdate= new ArrayList<>();
                    ingredientsUpdate = classObj.refreshIngredients("Vegetable");
                    updateIngredientRV.callback(position,ingredientsUpdate);
                }
                else if (position == 1){
                    ArrayList<IngredientRecyclerModel> ingredientsUpdate= new ArrayList<>();
                    ingredientsUpdate = classObj.refreshIngredients("Fruit");
                    updateIngredientRV.callback(position,ingredientsUpdate);
                }
                else if (position == 2){
                    ArrayList<IngredientRecyclerModel> ingredientsUpdate= new ArrayList<>();
                    ingredientsUpdate = classObj.refreshIngredients("Meat");
                    updateIngredientRV.callback(position,ingredientsUpdate);
                }
                else if (position == 3){
                    ArrayList<IngredientRecyclerModel> ingredientsUpdate= new ArrayList<>();
                    ingredientsUpdate = classObj.refreshIngredients("HSO");
                    updateIngredientRV.callback(position,ingredientsUpdate);
                }
                else if (position == 4){
                    ArrayList<IngredientRecyclerModel> ingredientsUpdate= new ArrayList<>();
                    ingredientsUpdate = classObj.refreshIngredients("EM");
                    updateIngredientRV.callback(position,ingredientsUpdate);
                }
                else if (position == 5){
                    ArrayList<IngredientRecyclerModel> ingredientsUpdate= new ArrayList<>();
                    ingredientsUpdate = classObj.refreshIngredients("Grain");
                    updateIngredientRV.callback(position,ingredientsUpdate);
                }
                //classObj.loadIngredients("Fruit");
                notifyDataSetChanged();
            }
        });

        if (select){
            if (row_index == position) {
                holder.linearLayout.setBackgroundResource(R.drawable.icon_recycler_bg_selected);
            } else {
                holder.linearLayout.setBackgroundResource(R.drawable.icon_recycler_bg);
            }
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class IconRecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;

        public IconRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.categoryImage);
            textView = itemView.findViewById(R.id.categoryText);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

}