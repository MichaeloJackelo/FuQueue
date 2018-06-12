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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


import butterknife.ButterKnife;


public class DiscountMagazine extends AppCompatActivity {
    ArrayList<Product> filteredProductList = new ArrayList<Product>();
    RecyclerView filtered_products_recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_magazine);
        downloadProductList("category","all");
        filtered_products_recyclerView = (RecyclerView) findViewById(R.id.discount_magazine_recyclerView);
        filtered_products_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        final DiscountMagazineAdapter filtered_product_adapter = new DiscountMagazineAdapter(filteredProductList, this);
        filtered_products_recyclerView.setAdapter(filtered_product_adapter);

    }

    public void downloadProductList(final String download_type, final String category_number)
    {
        final JSONArray[] json = new JSONArray[1];
        final Context context = this;
        Thread thread = new Thread() {
            public void run() {
                try {
                    String url = "http://flask-fuque-for-demo.herokuapp.com/products/";
                    filteredProductList.clear();
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
                            filteredProductList.add(new Product(productName, productPrize, productDescription, 1234, productbarcode));
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

