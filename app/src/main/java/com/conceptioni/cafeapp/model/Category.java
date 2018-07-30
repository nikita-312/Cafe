package com.conceptioni.cafeapp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Category {
  @SerializedName("cimage")
  @Expose
  private String cimage;
  @SerializedName("cname")
  @Expose
  private String cname;
  @SerializedName("items")
  @Expose
  private List<Items> items;
  @SerializedName("cid")
  @Expose
  private String cid;
  public void setCimage(String cimage){
   this.cimage=cimage;
  }
  public String getCimage(){
   return cimage;
  }
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
  public void setCid(String cid){
   this.cid=cid;
  }
  public String getCid(){
   return cid;
  }

  public boolean isselect;

    public boolean isIsselect() {
        return isselect;
    }

    public void setIsselect(boolean isselect) {
        this.isselect = isselect;
    }
}