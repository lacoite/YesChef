package com.example.yeschef;

import android.graphics.Bitmap;



public class MealPlanRecyclerModel {
    private String name;
    private Bitmap image;
    private String id;
    private String linkToImage;

    public MealPlanRecyclerModel(String id, String name, Bitmap image, String linkToImage) {
        this.image = image;
        this.name = name;
        this.id = id;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLinkToImage(String linkToImage) {
        this.linkToImage = linkToImage;
    }
}
