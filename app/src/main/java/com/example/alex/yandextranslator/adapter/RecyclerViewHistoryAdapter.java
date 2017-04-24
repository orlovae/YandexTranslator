package com.example.alex.yandextranslator.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.yandextranslator.R;
import com.example.alex.yandextranslator.data.Contract;

/**
 * Created by alex on 22.04.17.
 */

public class RecyclerViewHistoryAdapter extends
        RecyclerView.Adapter<RecyclerViewHistoryAdapter.ViewHolder>{
    private Context context;

    private Cursor dataCursor;

    public RecyclerViewHistoryAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.dataCursor = cursor;
    }

    public Cursor swapCursor (Cursor cursor){
        if (dataCursor == cursor){
            return null;
        }

        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null){
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view_item_history_layout,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        dataCursor.moveToPosition(position);

        int idColIndex = dataCursor.getColumnIndex(Contract.HistoryFavorites.COLUMN_ID);
        int translatableTextColIndex = dataCursor.getColumnIndex(Contract.HistoryFavorites.
                COLUMN_TRANSLATABLE_TEXT);
        int translatedTextColIndex = dataCursor.getColumnIndex(Contract.HistoryFavorites.
                COLUMN_TRANSLATED_TEXT);
        int translationDirectionColIndex = dataCursor.getColumnIndex(Contract.HistoryFavorites.
                COLUMN_TRANSLATION_DIRECTION);
        int favoriteColIndex = dataCursor.getColumnIndex(Contract.HistoryFavorites.
                COLUMN_FAVORITE);

        int id = dataCursor.getInt(idColIndex);
        String translatableFromCursor = dataCursor.getString(translatableTextColIndex);
        String translatedFromCursor = dataCursor.getString(translatedTextColIndex);
        String translationDirectionFromCursor = dataCursor.
                getString(translationDirectionColIndex);
        int favorite = dataCursor.getInt(favoriteColIndex);
        boolean favoriteFromCursor = castIntToBoolean(favorite);

        holder.textViewTranslatableText.setText(translatableFromCursor);
        holder.textViewTranslatedText.setText(translatedFromCursor);
        holder.textViewTranslationDirection.setText(translationDirectionFromCursor);
        if (favoriteFromCursor){
            holder.textViewFavorite.setText(context.getString(R.string.select_favorites));
        } else {
            holder.textViewFavorite.setText(context.getString(R.string.un_select_favorites));
        }
        holder.textViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.textViewFavorite.getText().toString().equals
                        (context.getString(R.string.un_select_favorites))){
                    holder.textViewFavorite.setText(context.getString(R.string.select_favorites));
                } else {
                    holder.textViewFavorite.setText(context.getString(R.string.un_select_favorites));
                }
                selectFavoriteToHistoryFavoritesTable(id, favoriteFromCursor);
            }
        });
    }

    private void selectFavoriteToHistoryFavoritesTable(int id, boolean favorite){
        int intFavorite = castBooleanToInt(favorite);
        ContentValues cv = new ContentValues();
        cv.put(Contract.HistoryFavorites.COLUMN_FAVORITE, intFavorite);

        String selection = Contract.HistoryFavorites.COLUMN_ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        context.getContentResolver().update(Contract.HistoryFavorites.CONTENT_URI, cv, selection,
                selectionArgs);
    }

    private int castBooleanToInt(boolean favorite) {
        if (favorite) {
            return 0;
        } else {
            return 1;
        }
    }

    private boolean castIntToBoolean(int favoriteFromCursor){
        if (favoriteFromCursor == 0){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int getItemCount() {
        return (dataCursor == null) ? 0 : dataCursor.getCount();
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
            textViewTranslatedText = (TextView)itemView.findViewById(R.id.text_view_translated_text);
            textViewTranslationDirection = (TextView)itemView.findViewById(R.id.
                    text_view_translation_direction);
        }
    }
}
