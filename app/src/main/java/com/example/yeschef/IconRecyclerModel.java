package com.example.yeschef;

public class IconRecyclerModel {
    private int image;
    private String text;

    public IconRecyclerModel(int image, String text){
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}
