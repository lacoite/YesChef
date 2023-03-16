package com.example.yeschef;

import static com.example.yeschef.GetServerData.recipeURL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yeschef.recipeRVinterface.LoadMore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.lang.Object;

import javax.net.ssl.HttpsURLConnection;



//class for the progress bar that appears when scrolling to load new recipes
class LoadingViewHolder extends RecyclerView.ViewHolder{

    public ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progress_bar);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder{

    public TextView name;
    public ImageView image;
    public TextView id;
    public TextView imageLink;
    public ImageButton star;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.recipeName);
        image = itemView.findViewById(R.id.recipeImage);
        imageLink = itemView.findViewById(R.id.hiddenImageLink);
        id = itemView.findViewById(R.id.hiddenID);
        star = itemView.findViewById(R.id.recipeStar);
    }
}

public class RecipeRecyclerAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    LoadMore loadMore;
    boolean isLoading;

    boolean ready = false;

    Activity activity;
    List<RecipeRecyclerModel> recipes;
    int visibleThreshold = 6;
    int lastVisibleItem, totalItemCount;

    RecipesActivity classObj;

    public RecipeRecyclerAdapter(RecyclerView recyclerView, Activity activity, List<RecipeRecyclerModel> recipes) {
        this.activity = activity;
        this.recipes = recipes;

        final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = gridLayoutManager.getItemCount();
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                if(!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)){
                    if( loadMore != null){
                        loadMore.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return recipes.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    public void setLoadMore(LoadMore loadMore){
        this.loadMore = loadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(activity).inflate(R.layout.recipe_rv_item, parent, false);
            return new ItemViewHolder(view);
        }
        else if(viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(activity).inflate(R.layout.recipe_rv_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            classObj = new RecipesActivity();
            RecipeRecyclerModel recipe = recipes.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            String fullName = recipe.getName();
            viewHolder.imageLink.setText(recipe.getLinkToImage());
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

            boolean saved = classObj.checkSaved(recipe.getId());
            if(saved){
                viewHolder.star.setColorFilter(Color.argb(100, 204, 75,0));
                viewHolder.star.setImageResource(R.drawable.star_on);
            }

            viewHolder.star.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    String id = recipe.getId();
                    Boolean inTable = classObj.checkSaved(id);
                    if(inTable){
                        //remove from table
                        classObj.removeFromSaved(id);
                        viewHolder.star.setColorFilter(Color.argb(0, 0, 0,0));
                        viewHolder.star.setImageResource(R.drawable.star_off);
                    } else {
                        //add to table (Need id, name, image, and link)
                        String idToSave = String.valueOf(viewHolder.id.getText());
                        String nameToSave = String.valueOf(viewHolder.name.getText());
                        String imageToSave = String.valueOf(viewHolder.imageLink.getText());

                        classObj.addToSaved(idToSave, nameToSave, imageToSave);//, linkToSave);
                        viewHolder.star.setColorFilter(Color.argb(100, 204, 75,0));
                        viewHolder.star.setImageResource(R.drawable.star_on);
                    }
                }
            });



        }

        else if(holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setLoaded(){
        isLoading = false;
    }
}

 class GetServerData extends AsyncTask {
    public static String recipeURL = "";
    String responseText;
    List<String> mealList = new ArrayList<String>();;
    StringBuffer response;
    URL url;
    String id;
     public GetServerData(String id) {
         this.id = id;
     }


     Context context;
    GetServerData() {
        this.context = context.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
            try {

                url = new URL("https://api.spoonacular.com/recipes/" +id + "/information?apiKey=e1229b49c5fd4ad8ab87f5efdd15d777");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    // Reading response from input Stream
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String output;
                    response = new StringBuffer();
                    Log.d("WebService", "Response code: " + responseCode);

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
                JSONObject result = new JSONObject(responseText);

                String spoonacularURL = "";
                String mealItem = result.getString("spoonacularSourceUrl");

                spoonacularURL = mealItem;//.getString("spoonacularSourceUrl");

                recipeURL = spoonacularURL;

            } catch (JSONException e) {
                e.printStackTrace();
            }


        return null;
    }

}