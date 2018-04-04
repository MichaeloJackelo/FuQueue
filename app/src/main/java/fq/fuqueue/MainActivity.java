package fq.fuqueue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread thread = new Thread(){
          public void run(){
              try {
                  sleep(2000);
              } catch (InterruptedException e1) {
                  e1.printStackTrace();
              }finally {
                  Intent intent = new Intent(".chosen");
                  startActivity(intent);
              }
          }
        };
        thread.start(); //sdfasdfasdfasd
    }
}
