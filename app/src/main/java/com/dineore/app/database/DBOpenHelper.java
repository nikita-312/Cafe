package com.dineore.app.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dineore.app.model.CartData;

import java.util.ArrayList;
import java.util.List;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CAFE.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "ADDTOCART";
    private static final String COLUMN_ITEM_ID = "ID";
    private static final String COLUMN_ITEM_TOTAL_PRICE = "PRICE";
    private static final String COLUMN_EXTRA_PRICE = "EXTRA_PRICE";
    private static final String COLUMN_ORIGINAL_PRICE = "ORIGINAL_PRICE";
    private static final String COLUMN_ITEMS_QUANTITY = "ITEMS_QUANTITY";
    private static final String COLUMN_ITEM_NAME = "NAME";
    private static final String COLUMN_ITEM_TOTAL_QUANTITY = "TOTAL_QUANTITY";
    private static final String COLUMN_USER_ID = "USER_ID";
    private static final String COLUMN_NOTE = "NOTE";
    private static final String COLUMN_CAFE_ID = "CAFE_ID";
    private static final String COLUMN_ITEM_DESC = "ITEM_DESC";
    private static final String COLUMN_ITEM_TYPE = "ITEM_TYPE";
    private static final String COLUMN_ITEM_IMAGE = "ITEM_IMAGE";
    private static final String COLUMN_SUB_TOTAL = "SUB_TOTAL";

    private List<CartData> cartDataArrayListwithid = new ArrayList<>();
    private List<CartData> cartDataArrayList = new ArrayList<>();

    private static final String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_NAME
            + " (" + COLUMN_USER_ID + " TEXT, " + COLUMN_CAFE_ID + " TEXT, " + COLUMN_ITEM_ID + " TEXT, "+ COLUMN_ITEM_NAME + " TEXT, "+ COLUMN_NOTE + " TEXT, "
            + COLUMN_ITEMS_QUANTITY +" TEXT, "+ COLUMN_ITEM_TOTAL_QUANTITY + " TEXT, "+ COLUMN_ORIGINAL_PRICE + " TEXT, " + COLUMN_EXTRA_PRICE + " TEXT, "+
            COLUMN_ITEM_TOTAL_PRICE + " TEXT, " + COLUMN_ITEM_DESC + " TEXT, " + COLUMN_ITEM_TYPE + " TEXT, " + COLUMN_ITEM_IMAGE + " TEXT, "+ COLUMN_SUB_TOTAL + " TEXT"
            + ") ";

    public DBOpenHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /*add data to database*/
    public boolean addCartData(String userid,String cafeid,String itemid,String itemname,String note,String quant,String totalquant,String originalprice,String extraprice,String totalprice,String desc,String itemtype,String image,String subtotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_ID, userid);
        contentValues.put(COLUMN_CAFE_ID, cafeid);
        contentValues.put(COLUMN_ITEM_ID, itemid);
        contentValues.put(COLUMN_ITEM_NAME, itemname);
        contentValues.put(COLUMN_NOTE, note);
        contentValues.put(COLUMN_ITEMS_QUANTITY, quant);
        contentValues.put(COLUMN_ITEM_TOTAL_QUANTITY, totalquant);
        contentValues.put(COLUMN_ORIGINAL_PRICE, originalprice);
        contentValues.put(COLUMN_EXTRA_PRICE, extraprice);
        contentValues.put(COLUMN_ITEM_TOTAL_PRICE, totalprice);
        contentValues.put(COLUMN_ITEM_DESC, desc);
        contentValues.put(COLUMN_ITEM_TYPE, itemtype);
        contentValues.put(COLUMN_ITEM_IMAGE, image);
        contentValues.put(COLUMN_SUB_TOTAL, subtotal);
        db.insert(TABLE_NAME, null, contentValues);

        return true;
    }

    /*get data as per item id*/
    public List<CartData> getCartData(String Userid,String itemid) {
        cartDataArrayListwithid.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = " + Userid + " AND " + COLUMN_ITEM_ID + " = " + itemid , null );
        if (res.getCount()>0){
            res.moveToFirst();
            for (int i = 0; i<res.getCount(); i++) {
                CartData cartData  = new CartData();
                cartData.setCOLUMN_USER_ID(res.getString(0));
                cartData.setCOLUMN_CAFE_ID(res.getString(1));
                cartData.setCOLUMN_ITEM_ID(res.getString(2));
                cartData.setCOLUMN_ITEM_NAME(res.getString(3));
                cartData.setCOLUMN_NOTE(res.getString(4));
                cartData.setCOLUMN_ITEMS_QUANTITY(res.getString(5));
                cartData.setCOLUMN_ITEM_TOTAL_QUANTITY(res.getString(6));
                cartData.setCOLUMN_ORIGINAL_PRICE(res.getString(7));
                cartData.setCOLUMN_EXTRA_PRICE(res.getString(8));
                cartData.setCOLUMN_ITEM_TOTAL_PRICE(res.getString(9));
                cartDataArrayListwithid.add(cartData);
                res.moveToNext();
            }
        }
        return cartDataArrayListwithid;
    }

    /*get all cart data*/
    public List<CartData> getAllCartData(String Userid) {
        cartDataArrayList.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = " + Userid, null );
        if (res.getCount()>0) {
            res.moveToFirst();
            for (int i = 0; i<res.getCount(); i++) {
                CartData cartData  = new CartData();
                cartData.setCOLUMN_USER_ID(res.getString(0));
                cartData.setCOLUMN_CAFE_ID(res.getString(1));
                cartData.setCOLUMN_ITEM_ID(res.getString(2));
                cartData.setCOLUMN_ITEM_NAME(res.getString(3));
                cartData.setCOLUMN_NOTE(res.getString(4));
                cartData.setCOLUMN_ITEMS_QUANTITY(res.getString(5));
                cartData.setCOLUMN_ITEM_TOTAL_QUANTITY(res.getString(6));
                cartData.setCOLUMN_ORIGINAL_PRICE(res.getString(7));
                cartData.setCOLUMN_EXTRA_PRICE(res.getString(8));
                cartData.setCOLUMN_ITEM_TOTAL_PRICE(res.getString(9));
                cartData.setCOLUMN_ITEM_DESC(res.getString(10));
                cartData.setCOLUMN_ITEM_TYPE(res.getString(11));
                cartData.setCOLUMN_IMAGE(res.getString(12));
                cartData.setCOLUMN_SUB_TOTAL(res.getString(13));
                cartDataArrayList.add(cartData);
                res.moveToNext();
            }
        }
        return cartDataArrayList;
    }

    /*update data as per item id*/
    public boolean updatecartdata(String userid,String cafeid,String itemid,String itemname,String note,String quant,String totalquant,String originalprice,String extraprice,String totalprice,String desc,String itemtype,String image,String subtotal){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_ID, userid);
        contentValues.put(COLUMN_CAFE_ID, cafeid);
        contentValues.put(COLUMN_ITEM_ID, itemid);
        contentValues.put(COLUMN_ITEM_NAME, itemname);
        contentValues.put(COLUMN_NOTE, note);
        contentValues.put(COLUMN_ITEMS_QUANTITY, quant);
        contentValues.put(COLUMN_ITEM_TOTAL_QUANTITY, totalquant);
        contentValues.put(COLUMN_ORIGINAL_PRICE, originalprice);
        contentValues.put(COLUMN_EXTRA_PRICE, extraprice);
        contentValues.put(COLUMN_ITEM_TOTAL_PRICE, totalprice);
        contentValues.put(COLUMN_ITEM_DESC, desc);
        contentValues.put(COLUMN_ITEM_TYPE, itemtype);
        contentValues.put(COLUMN_ITEM_IMAGE, image);
        contentValues.put(COLUMN_SUB_TOTAL, subtotal);
        database.update(TABLE_NAME, contentValues, COLUMN_USER_ID + " = " + userid + " AND " + COLUMN_ITEM_ID + " = " + itemid, null);

        return true;
    }

    /*update all row data*/
    public void updateAllcartdata(String userid, String totalquant, String extraprice, String totalprice, String subtotal) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM_TOTAL_QUANTITY, totalquant);
        contentValues.put(COLUMN_EXTRA_PRICE, extraprice);
        contentValues.put(COLUMN_ITEM_TOTAL_PRICE, totalprice);
        contentValues.put(COLUMN_SUB_TOTAL, subtotal);
        database.update(TABLE_NAME, contentValues, COLUMN_USER_ID + " = " + userid, null);
    }

    /*delete table*/
    public void deletetable(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME,null,null);
    }

    /*delete row*/
    public Integer deleterow(String userid,String itemid){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_NAME, COLUMN_USER_ID + " = ? AND " + COLUMN_ITEM_ID + " = ?",new String[] {userid,itemid});
    }
}
