package com.example.alex.yandextranslator;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by alex on 17.04.17.
 */

public class DialogLanguageSelect extends DialogFragment {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private String[] languages;
    private String languageSelect;
    private String languageSelectFromDialog;
    private int idTextViewCall;

    public interface DialogLanguageSelectListener {
        void onDialogItemClick(String languageSelectFromDialog, int idTextViewCall);
    }

    DialogLanguageSelectListener listener;

    @Override
    public void onAttach(Context context) {
        Log.d(LOG_TAG, "Start onAttach = ");
        super.onAttach(context);
        try {
            listener = (DialogLanguageSelectListener)getActivity();
            Log.d(LOG_TAG, "listener = " + listener.toString());
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        languages = getArguments().getStringArray("language");
        Log.d(LOG_TAG, "languages = " + languages.length);
        languageSelect = getArguments().getString("languageSelect");
        idTextViewCall = getArguments().getInt("idTextView");
        int intLanguageSelect = -1;

        try {
            Arrays.sort(languages);
            intLanguageSelect = Arrays.asList(languages).indexOf(languageSelect);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        AlertDialog.Builder abd = new AlertDialog.Builder(getActivity());
        abd.setSingleChoiceItems(languages, intLanguageSelect, myClickListener);

        return abd.create();
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            languageSelectFromDialog = languages[which];
            Log.d(LOG_TAG, "languageSelectFromDialog = " + languageSelectFromDialog);
            listener.onDialogItemClick(languageSelectFromDialog, idTextViewCall);
            dismiss();
        }
    };
}
