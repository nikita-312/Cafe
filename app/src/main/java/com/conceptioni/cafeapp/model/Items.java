package com.conceptioni.cafeapp.model;
import android.media.Image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class Items{
  @SerializedName("image")
  @Expose
  private List<Images> image;
  @SerializedName("item_id")
  @Expose
  private String item_id;
  @SerializedName("price")
  @Expose
  private String price;
  @SerializedName("item_name")
  @Expose
  private String item_name;
  @SerializedName("desc")
  @Expose
  private String desc;

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    @SerializedName("qty")
    @Expose
    private String qty;
  public void setImage(List<Images> image){
   this.image=image;
  }
  public List<Images> getImage(){
   return image;
  }
  public void setItem_id(String item_id){
   this.item_id=item_id;
  }
  public String getItem_id(){
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