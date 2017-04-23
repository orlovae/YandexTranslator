package com.example.alex.yandextranslator.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.alex.yandextranslator.App;
import com.example.alex.yandextranslator.R;
import com.example.alex.yandextranslator.adapter.RecyclerViewFavoriteAdapter;
import com.example.alex.yandextranslator.adapter.RecyclerViewHistoryAdapter;
import com.example.alex.yandextranslator.data.Contract;
import com.example.alex.yandextranslator.model.HistoryFavorites;

import java.util.ArrayList;

/**
 * Created by alex on 20.04.17.
 */

public class TabFragmentFavorites extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private EditText editTextSearch;
    private RecyclerView recyclerViewFavorite;
    private RecyclerViewFavoriteAdapter adapter;

    private App app;

    private int LOADER_ID = 2;

    @Override
    public void onAttach(Context context) {
        Log.d(LOG_TAG, "Start onAttach");
        super.onAttach(context);
        app = ((App)getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Start onCreateView");

        View view = inflater.inflate(R.layout.history_tab_fragment_layout, container, false);

        initView(view);

        initRecyclerView();

        return view;
    }

    private void initView(View view){
        editTextSearch = (EditText)view.findViewById(R.id.edit_text_search);
        recyclerViewFavorite = (RecyclerView)view.findViewById(R.id.recycler_view_history);
    }

    private void initRecyclerView(){
        Log.d(LOG_TAG, "Start initRecyclerView");
        ArrayList<HistoryFavorites> arrayList = app.getHistoryFavoritesArrayList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new RecyclerViewFavoriteAdapter(getActivity(), null);
        recyclerViewFavorite.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        recyclerViewFavorite.setAdapter(adapter);
        recyclerViewFavorite.setLayoutManager(layoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = Contract.HistoryFavorites.COLUMN_FAVORITE + " = ?";
        String[] selectionArgs = {Integer.toString(1)};

        return new CursorLoader(getActivity(),
                Contract.HistoryFavorites.CONTENT_URI, null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
