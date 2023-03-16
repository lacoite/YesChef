package com.example.yeschef;


public class CookbookRecipeRecyclerModel {
    private int image;
    private String text;

    public CookbookRecipeRecyclerModel(int image, String text) {
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

