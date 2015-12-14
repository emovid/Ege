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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
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
    final int DOUBLE_PRESS_INTERVAL = 1000;
    long lastPressTime = 0;

    private String phone;
    private String caption;
    private float shadeAlpha;

    Context context;

    LinearLayout inner;
    LinearLayout shade;

    public CallButton(Context context) {
        super(context);
        this.context = context;
        inflateView();
        this.setOnTouchListener(this);
        inner.setOnTouchListener(this);
        shade.setOnTouchListener(this);
    }

    public CallButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CallButtonOptions);

        inflateView();
        parseProperties(a);
        this.setOnTouchListener(this);
        inner.setOnTouchListener(this);
        shade.setOnTouchListener(this);
    }

    public CallButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CallButtonOptions);

        inflateView();
        parseProperties(a);
        this.setOnTouchListener(this);
        inner.setOnTouchListener(this);
        shade.setOnTouchListener(this);
    }

    private void parseProperties(TypedArray a) {
        // Drawable Icon
        Drawable ico = a.getDrawable(R.styleable.CallButtonOptions_icon_src);

        if (ico != null) {
            ImageButton iconBtn = (ImageButton) inner.findViewById(R.id.icon);
            iconBtn.setImageDrawable(ico);
        }

        // Background Color
        int bg = a.getColor(R.styleable.CallButtonOptions_background_color, Color.BLUE);
        inner.setBackgroundColor(bg);

        // Shade Color
        int shade_bg = a.getColor(R.styleable.CallButtonOptions_shade_color, Color.rgb(205, 255, 255));
        float shade_alpha = a.getFloat(R.styleable.CallButtonOptions_shade_alpha, 0.7f);
        shadeAlpha = shade_alpha;
        shade.setBackgroundColor(shade_bg);
        // Initially, makes it invisible
        shade.setAlpha(shadeAlpha);
        shade.setVisibility(View.INVISIBLE);

        // Text
        String text = a.getString(R.styleable.CallButtonOptions_text);
        setCaption(text);

        // Phone
        String phone = a.getString(R.styleable.CallButtonOptions_phone);
        setPhoneNumber(phone);
    }

    private void inflateView() {
        Log.d(QuickCallActivity.PACKAGE_NAME, "inflateView()");
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.call_button_view, null);
        inner = (LinearLayout) v.findViewById(R.id.inner_container);
        shade = (LinearLayout) v.findViewById(R.id.shade_container);
        // Release temporary parent
        v.removeAllViews();
        addView(inner);
        addView(shade);
    }

    public void setPhoneNumber(String phone) {
        phone = (phone == null) ? "110" : phone;
        TextView phoneNum = (TextView) shade.findViewById(R.id.phone);
        phoneNum.setText(phone);
        this.phone = phone;
    }

    public String getPhoneNumber() {
        return this.phone;
    }

    public void setCaption(String caption) {
        caption = (caption == null) ? "Button" : caption;
        TextView captionTv = (TextView) inner.findViewById(R.id.caption);
        captionTv.setText(caption);
        this.caption = caption;
    }

    public String getCaption() {
        return this.caption;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Get current time in nano seconds.
        long pressTime = System.currentTimeMillis();
        long intervalTouchTime = pressTime - lastPressTime;

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.d(QuickCallActivity.PACKAGE_NAME, "onTouch().time-interval :: " + (pressTime - lastPressTime));
            if (intervalTouchTime <= DOUBLE_PRESS_INTERVAL) {
                Log.d(QuickCallActivity.PACKAGE_NAME, "onTouch().action :: Double Tap");
                // If double click...
                Log.d(QuickCallActivity.PACKAGE_NAME, "getPhoneNumber().phone :: " + this.getPhoneNumber().toString());
                ((QuickCallActivity) this.context).callPhoneNumber(this.getPhoneNumber());
            } else {
                // If not double click....
                Log.d(QuickCallActivity.PACKAGE_NAME, "onTouch().action :: A Tap");
                Log.d(QuickCallActivity.PACKAGE_NAME, "onTouch().animate :: Fade to and from " + shadeAlpha);

                AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
                fadeIn.setInterpolator(new LinearInterpolator());
                fadeIn.setDuration(800);
                fadeIn.setFillBefore(false);


                AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
                fadeOut.setDuration(900);
                fadeOut.setInterpolator(new LinearInterpolator());
                fadeOut.setFillAfter(true);


                AnimationSet fades = new AnimationSet(false);
                fades.addAnimation(fadeIn);
                fades.addAnimation(fadeOut);
                shade.startAnimation(fades);
            }
            // record the last time the menu button was pressed.
            lastPressTime = pressTime;
        }

        return true;
    }
}
