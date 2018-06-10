package fq.fuqueue;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;

import com.beardedhen.androidbootstrap.AwesomeTextView;

public class IconTextView extends AwesomeTextView {
    private final Context context;

    public IconTextView(Context context) {
        super(context);
        this.context = context;
        createView();
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createView();
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        createView();
    }
    private void createView(){
        setGravity(Gravity.CENTER);
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "FontAwesome.otf");
        setTypeface(font);
    }
}
