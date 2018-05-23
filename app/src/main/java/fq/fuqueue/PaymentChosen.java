package fq.fuqueue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PaymentChosen extends AppCompatActivity {
    String summary_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_chosen);
        summary_price = (String) getIntent().getStringExtra("SUM_PRICE");
    }

    public void ClickPaypalChosen(View v){
        Intent intent = new Intent(this, PaypalPayment.class);
        intent.putExtra("SUMMARY_PRICE",summary_price);
        startActivity(intent);
    }






}
