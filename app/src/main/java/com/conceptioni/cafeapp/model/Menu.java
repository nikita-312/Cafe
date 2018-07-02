package com.conceptioni.cafeapp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Menu{
  @SerializedName("category")
  @Expose
  private List<Category> category;
  public void setCategory(List<Category> category){
   this.category=category;
  }
  public List<Category> getCategory(){
   return category;
  }
}