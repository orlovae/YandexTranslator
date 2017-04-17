package com.example.alex.yandextranslator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

/**
 * Created by alex on 17.04.17.
 */

public class LanguageSelectActivity extends AppCompatActivity {
    private TextView textView;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);
        initView();
    }

    private void initView(){
        textView = (TextView)findViewById(R.id.text_view_title);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
    }
}
