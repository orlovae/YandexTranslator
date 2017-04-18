package com.example.alex.yandextranslator;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.yandextranslator.adapter.RecyclerViewForDialogLanguageSelect;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by alex on 17.04.17.
 */

public class DialogLanguageSelect extends DialogFragment implements View.OnClickListener {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private static final int CHANGE_DATE = 2;

    private TextView textViewBack, textViewLanguageText;
    private RecyclerView recyclerView;
    private String[] languages;
    private String languageSelect;
    private String languageSelectFromDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        languages = getArguments().getStringArray("language");
        Arrays.sort(languages);
        languageSelect = getArguments().getString("languageSelect");
        Log.d(LOG_TAG, "languages = " + languages.length);
        Log.d(LOG_TAG, "languageSelect = " + languageSelect);

        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View view = inflater.inflate(R.layout.dialog_language_select, null, false);

        initView(view);
        setRecyclerView();

        return view;
    }

    private void initView(View view){
        textViewBack = (TextView)view.findViewById(R.id.text_view_back);
        textViewLanguageText = (TextView)view.findViewById(R.id.text_view_title);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
    }

    private void setRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerViewForDialogLanguageSelect(languages, languageSelect,
                getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_view_back:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case CHANGE_DATE:
                languageSelectFromDialog = data.getStringExtra("languageSelect");
                Log.d(LOG_TAG, "languageSelectFromDialog = " + languageSelectFromDialog);
                break;
        }
    }
}
