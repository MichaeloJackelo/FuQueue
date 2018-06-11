package fq.fuqueue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import butterknife.ButterKnife;


public class ActiveShoppingList extends AppCompatActivity{
    ArrayList<Product> shoppingList = new ArrayList<Product>();
    RecyclerView recyclerView;
    TextView summary_price;
    ActiveProductAdapter activeProductAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_shopping_list);
        shoppingList = ProductListManager.getActiveListProducts(this);
        Button change_scanner_Button = (Button) findViewById(R.id.button_change_scanner);
        change_scanner_Button.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                scanner_page(v);
            }
        });

        //textview summary price
        summary_price = (TextView) findViewById(R.id.result_price);

        //Recycleview
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        activeProductAdapter = new ActiveProductAdapter(shoppingList,summary_price,this);
        recyclerView.setAdapter(activeProductAdapter);
        TypefaceProvider.registerDefaultIconSets();
        ButterKnife.bind(this);

    }
    @Override
    public void onResume(){
        super.onResume();
        activeProductAdapter.refresh_text_view_summary_price();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    public void scanner_page(View v)
    {
        Intent intent = new Intent(this, BarcodeScanner.class);
        startActivity(intent);
    }
    public void go_payments_page(View v)
    {
        sendListToServer();
        Intent intent = new Intent(this, PaypalPayment.class);
        String[] summary_label = ((String) summary_price.getText()).split(" "); //descriptive string (Summary price xx zł)- we must extract price - xx!
        intent.putExtra("SUM_PRICE",summary_label[2]);
        startActivity(intent);
    }

    private void sendListToServer() {
        String URL = "flask-fuque-for-demo.herokuapp.com/list/1";

        //Convert product list to Json string
        JsonArray result = (JsonArray) new Gson().toJsonTree(shoppingList,
                new TypeToken<List<Product>>() {
                }.getType());
        final String requestBody = result.toString();


        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);


    }
}
