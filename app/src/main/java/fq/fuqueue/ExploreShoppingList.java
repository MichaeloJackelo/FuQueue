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

import fq.fuqueue.Config.Config;

public class ExploreShoppingList extends AppCompatActivity {
    ArrayList<Product> offlineProductList = new ArrayList<Product>();
    ArrayList<Product> addedProductsList = new ArrayList<Product>();
    ArrayList<String> categories_array = new ArrayList<String>();
    RecyclerView allProducts_recyclerView;
    RecyclerView addedProducts_recyclerView;
    Spinner spinner_category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_shopping_list);

        addedProductsList = ProductListManager.getOfflineListProducts(this);
        addedProducts_recyclerView = (RecyclerView) findViewById(R.id.offline_list_recyclerView);
        addedProducts_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        final OfflineProductAdapter offlineadapter = new OfflineProductAdapter(addedProductsList,this);
        addedProducts_recyclerView.setAdapter(offlineadapter);

        downloadProductList("all");
        allProducts_recyclerView = (RecyclerView) findViewById(R.id.all_products_recyclerView);
        allProducts_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        allProducts_recyclerView.setAdapter(new AllProductListAdapter(offlineProductList,offlineadapter,this));
        spinner_category = (Spinner) findViewById(R.id.spinner_category);
        getCategoriesfromurl();
        final Context context = this;
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sSelected = parent.getItemAtPosition(position).toString();
                if (sSelected == "all")
                {
                    downloadProductList( "all");
                }
                else
                {
                    downloadProductList(position + "");
                }
                offlineadapter.notify_data_changed();
                android.widget.Toast.makeText(context, "Click at " + position +  sSelected, android.widget.Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void downloadProductList(final String category_number)
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
                        url = "http://flask-fuque-for-demo.herokuapp.com/categories/" + category_number + "/products/";
                    }
                    offlineProductList.clear();
                    json[0] = readJsonFromUrl(url);
                    for(int i=0;i<json[0].length();i++)
                    {
                        String productName = null;
                        double productPrize = 0;
                        String productDescription = null;
                        int productbarcode = 0;
                        try {
                            productName = json[0].getJSONObject(i).getString("name");
                            productPrize = json[0].getJSONObject(i).getDouble("prize");
                            productDescription = json[0].getJSONObject(i).getString("description");
                            productbarcode = json[0].getJSONObject(i).getInt("barcode");
                            offlineProductList.add(new Product(productName, productPrize, productDescription, 1234, productbarcode));
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
    private ArrayList<String> getCategoriesfromurl(){
        //Creating a string request
        final ArrayList<String>[] categories;
        categories = new ArrayList[2];
        categories[0] = new ArrayList<String>();
        String url = "http://flask-fuque-for-demo.herokuapp.com/categories/";
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray j = null;
                        try {
                            //Parsing the fetched Json String to JSON Array
                            j = new JSONArray(response);
                           //Calling method getCategories to get the categories from the JSON Array
                            categories[0] = getCategories(j);
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

