package com.conceptioni.cafeapp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class Example{
  @SerializedName("cafe_title")
  @Expose
  private String cafe_title;
  @SerializedName("success")
  @Expose
  private Integer success;
  @SerializedName("category")
  @Expose
  private List<Category> category;
  public void setCafe_title(String cafe_title){
   this.cafe_title=cafe_title;
  }
  public String getCafe_title(){
   return cafe_title;
  }
  public void setSuccess(Integer success){
   this.success=success;
  }
  public Integer getSuccess(){
   return success;
  }
  public void setCategory(List<Category> category){
   this.category=category;
  }
  public List<Category> getCategory(){
   return category;
  }
}