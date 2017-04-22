package com.example.alex.yandextranslator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by alex on 20.04.17.
 */

public class TabFragmentHistory extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Start onCreateView");


        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
