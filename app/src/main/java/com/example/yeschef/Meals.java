package com.example.yeschef;

public class Meals {
    public int idMeal;
    public String strMeal;
    public String strCategory;
    public String strArea;
    public String strInstructions;
    public String strMealThumb;
    public String strIngredient1;


    public Meals(int idMeal,String strMeal,String strCategory,String strArea,String strInstructions,String strMealThumb,String strIngredient1) {
        this.idMeal = idMeal;
        this.strMeal = strMeal;
        this.strCategory = strCategory;
        this.strArea = strArea;
        this.strInstructions = strInstructions;
        this.strMealThumb = strMealThumb;
        this.strIngredient1 = strIngredient1;

    }

    public int getidMeal(){return idMeal;}
    public String getstrMeal() {return strMeal;}
    public String getstrCategory() {return strCategory;}
    public String getstrArea() {return strArea;}
    public String getstrInstructions() {return strInstructions;}
    public String getstrMealThumb() {return strMealThumb;}
    public String getstrIngredient1() {return strIngredient1;}


    //public void setDescription(String description) {
    //   this.description = description;
    //}

}

