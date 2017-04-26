package com.example.alex.yandextranslator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import java.util.Arrays;

import static com.example.alex.yandextranslator.fragment.TabFragmentTranslator.KEY_BUNDLE_FOR_DIALOG_ID_TEXT_VIEW;
import static com.example.alex.yandextranslator.fragment.TabFragmentTranslator.KEY_BUNDLE_FOR_DIALOG_LANGUAGE;
import static com.example.alex.yandextranslator.fragment.TabFragmentTranslator.KEY_BUNDLE_FOR_DIALOG_LANGUAGE_SELECT;

/**
 * Created by alex on 17.04.17.
 */

public class DialogLanguageSelect extends DialogFragment {
    public static final String LANGUAGE_FROM_DIALOG_SELECTED = "languageSelectFromDialog";
    public static final String ID_TEXTVIEW_CALL = "idTextViewCall";

    private String[] languages;
    private int idTextViewCall;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        languages = getArguments().getStringArray(KEY_BUNDLE_FOR_DIALOG_LANGUAGE);
        String languageSelect = getArguments().getString(KEY_BUNDLE_FOR_DIALOG_LANGUAGE_SELECT);
        idTextViewCall = getArguments().getInt(KEY_BUNDLE_FOR_DIALOG_ID_TEXT_VIEW);
        int intLanguageSelect = -1; /*-1 значит никакой язык не выбран*/

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
            String languageSelectFromDialog = languages[which];
            Intent intent = new Intent();
            intent.putExtra(LANGUAGE_FROM_DIALOG_SELECTED, languageSelectFromDialog);
            intent.putExtra(ID_TEXTVIEW_CALL, idTextViewCall);

            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                    intent);

            dismiss();
        }
    };
}
