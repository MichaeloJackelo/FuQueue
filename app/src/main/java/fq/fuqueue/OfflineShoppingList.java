package fq.fuqueue;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_shopping_list);
        handleResult("");
    }

    public void handleResult(String result) //function for handling result of scanning barcode
    {
        final String scanResult = result;
        final JSONArray[] json = new JSONArray[1];
        final Context context = this;
        Thread thread = new Thread() {
            public void run() {
                try {
                    String ii = "http://flask-fuque-for-demo.herokuapp.com/products/" + scanResult;
                    json[0] = readJsonFromUrl(ii);
                    //android.widget.Toast.makeText(context, ii, android.widget.Toast.LENGTH_SHORT).show();
                    //System.out.println(json[0].toString());
                    //System.out.println(json[0].get("id"));
                }catch (IOException | JSONException e2){}
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println(json[0].toString());


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add product to shopping list?");
        builder.setPositiveButton("No",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                //scannerView.resumeCameraPreview(BarcodeScanner.this);
            }
        });
        builder.setNeutralButton("Add!", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String productName = null;
                double productPrize = 0;
                String productDescription = null;
                int productbarcode = 0;
                try {

                    productName = json[0].getJSONObject(1).getString("name");
                    productPrize =  json[0].getJSONObject(0).getDouble("prize");
                    /*
                    productDescription = json[0].getString("description");
                    productbarcode = json[0].getInt("barcode");
                    */

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //goToShoppingListActivity();
            }
        });
        ArrayList<Product> product;
        product = ProductListManager.getArrayProducts(this);
        ProductListManager.addProductToList(product, new Product("kupa",4,"xdd",4,126969));
        ProductListManager.storeArrayProducts(product,this);
        builder.setMessage(scanResult);
        AlertDialog alert = builder.create();
        alert.show();
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

