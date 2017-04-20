package com.example.alex.yandextranslator.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.yandextranslator.R;

import goldzweigapps.tabs.Builder.EasyTabsBuilder;
import goldzweigapps.tabs.Items.TabItem;
import goldzweigapps.tabs.View.EasyTabs;

/**
 * Created by alex on 20.04.17.
 */

public class TabsFragment extends Fragment {
    private EasyTabs easyTabs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs_fragment_layout, container, false);

        initViews(view);

        setTabs();

        return view;
    }

    private void initViews(View view){
        easyTabs = (EasyTabs)view.findViewById(R.id.easyTabs);
    }

    private void setTabs(){
        String nameTabTranslator = getString(R.string.tab_translator);
        String nameTabHistory = getString(R.string.tab_history);
        String nameTabFavorites = getString(R.string.tab_favorites);

        TabFragmentTranslator fragmentTranslator = new TabFragmentTranslator();
        TabFragmentHistory fragmentHistory = new TabFragmentHistory();
        TabFragmentFavorites fragmentFavorites = new TabFragmentFavorites();


        EasyTabsBuilder.with(easyTabs).addTabs(
                new TabItem(fragmentTranslator, nameTabTranslator),
                new TabItem(fragmentHistory, nameTabHistory),
                new TabItem(fragmentFavorites, nameTabFavorites)
        ).Build();
    }

}
