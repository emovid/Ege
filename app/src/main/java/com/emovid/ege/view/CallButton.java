package com.emovid.ege.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emovid.ege.QuickCallActivity;
import com.emovid.ege.R;
import com.google.android.gms.vision.Frame;

/**
 * Created by abdillah on 21/11/15.
 */
public class CallButton extends FrameLayout implements View.OnTouchListener {
    final int DOUBLE_PRESS_INTERVAL = 3000;
    long lastPressTime;

    private String phone;

    Context context;

    public CallButton(Context context) {
        super(context);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.call_button_view, null);
        LinearLayout inner = (LinearLayout) v.findViewById(R.id.inner_container);
        LinearLayout shade = (LinearLayout) v.findViewById(R.id.shade_container);
        // Release temporary parent
        v.removeAllViews();

        // Listener OnTouch (once)
        this.setOnTouchListener(this);

        addView(inner);
        addView(shade);
    }

    public CallButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.call_button_view, null);
        LinearLayout inner = (LinearLayout) v.findViewById(R.id.inner_container);
        LinearLayout shade = (LinearLayout) v.findViewById(R.id.shade_container);
        // Release temporary parent
        v.removeAllViews();

        // Drawable Icon
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CallButtonOptions);
        Drawable ico = a.getDrawable(R.styleable.CallButtonOptions_icon_src);

        if (ico != null) {
            ImageButton iconBtn = (ImageButton) inner.findViewById(R.id.icon);
            iconBtn.setImageDrawable(ico);
        }

        // Background Color
        int bg = a.getColor(R.styleable.CallButtonOptions_background_color, Color.BLUE);
        inner.setBackgroundColor(bg);

        // Shade Color
        int shade_bg = a.getColor(R.styleable.CallButtonOptions_shade_color, Color.argb(10, 205, 255, 255));
        int shade_alpha = a.getInteger(R.styleable.CallButtonOptions_shade_alpha, 30);
        shade.setBackgroundColor(shade_bg);
        shade.setAlpha(shade_alpha / 255);

        // Text
        String text = a.getString(R.styleable.CallButtonOptions_text);
        text = (text == null) ? "Button" : text;
        TextView caption = (TextView) inner.findViewById(R.id.caption);
        caption.setText(text);

        // Phone
        String phone = a.getString(R.styleable.CallButtonOptions_phone);
        phone = (phone == null) ? "000" : phone;
        TextView phoneNum = (TextView) shade.findViewById(R.id.phone);
        phoneNum.setText(phone);

        // Listener OnTouch (once)
        this.setOnTouchListener(this);

        addView(inner);
        addView(shade);
    }

    public CallButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.call_button_view, null);
        LinearLayout inner = (LinearLayout) v.findViewById(R.id.inner_container);
        LinearLayout shade = (LinearLayout) v.findViewById(R.id.shade_container);
        // Release temporary parent
        v.removeAllViews();

        // Drawable Icon
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CallButtonOptions);
        Drawable ico = a.getDrawable(R.styleable.CallButtonOptions_icon_src);

        if (ico != null) {
            ImageButton iconBtn = (ImageButton) inner.findViewById(R.id.icon);
            iconBtn.setImageDrawable(ico);
        }

        // Background Color
        int bg = a.getColor(R.styleable.CallButtonOptions_background_color, Color.BLUE);
        inner.setBackgroundColor(bg);

        // Shade Color
        int shade_bg = a.getColor(R.styleable.CallButtonOptions_shade_color, Color.argb(10, 205, 255, 255));
        int shade_alpha = a.getInteger(R.styleable.CallButtonOptions_shade_alpha, 30);
        shade.setBackgroundColor(shade_bg);
        shade.setAlpha(shade_alpha / 255);

        // Text
        String text = a.getString(R.styleable.CallButtonOptions_text);
        text = (text == null) ? "Button" : text;
        TextView caption = (TextView) inner.findViewById(R.id.caption);
        caption.setText(text);

        // Phone
        String phone = a.getString(R.styleable.CallButtonOptions_phone);
        phone = (phone == null) ? "000" : phone;
        TextView phoneNum = (TextView) shade.findViewById(R.id.phone);
        phoneNum.setText(phone);

        // Listener OnTouch (once)
        this.setOnTouchListener(this);

        addView(inner);
        addView(shade);
    }

    public void setPhoneNumber(String phone) {
        phone = (phone == null) ? "110" : phone;
        TextView phoneNum = (TextView) this.findViewById(R.id.phone);
        phoneNum.setText(phone);
        this.phone = phone;
    }

    public String getPhoneNumber() {
        return this.phone;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Get current time in nano seconds.
        long pressTime = System.currentTimeMillis();

        if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
            Log.d("Touch Action", "Double Tap");
            // If double click...
            Log.d("PHONE Number", this.getPhoneNumber().toString());
            ((QuickCallActivity) this.context).callPhoneNumber(this.getPhoneNumber());
        } else {
            Log.d("Touch Action", "A Tap");
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
