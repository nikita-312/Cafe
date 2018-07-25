package com.conceptioni.cafeapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.database.DBOpenHelper;
import com.conceptioni.cafeapp.model.CartData;
import com.conceptioni.cafeapp.model.Items;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DescriptionActivity extends AppCompatActivity {

    String ItemData, ItemId, Qty, ItemName, Price, TotalQty = "",Desc="",Image="",ItemType= "",gst="";
    List<Items> itemsArrayList = new ArrayList<>();
    TextviewRegular ItemPricetvr, Itemnametvr, Itemdesctvr, qtytvr, addtocarttvr;
    EditText noteset;
    ImageView plusiv, minusiv, backiv, ivCart, itemiv;
    String Flag = "A";
    List<CartData> cartDataArrayList = new ArrayList<>();
    DBOpenHelper dbOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        dbOpenHelper = new DBOpenHelper(DescriptionActivity.this);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        init();
        allclick();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        ItemPricetvr = findViewById(R.id.ItemPricetvr);
        Itemnametvr = findViewById(R.id.Itemnametvr);
        Itemdesctvr = findViewById(R.id.Itemdesctvr);
        noteset = findViewById(R.id.noteset);
        qtytvr = findViewById(R.id.qtytvr);
        plusiv = findViewById(R.id.plusiv);
        minusiv = findViewById(R.id.minusiv);
        backiv = findViewById(R.id.backiv);
        addtocarttvr = findViewById(R.id.addtocarttvr);
        ivCart = findViewById(R.id.ivCart);
        itemiv = findViewById(R.id.itemiv);

        if (getIntent().getExtras() != null) {
            ItemId = getIntent().getStringExtra("ItemId");
            TotalQty = getIntent().getStringExtra("Total");
            gst = getIntent().getStringExtra("gst");

            itemsArrayList.clear();
            itemsArrayList = getArrayList();
            for (int i = 0; i < itemsArrayList.size(); i++) {
                if (ItemId.equalsIgnoreCase(itemsArrayList.get(i).getItem_id())) {
                    Itemnametvr.setText(itemsArrayList.get(i).getItem_name());
                    ItemPricetvr.setText(itemsArrayList.get(i).getPrice() + " Rs");
                    Itemdesctvr.setText(itemsArrayList.get(i).getDesc());
                    Qty = itemsArrayList.get(i).getQty();
                    ItemName = itemsArrayList.get(i).getItem_name();
                    Price = itemsArrayList.get(i).getPrice();
                    Desc = itemsArrayList.get(i).getDesc();
                    Image = itemsArrayList.get(i).getImage();
                    ItemType = itemsArrayList.get(i).getItem_type();
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.no_image)
                            .error(R.drawable.no_image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH);

                    Glide.with(DescriptionActivity.this).load(itemsArrayList.get(i).getImage()).apply(options).into(itemiv);
                }
            }
        }

        List<CartData> cartDataList = new ArrayList<>();
        cartDataList.clear();
        cartDataList = dbOpenHelper.getCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), ItemId);
        if (!cartDataList.isEmpty()){
            for (int i = 0; i <cartDataList.size() ; i++) {
                if (ItemId.equalsIgnoreCase(cartDataList.get(i).getCOLUMN_ITEM_ID())){
                    Qty = cartDataList.get(i).getCOLUMN_ITEMS_QUANTITY();
                }
            }
        }
        qtytvr.setText(Qty);

    }

    private void allclick() {
        plusiv.setOnClickListener(v -> {
            Flag = "A";
            int count = Integer.parseInt(Qty);
            int Quantity = count + 1;
            String finalQuantity = String.valueOf(Quantity);
            int totalqty = Integer.parseInt(TotalQty) + 1;
            TotalQty = String.valueOf(totalqty);

            setData(finalQuantity);
            cartDataArrayList = dbOpenHelper.getCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), ItemId);

            if (cartDataArrayList.isEmpty()) {
                boolean isadd = dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), finalQuantity, TotalQty, Price, gst, "",Desc,ItemType,Image,"");
                qtytvr.setText(finalQuantity);
                if (isadd){
                    dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable),TotalQty,gst
                            ,"","");
                }
            } else {
                for (int j = 0; j < cartDataArrayList.size(); j++) {
                    if (cartDataArrayList.get(j).getCOLUMN_ITEM_ID().equalsIgnoreCase(ItemId)) {
                        if (finalQuantity.equalsIgnoreCase("0")) {
                            Integer deletedRows = dbOpenHelper.deleterow(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), ItemId);
                            if (deletedRows > 0) {
                                boolean isupdate = dbOpenHelper.updatecartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), finalQuantity, TotalQty, Price, gst, "", Desc, ItemType , Image,"");
                                if (isupdate) {
                                    dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable),TotalQty,gst,"","");
                                    setData(finalQuantity);
                                }
                            }
                        } else {
                            boolean isupdate = dbOpenHelper.updatecartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), finalQuantity, TotalQty, Price, gst, "", Desc, ItemType, Image,"");
                            if (isupdate) {
                                dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable),TotalQty,gst,"","");
                                setData(finalQuantity);
                            }
                        }
                    } else {
                        boolean isadd = dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), finalQuantity, TotalQty, Price, gst, "", Desc, ItemType, Image,"");
                        qtytvr.setText(finalQuantity);
                        if (isadd){
                            dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable),TotalQty,gst,"","");
                        }
                    }
                }
            }
        });

        minusiv.setOnClickListener(v -> {
            if (!Qty.equalsIgnoreCase("0")) {
                Flag = "A";
                int count = Integer.parseInt(Qty);
                int Quantity = count - 1;
                String finalQuantity = String.valueOf(Quantity);
                int totalqty = Integer.parseInt(TotalQty) - 1;
                TotalQty = String.valueOf(totalqty);
                setData(finalQuantity);
                cartDataArrayList = dbOpenHelper.getCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), ItemId);
                if (cartDataArrayList.isEmpty()) {
                    boolean isadd = dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), finalQuantity, TotalQty, Price, gst, "",Desc,ItemType,Image,"");
                    qtytvr.setText(finalQuantity);
                    if (isadd){
                        dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable),TotalQty,gst,"","");
                    }

                } else {
                    for (int j = 0; j < cartDataArrayList.size(); j++) {
                        if (cartDataArrayList.get(j).getCOLUMN_ITEM_ID().equalsIgnoreCase(ItemId)) {
                            if (finalQuantity.equalsIgnoreCase("0")) {
                                minusiv.setClickable(false);
                                Integer deletedRows = dbOpenHelper.deleterow(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), ItemId);
                                if (deletedRows > 0) {
                                    boolean isupdate = dbOpenHelper.updatecartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), finalQuantity, TotalQty, Price, gst, "", Desc, ItemType , Image,"");
                                    if (isupdate) {
                                        dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable),TotalQty,gst,"","");
                                        setData(finalQuantity);
                                    }
                                }
                            } else {
                                minusiv.setClickable(true);
                                boolean isupdate = dbOpenHelper.updatecartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), finalQuantity, TotalQty, Price, gst, "", Desc, ItemType, Image,"");
                                if (isupdate) {
                                    dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable),TotalQty,gst,"","");
                                    setData(finalQuantity);
                                }
                            }
                        } else {
                            minusiv.setClickable(true);
                            boolean isadd = dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), finalQuantity, TotalQty, Price, gst, "", Desc, ItemType, Image,"");
                            qtytvr.setText(finalQuantity);
                            if (isadd){
                                dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable),TotalQty,gst,"","");
                            }
                        }
                    }
                }
            }
        });

        addtocarttvr.setOnClickListener(v -> {
            onBackPressed();
        });

        backiv.setOnClickListener(v -> onBackPressed());

        ivCart.setOnClickListener(v -> {
            startActivity(new Intent(DescriptionActivity.this, CartActivity.class));
            finish();
        });
    }

    public ArrayList<Items> getArrayList() {
        Gson gson = new Gson();
        ItemData = SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.ItemData, Constant.notAvailable);
        Type type = new TypeToken<ArrayList<Items>>() {
        }.getType();
        return gson.fromJson(ItemData, type);
    }

    @SuppressLint("SetTextI18n")
    private void setData(String Quantity) {
        if (!itemsArrayList.isEmpty()) {
            for (int i = 0; i < itemsArrayList.size(); i++) {
                itemsArrayList.get(i).setQty(Quantity);
                qtytvr.setText(Quantity);
                Qty = Quantity;
                SaveArrylistinShared(itemsArrayList);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void SaveArrylistinShared(List<Items> itemsArrayList) {
        for (int i = 0; i < 1; i++) {
            Gson gson = new Gson();
            String json = gson.toJson(itemsArrayList);
            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.ItemData, json).apply();
        }
    }
}
