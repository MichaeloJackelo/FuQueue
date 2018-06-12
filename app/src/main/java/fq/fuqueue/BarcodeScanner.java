package fq.fuqueue;





import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;



import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import static android.Manifest.permission.CAMERA;

public class BarcodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.M) //jesli wersja apki jest wyższa lub równa wersji M czyli Marshmallow - android 6.0
        {
            if (checkCameraPermission())
            {
                Toast.makeText(BarcodeScanner.this, "Permission is granted!", Toast.LENGTH_LONG).show();
            } else {
                requestCameraPermission();
            }
        }
    }
    private boolean checkCameraPermission()
    {
        return (ContextCompat.checkSelfPermission(BarcodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestCameraPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA},REQUEST_CAMERA);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkCameraPermission())
            {
                if(scannerView == null)
                {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else
            {
                requestCameraPermission();
            }
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result){
        final String scanResult = result.getText();
        final JSONObject[] json = new JSONObject[1];
        String url = "http://flask-fuque-for-demo.herokuapp.com/products/" + scanResult;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Array
                            j = new JSONObject(response);
                            //Calling method getCategories to get the categories from the JSON Array
                            getProduct(j);
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
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        goToShoppingListActivity();
    }


    private void getProduct(JSONObject json){
        String productName = null;
        double productPrize = 0;
        String productDescription = null;
        int productbarcode = 0;
        try {
            //Getting json object
            //JSONObject json = j.getJSONObject(0);
            productName = json.getString("name");
            productPrize =  json.getDouble("prize");
            productDescription = json.getString("description");
            productbarcode = json.getInt("barcode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<Product> productList= ProductListManager.getActiveListProducts(this);
        ProductListManager.addProductToBasket(productList,new Product(productName, productPrize,productDescription,1, productbarcode));
        ProductListManager.storeActiveListProducts(productList,this);
        //goToShoppingListActivity();

    }


    private void goToShoppingListActivity(){
        Intent intent = new Intent(BarcodeScanner.this,ActiveShoppingList.class);
        startActivity(intent);
    }
}
