package com.example.yeschef;

import static com.example.yeschef.GetServerData.recipeURL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MealPlanRecyclerAdapter extends RecyclerView.Adapter<MealPlanRecyclerAdapter.MealPlanRecyclerViewHolder> {

    private ArrayList<MealPlanRecyclerModel> items;
    MealPlanActivity classObj;

    public MealPlanRecyclerAdapter(ArrayList<MealPlanRecyclerModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MealPlanRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mealplan_recycler_item, parent, false);
        MealPlanRecyclerViewHolder viewHolder = new MealPlanRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanRecyclerViewHolder holder, int position) {
        classObj = new MealPlanActivity();
        MealPlanRecyclerModel currentItem = items.get(position);
        MealPlanRecyclerAdapter.MealPlanRecyclerViewHolder viewHolder = (MealPlanRecyclerAdapter.MealPlanRecyclerViewHolder) holder;
        String fullName = currentItem.getName();
        viewHolder.id.setText(currentItem.getId());

        String nameRN = String.valueOf(currentItem.getName());
        if(nameRN == "Edit To Add Meal"){
            viewHolder.name.setText("Edit To Add Meal");
        }else{
            viewHolder.name.setText(fullName.substring(0,Math.min(fullName.length(), 20))+"...");
        }

        if(currentItem.getLinkToImage() == ""){
            //viewHolder.image.setImageResource(R.drawable.app_icon);
        }else{
            viewHolder.image.setImageBitmap(currentItem.getImageLink());
        }

        if(nameRN != "Edit To Add Meal"){
        viewHolder.image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String id = "";
                id = String.valueOf(viewHolder.id.getText());


                new GetServerData(id).execute();

                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    public void run() {
                        //put your code here
                        String testURL = "";
                        testURL = recipeURL;
                        Intent i = new Intent(v.getContext(), WebViewActivity.class);
                        i.putExtra("key", testURL);
                        v.getContext().startActivity(i);
                    }
                }, 1000);

            }
        });
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MealPlanRecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView id;
        ImageView image;
        TextView name;

        public MealPlanRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.mealplanImage);
            id = itemView.findViewById(R.id.hiddenIDM);
            name = itemView.findViewById(R.id.mealplanText);
        }
    }
}
