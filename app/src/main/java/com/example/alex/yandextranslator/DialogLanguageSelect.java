package com.example.alex.yandextranslator;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by alex on 17.04.17.
 */

public class DialogLanguageSelect extends DialogFragment implements View.OnClickListener {
    private TextView textViewBack, textViewLanguageText;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View view = inflater.inflate(R.layout.dialog_language_select, null, false);

        initView(view);

        return view;
    }

    private void initView(View view){
        textViewBack = (TextView)view.findViewById(R.id.text_view_back);
        textViewLanguageText = (TextView)view.findViewById(R.id.text_view_title);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_view_back:
                break;
        }

    }
}
