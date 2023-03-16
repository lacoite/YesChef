package com.example.yeschef;

import android.graphics.Bitmap;

public class DummyRecyclerModel {
    private String name;
    private Bitmap image;
    private String id;
    private String linkToImage;

    public DummyRecyclerModel(String id, String name, Bitmap image, String linkToImage) {
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


}
