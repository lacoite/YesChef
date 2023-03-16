package com.example.yeschef;

import android.graphics.Bitmap;

public class RecipeRecyclerModel {

    private String name;
    private Bitmap image;
    private String id;
    private String linkToImage;
    private String saved;

    public RecipeRecyclerModel(String id, String name, Bitmap image, String linkToImage){
        this.id = id;
        this.name = name;
        this.image = image;
        this.linkToImage = linkToImage;
    }

    public String getName(){
        return name;
    }

    public String getLinkToImage(){
        return linkToImage;
    }

    public Bitmap getImageLink(){
        return image;
    }

    public String getId(){
        return id;
    }


}
