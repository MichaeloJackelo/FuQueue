package fq.fuqueue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
public class chosen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen);
        Button clickButton = (Button) findViewById(R.id.button_change);
        clickButton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                next_page(v);
            }
        });
    }
    public void next_page(View v)
    {
        Intent intent = new Intent(this, ActiveShoppingList.class);
        startActivity(intent);
    }
}
