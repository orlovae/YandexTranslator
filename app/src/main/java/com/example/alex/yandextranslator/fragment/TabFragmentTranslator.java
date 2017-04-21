package com.example.alex.yandextranslator.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alex.yandextranslator.App;
import com.example.alex.yandextranslator.DialogLanguageSelect;
import com.example.alex.yandextranslator.R;
import com.example.alex.yandextranslator.model.language.Language;

import java.util.ArrayList;

import static com.example.alex.yandextranslator.DialogLanguageSelect.ID_TEXTVIEW_CALL;
import static com.example.alex.yandextranslator.DialogLanguageSelect.LANGUAGE_FROM_DIALOG_SELECTED;

/**
 * Created by alex on 20.04.17.
 */

public class TabFragmentTranslator extends Fragment implements View.OnClickListener{

    private final String LOG_TAG = this.getClass().getSimpleName();
    public static final int CHANGE_DATE = 1;

    private TextView textViewTranslate;
    private TextView textViewLanguageText, textViewRevers, textViewLanguageTranslation;
    private EditText editText;
    private Button button;

    private App app;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        app = ((App)getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.translator_tab_fragment_layout, container, false);

        initView(view);

        buttonBehavior();

        return view;
    }

    private void initView(View view) {
        editText = (EditText)view.findViewById(R.id.edit_text);
        button = (Button)view.findViewById(R.id.button);
        textViewTranslate = (TextView)view.findViewById(R.id.text_view_translate);
        textViewLanguageText = (TextView)view.findViewById(R.id.text_view_language_text);
        textViewRevers = (TextView)view.findViewById(R.id.text_view_revers);
        textViewRevers.setText(R.string.revers);
        textViewLanguageTranslation = (TextView)view.findViewById(R.id.text_view_language_translator);
        setIntiTextViewSelectLanguage();
    }

    private void setIntiTextViewSelectLanguage(){
        ArrayList<Language> listLanguageText = app.getListLanguage("en");
        ArrayList<Language> listLanguageTranslation = app.getListLanguage("ru");
        textViewLanguageText.setText(listLanguageText.get(0).getLanguage());
        textViewLanguageTranslation.setText(listLanguageTranslation.get(0).getLanguage());
    }

    private void buttonBehavior() {
        button.setOnClickListener(this);
        textViewLanguageText.setOnClickListener(this);
        textViewRevers.setOnClickListener(this);
        textViewLanguageTranslation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(LOG_TAG, "Start onClick");
        switch (v.getId()) {
            case R.id.button:

                String textToYandex = getEditText(editText);
//                Log.d(LOG_TAG, "textToYandex = " + textToYandex);

//                app.createMapJson(textToYandex, "languageDetection");
//
//                app.responseLanguageDetection();

                String[] codeLanguageToRequest  = getcodeLanguageToRequest();
                app.setCodeLangToRequest(codeLanguageToRequest);

                app.createMapJson(textToYandex, "translator");

                app.responseTranslator();

                Log.d(LOG_TAG, "translation = " + app.getResponseTranslator());

                textViewTranslate.setText(app.getResponseTranslator());

                break;
            case R.id.text_view_revers:
                reversTextViewLanguageSelect();
                break;
            case R.id.text_view_language_text:
                app.responseLanguageDictionare();

                DialogLanguageSelect dialogFragmentSelectLanguageText = new DialogLanguageSelect();
                dialogFragmentSelectLanguageText.
                        setArguments(prepareBundleForDialogSelectLanguage(textViewLanguageText));
                dialogFragmentSelectLanguageText.setTargetFragment(this, CHANGE_DATE);
                dialogFragmentSelectLanguageText.show(getFragmentManager(),
                        dialogFragmentSelectLanguageText.getClass().getName());

                break;
            case R.id.text_view_language_translator:
                app.responseLanguageDictionare();

                DialogLanguageSelect dialogFragmentSelectTranslator = new DialogLanguageSelect();
                dialogFragmentSelectTranslator.
                        setArguments(prepareBundleForDialogSelectLanguage(textViewLanguageTranslation));
                dialogFragmentSelectTranslator.setTargetFragment(this, CHANGE_DATE);
                dialogFragmentSelectTranslator.show(getFragmentManager(),
                        dialogFragmentSelectTranslator.getClass().getName());

                break;
        }
    }

    private String[] getcodeLanguageToRequest(){
        String codeLanguageText = textViewLanguageText.getText().toString();
        String codeLanguageToTranslation = textViewLanguageTranslation.getText().toString();
        return new String[] {codeLanguageText, codeLanguageToTranslation};

    }

    private String getEditText(EditText editText){
        String text = editText.getText().toString();
        if (text.length() == 0) text = "";
        return text;
    }

    private void reversTextViewLanguageSelect(){
        String stringTextViewLanguageText = textViewLanguageText.getText().toString();
        String stringTextViewLanguageTranslation = textViewLanguageTranslation.getText().toString();
        textViewLanguageText.setText(stringTextViewLanguageTranslation);
        textViewLanguageTranslation.setText(stringTextViewLanguageText);
    }

    private Bundle prepareBundleForDialogSelectLanguage(TextView textView){
        String[] language = app.convertArrayList();
        Bundle args = new Bundle();
        args.putStringArray("language", language);
//        Log.d(LOG_TAG, "languages = " + language.length);
        args.putString("languageSelect", textView.getText().toString());
        args.putInt("idTextView", textView.getId());

        return args;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null || resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case CHANGE_DATE:
                String languageSelectFromDialog = data.getStringExtra(LANGUAGE_FROM_DIALOG_SELECTED);
                int idTextView = data.getIntExtra(ID_TEXTVIEW_CALL, -1); //TODO -1 ошибка передачи

                if (idTextView == R.id.text_view_language_text) {
                    textViewLanguageText.setText(languageSelectFromDialog);
                } else {
                    textViewLanguageTranslation.setText(languageSelectFromDialog);
                }

                break;
        }
    }
}
