package com.android.example.leanback.fastlane;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.example.leanback.R;
import com.android.example.leanback.data.Video;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class CardPresenter extends Presenter {

    private static int CARD_WIDTH = 200;
    private static int CARD_HEIGHT = 200;

    private static Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        Log.d("onCreateViewHolder", "creating viewholder");
        mContext = viewGroup.getContext();
        ImageCardView cardView = new ImageCardView(mContext);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        ((TextView)cardView.findViewById(R.id.content_text)).setTextColor(Color.LTGRAY);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Video video = (Video)item;
        ((ViewHolder)viewHolder).mCardView.setTitleText(video.getTitle());
        ((ViewHolder)viewHolder).mCardView.setContentText(video.getDescription());
        ((ViewHolder)viewHolder).mCardView.setMainImageDimensions(CARD_WIDTH * 2, CARD_HEIGHT * 2);
        ((ViewHolder)viewHolder).updateCardViewImage(video.getThumbUrl());

    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    static class ViewHolder extends Presenter.ViewHolder {
        private ImageCardView mCardView;
        private Drawable mDefaultCardImage;
        private PicassoImageCardViewTarget mImageCardViewTarget;

        public ViewHolder(View view) {
            super(view);
            mCardView = (ImageCardView) view;
            mImageCardViewTarget = new PicassoImageCardViewTarget(mCardView);
            mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.filmi);
        }

        protected void updateCardViewImage(String url){
            Picasso.with(mContext)
                    .load(url)
                    .resize(CARD_WIDTH * 2, CARD_HEIGHT * 2)
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .into(mImageCardViewTarget);
        }
    }

    static class PicassoImageCardViewTarget implements Target {

        private ImageCardView mImageCardView;

        public PicassoImageCardViewTarget(ImageCardView mImageCardView) {
            this.mImageCardView = mImageCardView;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Drawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), bitmap);
            mImageCardView.setMainImage(bitmapDrawable);

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            mImageCardView.setMainImage(errorDrawable);

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }

}
