package com.android.example.leanback.fastlane;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.util.DisplayMetrics;

import com.android.example.leanback.BlurTransform;
import com.android.example.leanback.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kwmt on 2015/08/16.
 */
public class BackgroundHelper {

    private static long BACKGROUND_UPDATE_DELAY = 200;

    private final Handler mHandler = new Handler();

    private Activity mActivity;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundURL;

    private Drawable mDefaultBackground;
    private Target mBackgroundTarget;

    public BackgroundHelper(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.mBackgroundURL = backgroundUrl;
    }

    public void prepareBackgroundManager() {
        BackgroundManager backgroundManager = BackgroundManager.getInstance(mActivity);
        backgroundManager.attach(mActivity.getWindow());

        mBackgroundTarget = new PicassoBackgroundManagerTarget(backgroundManager);
        mDefaultBackground = mActivity.getResources().getDrawable(R.drawable.default_background);

        mMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    protected void updateBackground(String url){
        Picasso.with(mActivity)
                .load(url)
                .resize(mMetrics.widthPixels, mMetrics.heightPixels)
                .centerCrop()
                .transform(BlurTransform.getInstance(mActivity))
                .into(mBackgroundTarget);

        if(null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
    }

    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mBackgroundURL != null){
                        updateBackground(mBackgroundURL);
                    }
                }
            });
        }
    }

    public void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }


    static class PicassoBackgroundManagerTarget implements Target {
        BackgroundManager mBackgroundMagnager;

        public PicassoBackgroundManagerTarget(BackgroundManager backgroundMagnager) {
            this.mBackgroundMagnager = backgroundMagnager;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            this.mBackgroundMagnager.setBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            this.mBackgroundMagnager.setDrawable(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }


        @Override
        public boolean equals(Object o) {
            if (this == o){
                return true;
            }

            if(o == null || getClass() != o.getClass()) {
                return false;
            }

            PicassoBackgroundManagerTarget that = (PicassoBackgroundManagerTarget) o;
            if (!mBackgroundMagnager.equals(that.mBackgroundMagnager)){
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return mBackgroundMagnager.hashCode();
        }
    }
}
