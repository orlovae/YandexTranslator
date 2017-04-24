package com.example.alex.yandextranslator.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alex.yandextranslator.App;
import com.example.alex.yandextranslator.DialogLanguageSelect;
import com.example.alex.yandextranslator.R;
import com.example.alex.yandextranslator.model.Language;
import com.example.alex.yandextranslator.model.response.LanguageDictionare;
import com.example.alex.yandextranslator.model.response.Translator;
import com.example.alex.yandextranslator.rest.ApiDictionare;
import com.example.alex.yandextranslator.rest.ApiTranslator;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.alex.yandextranslator.App.KEY_MAP_JSON_LANG;
import static com.example.alex.yandextranslator.App.KEY_MAP_JSON_TEXT;
import static com.example.alex.yandextranslator.App.KEY_MAP_JSON_TRANSLATOR;
import static com.example.alex.yandextranslator.DialogLanguageSelect.ID_TEXTVIEW_CALL;
import static com.example.alex.yandextranslator.DialogLanguageSelect.LANGUAGE_FROM_DIALOG_SELECTED;

import static com.example.alex.yandextranslator.App.KEY_MAP_JSON_DICTIONARE;

/**
 * Created by alex on 20.04.17.
 */

public class TabFragmentTranslator extends Fragment implements View.OnClickListener{
    public static final int CHANGE_DATE = 1;
    public static final String KEY_BUNDLE_FOR_DIALOG_LANGUAGE = "language";
    public static final String KEY_BUNDLE_FOR_DIALOG_LANGUAGE_SELECT = "languageSelect";
    public static final String KEY_BUNDLE_FOR_DIALOG_ID_TEXT_VIEW = "idTextView";

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

        initDictionare();

        initView(view);

        buttonBehavior();

