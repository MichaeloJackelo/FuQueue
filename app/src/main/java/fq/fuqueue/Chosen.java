package fq.fuqueue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import butterknife.ButterKnife;

public class Chosen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen);
        TypefaceProvider.registerDefaultIconSets();
        ButterKnife.bind(this);
    }

    public void ClickActivShoppingList(View v)
    {
        Intent intent = new Intent(this, ActiveShoppingList.class);
        startActivity(intent);
    }
    public void ClickStartPayment(View v)
    {
        Intent intent = new Intent(this, PaypalPayment.class);
        startActivity(intent);
    }

    public void scanner_page(View v)
    {
        Intent intent = new Intent(this, BarcodeScanner.class);
        startActivity(intent);
    }
    public void offline_shopping_list_page(View v)
    {
        Intent intent = new Intent(this, CategoryShoppingList.class);
        startActivity(intent);
    }
    public void ClickStoreMap(View v)
    {
        Intent intent = new Intent(this, StoreMap.class);
        startActivity(intent);
    }
    public void ClickDiscountMagazine(View v)
    {
        Intent intent = new Intent(this, DiscountMagazine.class);
        startActivity(intent);
    }

}
