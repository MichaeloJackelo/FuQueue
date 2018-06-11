package fq.fuqueue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import butterknife.ButterKnife;

public class StoreMap extends AppCompatActivity {
    String[] elementy = {"Opcja 1", "Opcja 2", "Opcja 3", "Opcja 4", "Opcja 5"}; // Tu trzeba wszstkie dostępne kategorie w sklepie



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, elementy);
        final Spinner spinner = findViewById(R.id.spinner_category);
        final ImageView img_arrow = findViewById(R.id.image_arrow);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:

                        //float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                        img_arrow.setX(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
                        img_arrow.setY(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
                        break;
                    case 1:
                        img_arrow.setX(47);
                        img_arrow.setY(104);
                        break;
                    case 2:
                        img_arrow.setX(80);
                        img_arrow.setY(204);
                        break;
                    case 3:
                        img_arrow.setX(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics()));
                        img_arrow.setY(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics()));
                        break;
                    case 4:
                        img_arrow.setX(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
                        img_arrow.setY(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        } );
        TypefaceProvider.registerDefaultIconSets();
        ButterKnife.bind(this);


    }



}
