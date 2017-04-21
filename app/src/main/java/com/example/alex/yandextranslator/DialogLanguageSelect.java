package com.example.alex.yandextranslator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
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
    public static final String LANGUAGE_FROM_DIALOG_SELECTED = "languageSelectFromDialog";
    public static final String ID_TEXTVIEW_CALL = "idTextViewCall";

    private String[] languages;
    private String languageSelect;
    private String languageSelectFromDialog;
    private int idTextViewCall;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        languages = getArguments().getStringArray("language");
//        Log.d(LOG_TAG, "languages = " + languages.length);
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
            Intent intent = new Intent();
            intent.putExtra(LANGUAGE_FROM_DIALOG_SELECTED, languageSelectFromDialog);
            intent.putExtra(ID_TEXTVIEW_CALL, idTextViewCall);

            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                    intent);

            dismiss();
        }
    };
}
