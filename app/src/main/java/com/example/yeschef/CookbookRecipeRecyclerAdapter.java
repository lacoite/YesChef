package com.example.yeschef;

import static com.example.yeschef.GetServerData.recipeURL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yeschef.recipeRVinterface.LoadMore;

import java.util.ArrayList;

class CookbookItemViewHolder extends RecyclerView.ViewHolder{

    TextView id;
    ImageView image;
    TextView name;
    ImageButton star;


    public CookbookItemViewHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.recipeImageC);
        star = itemView.findViewById(R.id.recipeStarC);
        id = itemView.findViewById(R.id.hiddenIDC);
        name = itemView.findViewById(R.id.recipeNameC);
    }
}

public class CookbookRecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;

    CookbookActivity classObj;
    private ArrayList<RecipeRecyclerModel> items;

    public CookbookRecipeRecyclerAdapter(RecyclerView recyclerView, Activity activity, ArrayList<RecipeRecyclerModel> items) {
        this.items = items;
        this.activity = activity;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_recycler_item, parent, false);
       //CookbookRecipeRecyclerViewHolder cookbookRecipeRecyclerViewHolder = new CookbookRecipeRecyclerViewHolder(view);
        return new CookbookItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        classObj = new CookbookActivity();
        RecipeRecyclerModel recipe = items.get(position);
        CookbookItemViewHolder viewHolder = (CookbookItemViewHolder) holder;
        String fullName = recipe.getName();
        viewHolder.id.setText(recipe.getId());
        viewHolder.name.setText(fullName.substring(0,Math.min(fullName.length(), 20))+"...");


        viewHolder.image.setImageBitmap(recipe.getImageLink());
        viewHolder.image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String id = "";
                id = recipe.getId();

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

        viewHolder.star.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String id = recipe.getId();
                Boolean inTable = classObj.checkSaved(id);
                if(inTable){
//                    //remove from table
                    classObj.removeFromSaved(id);
                    viewHolder.star.setColorFilter(Color.argb(0, 0, 0,0));
                    viewHolder.star.setImageResource(R.drawable.star_off);
                } else {
                    //add to table (Need id, name, image, and link)
                    String idToSave = String.valueOf(viewHolder.id.getText());
                    String nameToSave = String.valueOf(viewHolder.name.getText());
                    String imageToSave = "ImageLink";//String.valueOf(viewHolder.image.getText()));

                    classObj.addToSaved(idToSave, nameToSave, imageToSave);//, linkToSave);
                    viewHolder.star.setColorFilter(Color.argb(100, 204, 75,0));
                    viewHolder.star.setImageResource(R.drawable.star_on);
                }

            }
        });

    }



    @Override
    public int getItemCount() {

        return items.size();
    }

}
