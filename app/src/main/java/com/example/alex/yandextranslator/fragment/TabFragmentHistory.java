package com.example.alex.yandextranslator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.alex.yandextranslator.R;

/**
 * Created by alex on 20.04.17.
 */

public class TabFragmentHistory extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private EditText editTextSearch;
    private RecyclerView recyclerViewHistory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Start onCreateView");

        View view = inflater.inflate(R.layout.history_tab_fragment_layout, container, false);

        initView(view);

        return view;
    }

    private void initView(View view){
        editTextSearch = (EditText)view.findViewById(R.id.edit_text_search);
        recyclerViewHistory = (RecyclerView)view.findViewById(R.id.recycler_view_history);
    }

}
