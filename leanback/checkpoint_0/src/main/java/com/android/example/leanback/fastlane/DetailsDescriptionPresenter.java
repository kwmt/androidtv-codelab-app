package com.android.example.leanback.fastlane;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;
import android.util.Log;

import com.android.example.leanback.data.Video;

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {
    @Override
    protected void onBindDescription(ViewHolder vh, Object item) {
        Video video = (Video) item;
        if (video != null) {
            Log.d("Presenter", String.format("%s, %s, %s", video.getTitle(), video.getThumbUrl(), video.getDescription()));
            vh.getTitle().setText(video.getTitle());
            vh.getSubtitle().setText(String.valueOf(video.getRating()));
            vh.getBody().setText(video.getDescription());
        }
    }
}
