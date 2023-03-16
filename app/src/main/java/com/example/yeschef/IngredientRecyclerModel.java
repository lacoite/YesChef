package com.example.yeschef;

public class IngredientRecyclerModel {

    private String ingredient;
    private String type;
    private String selected;

    public IngredientRecyclerModel( String ingredient, String selected, String type){
        this.ingredient = ingredient;
        this.selected = selected;
        this.type = type;
    }
    public String getSelected(){ return selected; }
    public String getIngredient(){
            return ingredient;
    }
    public String getType(){return type; }
}
