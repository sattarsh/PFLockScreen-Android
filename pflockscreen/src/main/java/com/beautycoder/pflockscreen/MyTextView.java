package com.beautycoder.pflockscreen;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class MyTextView extends androidx.appcompat.widget.AppCompatTextView {


    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        rotate();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        rotate();
    }

    public MyTextView(Context context) {
        super(context);
        init();
        rotate();
    }

    private void rotate() {
        // TODO Auto-generated method stub
        setSelected(true);
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/iran_yekan_bold.ttf");
            setTypeface(tf);
        }
    }
}
