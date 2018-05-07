package fq.fuqueue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.SharedPreferences;
import android.app.Activity;

import android.view.View;


public class ActiveShoppingList extends AppCompatActivity{
    ArrayList<Product> shoppingList = new ArrayList<Product>();
    ArrayAdapter<String> adapter = null;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_shopping_list);
        shoppingList = getArrayProducts();
        shoppingList.add(new Product("płatki czekoladowe", 10, "takie sobiete płatki",1,12000099));
        storeArrayProducts(shoppingList);
        Button change_scanner_Button = (Button) findViewById(R.id.button_change_scanner);
        change_scanner_Button.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                scanner_page(v);
            }
        });
        //Recycleview
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new ProductAdapter(shoppingList,this));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    public void storeArrayProducts( ArrayList<Product> inArrayList)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        com.google.gson.Gson gson = new com.google.gson.Gson();
        String json = gson.toJson(inArrayList);
        editor.putString("task list", json);
        editor.apply();
    }

    public ArrayList getArrayProducts()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        String json = sharedPreferences.getString("task list" , null);
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ArrayList<Product>>(){}.getType();
        ArrayList<Product> product_list = gson.fromJson(json,type);
        if( product_list == null)
        {
            product_list = new ArrayList<Product>();
        };
        return product_list;
    }

    public void scanner_page(View v)
    {
        Intent intent = new Intent(this, BarcodeScanner.class);
        startActivity(intent);
    }
}
