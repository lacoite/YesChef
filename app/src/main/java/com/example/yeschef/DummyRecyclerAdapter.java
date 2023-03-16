package com.example.yeschef;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DummyRecyclerAdapter extends RecyclerView.Adapter<DummyRecyclerAdapter.DummyRecyclerViewHolder> {

    private ArrayList<DummyRecyclerModel> items;
    public MealPlanEditDummyActivity classObj = new MealPlanEditDummyActivity();

    public DummyRecyclerAdapter(ArrayList<DummyRecyclerModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public DummyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mealplan_recycler_item, parent, false);
        DummyRecyclerViewHolder viewHolder = new DummyRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DummyRecyclerViewHolder holder, int position) {
        DummyRecyclerModel currentItem = items.get(position);
        DummyRecyclerViewHolder viewHolder = (DummyRecyclerViewHolder) holder;
        String fullName = currentItem.getName();
        Log.i("SAVED ID: ", currentItem.getId());
        viewHolder.id.setText(currentItem.getId());
        viewHolder.name.setText(fullName.substring(0,Math.min(fullName.length(), 20))+"...");
        holder.image.setImageBitmap(currentItem.getImageLink());

        holder.image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("shit", currentItem.getName());
                //classObj.onClickDummy(currentItem.getText());
                Intent intent = new Intent(v.getContext(), MealPlanEditDummyActivity.class);
                //int num = classObj.onRadioButtonClicked();
                intent.putExtra("name", currentItem.getName());
                intent.putExtra("image", currentItem.getLinkToImage());
                intent.putExtra("id", currentItem.getId());
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class DummyRecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView id;
        ImageView image;
        TextView name;

        public DummyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.mealplanImage);
            id = itemView.findViewById(R.id.hiddenIDM);
            name = itemView.findViewById(R.id.mealplanText);
        }
    }
}
