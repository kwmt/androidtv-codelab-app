package com.android.example.leanback.fastlane;

import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.android.example.leanback.R;

public class LeanbackBrowseFragment extends BrowseFragment {

    private ArrayObjectAdapter mRowsAdapter;

    public void init() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setAdapter(mRowsAdapter);

        setBrandColor(getResources().getColor(R.color.primary));
        // http://stackoverflow.com/a/29041466/2520998
        setBadgeDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.filmi));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }
}
