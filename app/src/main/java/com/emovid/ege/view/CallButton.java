package com.emovid.ege.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emovid.ege.R;

/**
 * Created by abdillah on 21/11/15.
 */
public class CallButton extends FrameLayout implements View.OnTouchListener {
    final int DOUBLE_PRESS_INTERVAL = 2000;
    long lastPressTime;

    Context context;

    public CallButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.call_button_view, this, true);

        // Drawable Icon
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CallButtonOptions);
        Drawable ico = a.getDrawable(R.styleable.CallButtonOptions_ico_src);

        ImageButton btn = (ImageButton) v.findViewById(R.id.ico);
        btn.setImageDrawable(ico);

        // Background Color
        int bg = a.getColor(R.styleable.CallButtonOptions_background_color, Color.BLACK);
        LinearLayout inner = (LinearLayout) v.findViewById(R.id.inner_container);
        inner.setBackgroundColor(bg);

        // Listener OnTouch (once)
        v.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Get current time in nano seconds.
        long pressTime = System.currentTimeMillis();

        if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
            // If double click...
            // Call activity
        } else {
            // If not double click....
            // Animate
            int widgetWidth = this.getWidth();
            int widgetHeight = this.getHeight();

            ScaleAnimation scale = new ScaleAnimation(widgetHeight, widgetWidth, 0, 0, widgetHeight, widgetWidth);
            this.findViewById(R.id.shade_container).startAnimation(scale);
        }

        // record the last time the menu button was pressed.
        lastPressTime = pressTime;
        return true;
    }
}
