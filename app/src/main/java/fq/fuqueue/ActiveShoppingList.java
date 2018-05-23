package fq.fuqueue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.Button;

import java.util.ArrayList;

import android.view.View;
import android.widget.TextView;


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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        activeProductAdapter = new ActiveProductAdapter(shoppingList,summary_price,this);
        recyclerView.setAdapter(activeProductAdapter);
        //summary_price.setText(new Double(new ActiveProductAdapter(shoppingList, this).summaryPrice()).toString());
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
        Intent intent = new Intent(this, PaypalPayment.class);
        String[] summary_label = ((String) summary_price.getText()).split(" "); //descriptive string (Summary price xx zł)- we must extract price - xx!
        intent.putExtra("SUM_PRICE",summary_label[2]);
        startActivity(intent);
    }
}
