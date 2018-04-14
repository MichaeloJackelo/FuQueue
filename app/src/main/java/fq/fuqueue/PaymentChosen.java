package fq.fuqueue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PaymentChosen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_chosen);
    }

    public void ClickPaypalChosen(View v){
        Intent intent = new Intent(this, PaypalPayment.class);
        startActivity(intent);
    }






}