        return view;
    }

    private void initDictionare() {
        responseLanguageDictionare(app.createMapJson(null, KEY_MAP_JSON_DICTIONARE));
    }

    public void responseLanguageDictionare(Map<String, String> mapJson){
        ApiDictionare apiDictionare = app.getApiDictionare();
        Call<LanguageDictionare> call = apiDictionare.languageDictionare(mapJson);

        call.enqueue(new Callback<LanguageDictionare>() {
            @Override
            public void onResponse(Call<LanguageDictionare> call, Response<LanguageDictionare> response) {
                try {
                    if (response.isSuccessful()){
                        LanguageDictionare languageDictionare = response.body();
                        app.initDataBase(languageDictionare);
                        setInitTextViewSelectLanguage();

                    } else {
                        textViewTranslate.setText(getString(R.string.error_invalid_responce));
                    }

                } catch (Exception e) {
                    textViewTranslate.setText(getString(R.string.error_invalid_responce));
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LanguageDictionare> call, Throwable t) {
                textViewTranslate.setText(getString(R.string.error_invalid_responce));
                t.printStackTrace();
            }
        });
    }

    private void initView(View view) {
        editText = (EditText)view.findViewById(R.id.edit_text);
        button = (Button)view.findViewById(R.id.button);
        textViewTranslate = (TextView)view.findViewById(R.id.text_view_translate);
        textViewLanguageText = (TextView)view.findViewById(R.id.text_view_language_text);
        textViewRevers = (TextView)view.findViewById(R.id.text_view_revers);
        textViewRevers.setText(R.string.revers);
        textViewLanguageTranslation = (TextView)view.findViewById(R.id.text_view_language_translator);
    }

    private void setInitTextViewSelectLanguage(){
        if (textViewTranslate.getText().toString().equals("") &&
                textViewLanguageTranslation.getText().toString().equals("")){

            ArrayList<Language> listLanguageText = app.getListLanguage("en");
            /*По умолчанию язык с которого переводят английский*/
            textViewLanguageText.setText(listLanguageText.get(0).getLanguage());

            ArrayList<Language> listLanguageTranslation = app.getListLanguage("ru");
            /*По умолчанию язык на который переводят - русский*/
            textViewLanguageTranslation.setText(listLanguageTranslation.get(0).getLanguage());
        }
    }

    private void buttonBehavior() {
        button.setOnClickListener(this);
        textViewLanguageText.setOnClickListener(this);
        textViewRevers.setOnClickListener(this);
        textViewLanguageTranslation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:

                String textToYandex = getEditText(editText);
                String[] codeLanguageToRequest  = getcodeLanguageToRequest();
                app.setCodeLangToRequest(codeLanguageToRequest);
                responseTranslator(app.createMapJson(textToYandex, KEY_MAP_JSON_TRANSLATOR));

                break;
            case R.id.text_view_revers:
                reversTextViewLanguageSelect();
                break;
            case R.id.text_view_language_text:
                responseLanguageDictionare(app.createMapJson(null, KEY_MAP_JSON_DICTIONARE));

                DialogLanguageSelect dialogFragmentSelectLanguageText = new DialogLanguageSelect();
                dialogFragmentSelectLanguageText.
                        setArguments(prepareBundleForDialogSelectLanguage(textViewLanguageText));
                dialogFragmentSelectLanguageText.setTargetFragment(this, CHANGE_DATE);
                dialogFragmentSelectLanguageText.show(getFragmentManager(),
                        dialogFragmentSelectLanguageText.getClass().getName());
                break;
            case R.id.text_view_language_translator:
                responseLanguageDictionare(app.createMapJson(null, KEY_MAP_JSON_DICTIONARE));

                DialogLanguageSelect dialogFragmentSelectTranslator = new DialogLanguageSelect();
                dialogFragmentSelectTranslator.
                        setArguments(prepareBundleForDialogSelectLanguage(textViewLanguageTranslation));
                dialogFragmentSelectTranslator.setTargetFragment(this, CHANGE_DATE);
                dialogFragmentSelectTranslator.show(getFragmentManager(),
                        dialogFragmentSelectTranslator.getClass().getName());
                break;
        }
    }

    public void responseTranslator(Map<String, String> mapJson) {
        ApiTranslator apiTranslator = app.getApiTranslator();

        Call<Translator> call = apiTranslator.translate(mapJson);

        call.enqueue(new Callback<Translator>() {
            @Override
            public void onResponse(Call<Translator> call, Response<Translator> response) {
                try {
                    String responseTranslator;
                    if (response.isSuccessful()){
                        responseTranslator = response.body().getText().toString();
                        String textTranslator = mapJson.get(KEY_MAP_JSON_TEXT);
                        String translationDirection = mapJson.get(KEY_MAP_JSON_LANG);
                        app.addToHistoryFavoritesTable(textTranslator, responseTranslator,
                                translationDirection, 0);
                    } else {
                        responseTranslator = getString(R.string.error_invalid_responce);
                    }
                    textViewTranslate.setText(responseTranslator);
                } catch (Exception e) {
                    e.printStackTrace();
                    textViewTranslate.setText(getString(R.string.error_invalid_responce));
                }
            }
            @Override
            public void onFailure(Call<Translator> call, Throwable t) {
                t.printStackTrace();
                textViewTranslate.setText(getString(R.string.error_invalid_responce));
            }
        });
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
        args.putStringArray(KEY_BUNDLE_FOR_DIALOG_LANGUAGE, language);
        args.putString(KEY_BUNDLE_FOR_DIALOG_LANGUAGE_SELECT, textView.getText().toString());
        args.putInt(KEY_BUNDLE_FOR_DIALOG_ID_TEXT_VIEW, textView.getId());

        return args;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null || resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case CHANGE_DATE:
                String languageSelectFromDialog = data.getStringExtra(LANGUAGE_FROM_DIALOG_SELECTED);
                int idTextView = data.getIntExtra(ID_TEXTVIEW_CALL, -1);
                /*-1 ошибка передачи*/

                if (idTextView == R.id.text_view_language_text) {
                    textViewLanguageText.setText(languageSelectFromDialog);
                } else {
                    textViewLanguageTranslation.setText(languageSelectFromDialog);
                }
                break;
        }
    }
}
