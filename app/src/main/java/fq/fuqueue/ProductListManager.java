package fq.fuqueue;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by micha on 2018-05-07.
 */

public class ProductListManager {

    static void addProductToBasket(ArrayList<Product> inArrayList, Product newProduct){
        int ProductFound=0;
        for(int i=0; i<inArrayList.size();i++){
            if(inArrayList.get(i).barcode == newProduct.barcode)
            {
                inArrayList.get(i).quantity++;
                ProductFound = 1;
                break;
            }
        }
        if(ProductFound == 0)
            inArrayList.add(newProduct);
    }

    static void storeActiveListProducts(ArrayList<Product> inArrayList, Context app_context)
    {
        android.content.SharedPreferences sharedPreferences = app_context.getSharedPreferences("shared preferences", app_context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        com.google.gson.Gson gson = new com.google.gson.Gson();
        String json = gson.toJson(inArrayList);
        editor.putString("task list", json);
        editor.apply();
    }
    static ArrayList<Product> getActiveListProducts(Context app_context)
    {
        SharedPreferences sharedPreferences = app_context.getSharedPreferences("shared preferences", app_context.MODE_PRIVATE);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        String json = sharedPreferences.getString("task list" , null);
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ArrayList<Product>>(){}.getType();
        ArrayList<Product> product_list = gson.fromJson(json,type);
        if( product_list == null)
        {
            product_list = new ArrayList<Product>();
        }
        return product_list;
    }
    static void storeOfflineBasketProducts(ArrayList<Product> inArrayList, Context app_context)
    {
        android.content.SharedPreferences sharedPreferences = app_context.getSharedPreferences("shared preferences", app_context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        com.google.gson.Gson gson = new com.google.gson.Gson();
        String json = gson.toJson(inArrayList);
        editor.putString("offline list", json);
        editor.apply();
    }
    static ArrayList<Product> getOfflineBasketProducts(Context app_context)
    {
        SharedPreferences sharedPreferences = app_context.getSharedPreferences("shared preferences", app_context.MODE_PRIVATE);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        String json = sharedPreferences.getString("offline list" , null);
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ArrayList<Product>>(){}.getType();
        ArrayList<Product> product_list = gson.fromJson(json,type);
        if( product_list == null)
        {
            product_list = new ArrayList<Product>();
        }
        return product_list;
    }
}
