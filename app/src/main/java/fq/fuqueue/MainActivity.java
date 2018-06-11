package fq.fuqueue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.beardedhen.androidbootstrap.font.MaterialIcons;

import butterknife.ButterKnife;

import static com.beardedhen.androidbootstrap.font.Typicon.TY_CODE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } finally {
                    starttt();
                }
            }
        };
        thread.start(); //testkrystianplatnosci
        TypefaceProvider.registerDefaultIconSets();
        ButterKnife.bind(this);
    }

    private void starttt() {
        Intent intent = new Intent(this, LoginUser.class);
        startActivity(intent);
    }


}
