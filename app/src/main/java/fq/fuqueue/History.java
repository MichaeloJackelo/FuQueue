package fq.fuqueue;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class History extends AppCompatActivity {
    RecyclerView filtered_products_recyclerView10june;
    RecyclerView filtered_products_recyclerView11june;
    Product Product1a = new Product("ananas",2,"ananas with leafs",10, 12987,"/static/products/cola.jpg");
    Product Product1b = new Product("bananas",1,"yellow banana",4, 1298712,"/static/products/cola.jpg");
    Product Product1c = new Product("cucumber",0.4,"green",11, 1298721,"/static/products/cola.jpg");
    Product Product1d = new Product("dill",1,"dill green",14, 1298731,"/static/products/cola.jpg");
    Product Product2a = new Product("soya",2,"for childrens",21, 1222, "/static/products/cola.jpg");
    Product Product2b = new Product("beetroot",1,"red",7, 13333, "/static/products/cola.jpg");
    Product Product2c = new Product("red cabbage",0.4,"color red",8, 1244, "/static/products/cola.jpg");
    Product Product2d = new Product("broad bean",1,"small beans",2, 1255, "/static/products/cola.jpg");
    ArrayList<Product> products10june = new ArrayList<Product>();
    ArrayList<Product> products11june = new ArrayList<Product>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        products10june.add(Product1a);
        products10june.add(Product1b);
        products10june.add(Product1c);
        products10june.add(Product1d);
        products11june.add(Product2a);
        products11june.add(Product2b);
        products11june.add(Product2c);
        products11june.add(Product2d);
        filtered_products_recyclerView10june = (RecyclerView) findViewById(R.id.history_recyclerView10june);
        filtered_products_recyclerView10june.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        final HistoryAdapter filtered_product_adapter10june = new HistoryAdapter(products10june, this);
        filtered_products_recyclerView10june.setAdapter(filtered_product_adapter10june);
        /*
        filtered_products_recyclerView11june = (RecyclerView) findViewById(R.id.history_recycleView11june);
        filtered_products_recyclerView11june.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        final HistoryAdapter filtered_product_adapter11june = new HistoryAdapter(products11june, this);
        filtered_products_recyclerView11june.setAdapter(filtered_product_adapter11june);
        */
    }
}

