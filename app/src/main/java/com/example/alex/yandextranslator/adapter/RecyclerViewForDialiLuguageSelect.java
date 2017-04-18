package com.example.alex.yandextranslator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.example.alex.yandextranslator.R;

/**
 * Created by alex on 18.04.17.
 */

public class RecyclerViewForDialiLuguageSelect extends RecyclerView.Adapter<RecyclerViewForDialiLuguageSelect.ViewHolder> {
    private String[] languages;
    private Context context;

    public RecyclerViewForDialiLuguageSelect(String[] languages, Context context) {
        this.languages = languages;
        this.context = context;
    }

    @Override
    public RecyclerViewForDialiLuguageSelect.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                           int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.
                list_view_item_layout_dialog_language_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewForDialiLuguageSelect.ViewHolder holder, int position) {
        holder.checkedTextView.setText(languages[position]);
    }

    @Override
    public int getItemCount() {
        return languages == null ? 0 : languages.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckedTextView checkedTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View itemView) {
            checkedTextView = (CheckedTextView)itemView.findViewById(R.id.text_view_language);

        }
    }
}
