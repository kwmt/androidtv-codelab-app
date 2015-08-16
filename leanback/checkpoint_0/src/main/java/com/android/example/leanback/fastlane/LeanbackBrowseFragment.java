package com.android.example.leanback.fastlane;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.CursorObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SinglePresenterSelector;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.android.example.leanback.R;
import com.android.example.leanback.data.Video;
import com.android.example.leanback.data.VideoDataManager;
import com.android.example.leanback.data.VideoItemContract;

import java.io.Serializable;

public class LeanbackBrowseFragment extends BrowseFragment {


    private static final String[] HEADERS = new String[]{
            "Featured", "Popular", "Editor's choice"
    };

    private ArrayObjectAdapter mRowsAdapter;
    private BackgroundHelper bgHelper;

    public void init() {

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setAdapter(mRowsAdapter);

        setBrandColor(getResources().getColor(R.color.primary));
        // http://stackoverflow.com/a/29041466/2520998
        setBadgeDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.filmi));


        for(int position = 0; position < HEADERS.length; position++) {
            ObjectAdapter rowContents = new CursorObjectAdapter((new SinglePresenterSelector(new CardPresenter())));
            VideoDataManager manager = new VideoDataManager(getActivity(),
                    getLoaderManager(),
                    VideoItemContract.VideoItem.buildDirUri(),
                    rowContents);
            manager.startDataLoading();

            HeaderItem headerItem = new HeaderItem(position, HEADERS[position]);
            mRowsAdapter.add(new ListRow(headerItem, manager.getItemList()));
        }

        bgHelper = new BackgroundHelper(getActivity());
        bgHelper.prepareBackgroundManager();


        setOnItemViewClickedListener(getDefaultItemViewClickedListener());
        setOnItemViewSelectedListener(getDefaultItemSelectedListener());

    }

    private OnItemViewClickedListener getDefaultItemViewClickedListener() {
        return new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                Intent intent = new Intent(getActivity(), VideoDetailsActivity.class);
                intent.putExtra(Video.INTENT_EXTRA_VIDEO, (Serializable)item);
                startActivity(intent);

            }
        };
    }

    protected OnItemViewSelectedListener getDefaultItemSelectedListener(){
        return new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                if(item instanceof Video){
                    bgHelper.setBackgroundUrl(((Video) item).getThumbUrl());
                    bgHelper.startBackgroundTimer();
                }
            }
        };
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }
}
