package com.conceptioni.cafeapp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Items{
  @SerializedName("item_id")
  @Expose
  private Integer item_id;
  @SerializedName("item_name")
  @Expose
  private String item_name;
  public void setItem_id(Integer item_id){
   this.item_id=item_id;
  }
  public Integer getItem_id(){
   return item_id;
  }
  public void setItem_name(String item_name){
   this.item_name=item_name;
  }
  public String getItem_name(){
   return item_name;
  }
}