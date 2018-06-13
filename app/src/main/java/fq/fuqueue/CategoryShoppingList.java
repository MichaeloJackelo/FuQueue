package fq.fuqueue;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class CategoryShoppingList extends AppCompatActivity {
    ArrayList<Product> filteredProductList = new ArrayList<Product>();
    ArrayList<Product> basketProductsList = new ArrayList<Product>();
    RecyclerView filtered_products_recyclerView;
    RecyclerView basket_Products_recyclerView;
    Spinner spinner_category;
    Spinner spinner_country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_explore_shopping_list);
        TypefaceProvider.registerDefaultIconSets();

        basketProductsList = ProductListManager.getOfflineBasketProducts(this);
        basket_Products_recyclerView = (RecyclerView) findViewById(R.id.offline_list_recyclerView);
        basket_Products_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        final OfflineBasketAdapter offline_basket_adapter = new OfflineBasketAdapter(basketProductsList,this);
        basket_Products_recyclerView.setAdapter(offline_basket_adapter);

        downloadProductList("category","all");
        filtered_products_recyclerView = (RecyclerView) findViewById(R.id.all_products_recyclerView);
        filtered_products_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        final CategoryShoppingListAdapter filtered_product_adapter = new CategoryShoppingListAdapter(filteredProductList,offline_basket_adapter,this);
        filtered_products_recyclerView.setAdapter(filtered_product_adapter);
        spinner_category = (Spinner) findViewById(R.id.spinner_category);
        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        get_spinner_elementrs_from_url("category");
        get_spinner_elementrs_from_url("country");
        final Context context = this;
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sSelected = parent.getItemAtPosition(position).toString();
                if (sSelected == "all")
                {
                    downloadProductList( "category","all");
                }
                else
                {
                    downloadProductList("category",position + "");
                }
                filtered_product_adapter.notify_data_changed();
                android.widget.Toast.makeText(context, "Click at " + position +  sSelected, android.widget.Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sSelected = parent.getItemAtPosition(position).toString();
                if (sSelected == "all")
                {
                    downloadProductList( "country","all");
                }
                else
                {
                    downloadProductList("country",position + "");
                }
                filtered_product_adapter.notify_data_changed();
                android.widget.Toast.makeText(context, "Click at " + position +  sSelected, android.widget.Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TypefaceProvider.registerDefaultIconSets();
    }

    public void downloadProductList(final String download_type, final String category_number)
    {
        final JSONArray[] json = new JSONArray[1];
        final Context context = this;
        Thread thread = new Thread() {
            public void run() {
                try {

                    String url = null;
                    if(category_number == "all")
                    {
                        url = "http://flask-fuque-for-demo.herokuapp.com/products/";
                    }
                    else
                    {
                        if(download_type == "category")
                            url = "http://flask-fuque-for-demo.herokuapp.com/categories/" + category_number + "/products/";
                        else
                            url = "http://flask-fuque-for-demo.herokuapp.com/countries_of_origin/" + category_number + "/products/";
                    }
                    filteredProductList.clear();
                    json[0] = readJsonFromUrl(url);
                    for(int i=0;i<json[0].length();i++)
                    {
                        String productName = null;
                        double productPrize = 0;
                        String productDescription = null;
                        int productbarcode = 0;
                        String productURL = null;
                        try {
                            productName = json[0].getJSONObject(i).getString("name");
                            productPrize = json[0].getJSONObject(i).getDouble("prize");
                            productDescription = json[0].getJSONObject(i).getString("description");
                            productbarcode = json[0].getJSONObject(i).getInt("barcode");
                            productURL = json[0].getJSONObject(i).getString("picture_url");
                            filteredProductList.add(new Product(productName, productPrize, productDescription, 1234, productbarcode, productURL));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (IOException | JSONException e2){}
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private ArrayList<String> get_spinner_elementrs_from_url(final String spinner_name){
        //Creating a string request
        final ArrayList<String>[] categories;
        categories = new ArrayList[2];
        categories[0] = new ArrayList<String>();
        String url = null;
        if(spinner_name == "category")
            url = "http://flask-fuque-for-demo.herokuapp.com/categories/";
        else
            url = "http://flask-fuque-for-demo.herokuapp.com/countries_of_origin/";
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray j = null;
                        try {
                            //Parsing the fetched Json String to JSON Array
                            j = new JSONArray(response);
                           //Calling method getCategories to get the categories from the JSON Array
                            if(spinner_name == "category")
                                categories[0] = getCategories(j);
                            else
                                categories[0] = getCountries(j);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
        return categories[0];
    }
    private ArrayList<String> getCategories(JSONArray j){
        ArrayList<String> categories_array = new ArrayList<String>();
        //Traversing through all the items in the json array
        categories_array.add("all"); //first on list - very useful for showing all products
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                categories_array.add(json.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories_array);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(spinnerAdapter);
        return categories_array;
    }
    private ArrayList<String> getCountries(JSONArray j){
        ArrayList<String> countries_array = new ArrayList<String>();
        //Traversing through all the items in the json array
        countries_array.add("all"); //first on list - very useful for showing all products
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                countries_array.add(json.getString("country_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries_array);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_country.setAdapter(spinnerAdapter);
        return countries_array;
    }
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray json = new JSONArray(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}

