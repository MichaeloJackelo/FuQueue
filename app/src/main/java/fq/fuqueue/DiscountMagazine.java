package fq.fuqueue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import butterknife.ButterKnife;

public class DiscountMagazine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_magazine);
        TypefaceProvider.registerDefaultIconSets();
        ButterKnife.bind(this);
    }
}
