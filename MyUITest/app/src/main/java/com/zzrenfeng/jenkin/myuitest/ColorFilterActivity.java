package com.zzrenfeng.jenkin.myuitest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import com.zzrenfeng.jenkin.myuitest.util.ImageHelper;

public class ColorFilterActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final int MAX_VALUE = 255;
    private static final int MID_VALUE = 127;

    private ImageView meiziImageView;
    private SeekBar hueSeekBar;
    private SeekBar saturationSeekBar;
    private SeekBar lumSeekBar;
    private float mHue = 0.0f;
    private float mSaturation = 1.0f;
    private float mLum = 1.0f;
    private Bitmap mBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_filter);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img_meizi);
        bindViews();
    }


    private void bindViews() {
        meiziImageView = (ImageView) findViewById(R.id.img_meizi);
        hueSeekBar = (SeekBar) findViewById(R.id.sb_hue);
        saturationSeekBar = (SeekBar) findViewById(R.id.sb_saturation);
        lumSeekBar = (SeekBar) findViewById(R.id.sb_lum);

        meiziImageView.setImageBitmap(mBitmap);
        hueSeekBar.setMax(MAX_VALUE);
        hueSeekBar.setProgress(MID_VALUE);
        saturationSeekBar.setMax(MAX_VALUE);
        saturationSeekBar.setProgress(MID_VALUE);
        lumSeekBar.setMax(MAX_VALUE);
        lumSeekBar.setProgress(MID_VALUE);

        hueSeekBar.setOnSeekBarChangeListener(this);
        saturationSeekBar.setOnSeekBarChangeListener(this);
        lumSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_hue:
                mHue = (progress - MID_VALUE) * 1.0F / MID_VALUE * 180;
                break;
            case R.id.sb_saturation:
                mSaturation = progress * 1.0F / MID_VALUE;
                break;
            case R.id.sb_lum:
                mLum = progress * 1.0F / MID_VALUE;
                break;
            default:
                break;
        }
        meiziImageView.setImageBitmap(ImageHelper.handleImageEffect(mBitmap, mHue, mSaturation, mLum));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}







