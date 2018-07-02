package com.conceptioni.cafeapp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class Category{
  @SerializedName("cname")
  @Expose
  private String cname;
  @SerializedName("items")
  @Expose
  private List<Items> items;
  @SerializedName("cid")
  @Expose
  private Integer cid;
  public void setCname(String cname){
   this.cname=cname;
  }
  public String getCname(){
   return cname;
  }
  public void setItems(List<Items> items){
   this.items=items;
  }
  public List<Items> getItems(){
   return items;
  }
  public void setCid(Integer cid){
   this.cid=cid;
  }
  public Integer getCid(){
   return cid;
  }
}