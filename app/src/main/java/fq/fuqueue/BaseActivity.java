package fq.fuqueue;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.ScrollView;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import butterknife.ButterKnife;

abstract public class BaseActivity extends AppCompatActivity {

    @LayoutRes
    protected abstract int getContentLayoutId();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        TypefaceProvider.registerDefaultIconSets();
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);

        if (scrollView != null) {
            scrollView.addView(LayoutInflater.from(this).inflate(getContentLayoutId(), scrollView, false));
        }

        ButterKnife.bind(this);
    }

}
