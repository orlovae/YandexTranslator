package com.example.alex.yandextranslator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.yandextranslator.R;
import com.example.alex.yandextranslator.model.HistoryFavorites;

/**
 * Created by alex on 22.04.17.
 */

import java.util.ArrayList;

public class RecyclerViewHistoryAdapter extends
        RecyclerView.Adapter<RecyclerViewHistoryAdapter.ViewHolder>{
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Context context;

    private ArrayList<HistoryFavorites> historyFavoritesArrayList;

    public RecyclerViewHistoryAdapter(Context context,
                                      ArrayList<HistoryFavorites> historyFavoritesArrayList) {
        this.context = context;
        this.historyFavoritesArrayList = historyFavoritesArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view_item_history_layout,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryFavorites historyFavorites = historyFavoritesArrayList.get(position);
        String translatableText = historyFavorites.getTranslatableText();
        String translatedText = historyFavorites.getTranslatedText();
        String translationDirection = historyFavorites.getTranslationDirection();
        boolean favorite = historyFavorites.isFavorite();

        holder.textViewTranslatableText.setText(translatableText);
        holder.textViewTranslatedText.setText(translatedText);
        holder.textViewTranslationDirection.setText(translationDirection);
        if (favorite){
            holder.textViewFavorite.setText(context.getString(R.string.select_favorites));
        } else {
            holder.textViewFavorite.setText(context.getString(R.string.un_select_favorites));
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFavorite;
        TextView textViewTranslatableText;
        TextView textViewTranslatedText;
        TextView textViewTranslationDirection;


        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView){
            textViewFavorite = (TextView)itemView.findViewById(R.id.text_view_favorite);
            textViewTranslatableText = (TextView)itemView.findViewById(R.id.
                    text_view_translatable_text);
            textViewTranslatedText = (TextView)itemView.findViewById(R.id.text_view_translate);
            textViewTranslationDirection = (TextView)itemView.findViewById(R.id.
                    text_view_translation_direction);
        }
    }
}
