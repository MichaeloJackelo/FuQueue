package fq.fuqueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;



import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;
import com.google.zxing.Result;

import java.util.ArrayList;


import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import static android.Manifest.permission.CAMERA;

public class BarcodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
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
    public void onCameraRequestPermissionResult(int requestCode, String permissionp[], int grantResults[])
    {
        switch(requestCode)
        {
            case REQUEST_CAMERA:
                if(grantResults.length > 0)
                {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted)
                    {
                        Toast.makeText(BarcodeScanner.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(BarcodeScanner.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(BarcodeScanner.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    public void handleResult(Result result) //function for handling result of scanning barcode
    {
        final String scanResult = result.getText();
        final JSONObject[] json = new JSONObject[1];
        final Context context = this;
        Thread thread = new Thread() {
            public void run() {
                try {
                    String ii = "http://flask-fuque-for-demo.herokuapp.com/products/" + scanResult;
                    json[0] = readJsonFromUrl(ii);
                    //android.widget.Toast.makeText(context, ii, android.widget.Toast.LENGTH_SHORT).show();
                    System.out.println(json[0].toString());
                    System.out.println(json[0].get("id"));
                }catch (IOException | JSONException e2){}
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(json[0].toString());


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add product to shopping list?");
        builder.setPositiveButton("No",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                scannerView.resumeCameraPreview(BarcodeScanner.this);
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
                    productName = json[0].getString("name");
                    productPrize =  json[0].getDouble("prize");
                    productDescription = json[0].getString("description");
                    productbarcode = json[0].getInt("barcode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<Product> productList= ProductListManager.getActiveListProducts(context);
                ProductListManager.addProductToList(productList,new Product(productName, productPrize,productDescription,1, productbarcode));
                ProductListManager.storeActiveListProducts(productList,context);
                goToShoppingListActivity();
            }
        });
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

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    private void goToShoppingListActivity(){
        Intent intent = new Intent(BarcodeScanner.this,ActiveShoppingList.class);
        startActivity(intent);
    }
}
