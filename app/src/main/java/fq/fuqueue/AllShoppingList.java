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

import com.beardedhen.androidbootstrap.TypefaceProvider;

import org.json.JSONException;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import butterknife.ButterKnife;

public class AllShoppingList extends AppCompatActivity {
    ArrayList<Product> offlineProductList = new ArrayList<Product>();
    ArrayList<Product> addedProductsList = new ArrayList<Product>();
    RecyclerView allProducts_recyclerView;
    RecyclerView addedProducts_recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shopping_list);

        addedProductsList = ProductListManager.getOfflineListProducts(this);
        addedProducts_recyclerView = (RecyclerView) findViewById(R.id.offline_list_recyclerView);
        addedProducts_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        OfflineProductAdapter offlineadapter = new OfflineProductAdapter(addedProductsList,this);
        addedProducts_recyclerView.setAdapter(offlineadapter);

        downloadProductList();
        allProducts_recyclerView = (RecyclerView) findViewById(R.id.all_products_recyclerView);
        allProducts_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        allProducts_recyclerView.setAdapter(new AllProductListAdapter(offlineProductList,offlineadapter,this));
        Spinner spinner_category = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.search_categories));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(spinnerAdapter);
        final Context context = this;
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sSelected = parent.getItemAtPosition(position).toString();
                android.widget.Toast.makeText(context, "Clicked at " + position + " element " + sSelected, android.widget.Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TypefaceProvider.registerDefaultIconSets();
        ButterKnife.bind(this);
    }

    public void downloadProductList() //function for handling result of scanning barcode
    {
        final JSONArray[] json = new JSONArray[1];
        final Context context = this;
        Thread thread = new Thread() {
            public void run() {
                try {
                    String url = "http://flask-fuque-for-demo.herokuapp.com/products/";
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
                            offlineProductList.add(new Product(productName, productPrize, productDescription, 1000, productbarcode));
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

