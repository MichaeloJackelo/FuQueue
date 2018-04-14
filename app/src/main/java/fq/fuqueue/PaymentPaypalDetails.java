package fq.fuqueue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentPaypalDetails extends AppCompatActivity {

    TextView textView_IdPaypalDetails, textView_AmountPaypalDetails, textView_StatusPaypalDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_paypal_details);

        textView_IdPaypalDetails = (TextView)findViewById(R.id.textView_IdPaypalDetails);
        textView_AmountPaypalDetails = (TextView)findViewById(R.id.textView_AmountPaypalDetails);
        textView_StatusPaypalDetails = (TextView)findViewById(R.id.textView_StatusPaypalDetails);

        Intent intent = getIntent();

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentPaypalDetails"));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentPaypalAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String paymentPaypalAmount) {
        try{
            textView_IdPaypalDetails.setText(response.getString("id"));
            textView_StatusPaypalDetails.setText(response.getString("status"));
            textView_AmountPaypalDetails.setText(response.getString(String.format("$%s",paymentPaypalAmount)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
