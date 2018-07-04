package com.conceptioni.cafeapp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Items{
  @SerializedName("image")
  @Expose
  private String image;
  @SerializedName("item_id")
  @Expose
  private Integer item_id;
  @SerializedName("price")
  @Expose
  private String price;
  @SerializedName("item_name")
  @Expose
  private String item_name;
  @SerializedName("desc")
  @Expose
  private String desc;
  public void setImage(String image){
   this.image=image;
  }
  public String getImage(){
   return image;
  }
  public void setItem_id(Integer item_id){
   this.item_id=item_id;
  }
  public Integer getItem_id(){
   return item_id;
  }
  public void setPrice(String price){
   this.price=price;
  }
  public String getPrice(){
   return price;
  }
  public void setItem_name(String item_name){
   this.item_name=item_name;
  }
  public String getItem_name(){
   return item_name;
  }
  public void setDesc(String desc){
   this.desc=desc;
  }
  public String getDesc(){
   return desc;
  }
}