/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.leanback.fastlane;


import android.app.Fragment;
import android.app.LoaderManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.CursorObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.SinglePresenterSelector;
import android.view.View;

import com.android.example.leanback.R;
import com.android.example.leanback.data.VideoDataManager;
import com.android.example.leanback.data.VideoItemContract;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeanbackBrowseFragment extends BrowseFragment {

    private ArrayObjectAdapter mRowsAdapter;

    private static final String[] HEADERS = new String[]{
        "Featured", "Popular","Editor's choice"
    };


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    public void init() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setAdapter(mRowsAdapter);

        setBrandColor(getResources().getColor(R.color.primary));
        setBadgeDrawable(getResources().getDrawable(R.drawable.filmi));


        for (int position = 0; position < HEADERS.length; position++) {
            ObjectAdapter rowContents = new CursorObjectAdapter((new SinglePresenterSelector(new CardPresenter())));
            VideoDataManager manager = new VideoDataManager(getActivity(), getLoaderManager(), VideoItemContract.VideoItem.buildDirUri(), rowContents );
            manager.startDataLoading();

            HeaderItem headerItem = new HeaderItem(position, HEADERS[position], null);
            mRowsAdapter.add(new ListRow(headerItem, manager.getItemList()));
        }

    }
}
