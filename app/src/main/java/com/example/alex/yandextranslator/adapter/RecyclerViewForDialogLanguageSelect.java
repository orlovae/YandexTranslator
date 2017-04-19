package com.example.alex.yandextranslator.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.alex.yandextranslator.R;

/**
 * Created by alex on 18.04.17.
 */

public class RecyclerViewForDialogLanguageSelect extends
        RecyclerView.Adapter<RecyclerViewForDialogLanguageSelect.ViewHolder> {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private String[] languages;
    private String languageSelect;
    private Context context;

    public RecyclerViewForDialogLanguageSelect(String[] languages, String languageSelect,
                                               Context context) {
        this.languages = languages;
        this.languageSelect = languageSelect;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.list_view_item_layout_dialog_language_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewForDialogLanguageSelect.ViewHolder holder, int position) {
        holder.textViewLanguage.setText(languages[position]);
        if (languages[position].equals(languageSelect)) {
            holder.textViewLanguage.setBackgroundResource(R.color.colorGrey);
            holder.textViewChecked.setBackgroundResource(R.color.colorGrey);
            holder.textViewChecked.setVisibility(View.VISIBLE);
        }
        holder.textViewLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.textViewLanguage.setBackgroundResource(R.color.colorGrey);
                holder.textViewChecked.setBackgroundResource(R.color.colorGrey);
                holder.textViewChecked.setVisibility(View.VISIBLE);

//                Intent intent = new Intent();
//                intent.putExtra("languageSelect",languages[position]);
//
//                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return languages == null ? 0 : languages.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLanguage, textViewChecked;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View itemView) {
            textViewLanguage = (TextView)itemView.findViewById(R.id.recycler_view_tv_language);
            textViewChecked = (TextView)itemView.findViewById(R.id.recycler_view_tv_language_check);
        }
    }
}
