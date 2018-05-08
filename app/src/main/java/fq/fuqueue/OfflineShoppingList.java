package fq.fuqueue;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static fq.fuqueue.BarcodeScanner.readJsonFromUrl;

public class OfflineShoppingList extends AppCompatActivity {
    ArrayList<Product> offlineProductList = new ArrayList<Product>();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_shopping_list);
        downloadProductList();
        recyclerView = findViewById(R.id.offlinerecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new OfflineProductAdapter(offlineProductList,this));
    }

    public void downloadProductList() //function for handling result of scanning barcode
    {
        final JSONArray[] json = new JSONArray[1];
        final Context context = this;
        Thread thread = new Thread() {
            public void run() {
                try {
                    String ii = "http://flask-fuque-for-demo.herokuapp.com/products/";
                    json[0] = readJsonFromUrl(ii);
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
                            offlineProductList.add(new Product(productName, productPrize, productDescription, 1, productbarcode));
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

