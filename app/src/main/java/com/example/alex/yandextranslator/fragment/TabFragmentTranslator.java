package com.example.alex.yandextranslator.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.alex.yandextranslator.data.Contract;
import com.example.alex.yandextranslator.model.language.Language;
import com.example.alex.yandextranslator.model.response.LanguageDictionare;
import com.example.alex.yandextranslator.model.response.Translator;
import com.example.alex.yandextranslator.rest.ApiDictionare;
import com.example.alex.yandextranslator.rest.ApiTranslator;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Log.d(LOG_TAG, "Start onAttach");
        super.onAttach(context);
        app = ((App)getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Start onCreateView");
        View view = inflater.inflate(R.layout.translator_tab_fragment_layout, container, false);

        initDictionare();

        initView(view);

        buttonBehavior();

        editTextBehavior();

        return view;
    }

    private void editTextBehavior(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textToYandex = s.toString();
//                Log.d(LOG_TAG, "textToYandex = " + textToYandex);
                String[] codeLanguageToRequest  = getcodeLanguageToRequest();
                app.setCodeLangToRequest(codeLanguageToRequest);

                responseTranslator(app.createMapJson(textToYandex, "translator"));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initDictionare() {
        Log.d(LOG_TAG, "Start initDictionare");
        responseLanguageDictionare(app.createMapJson(null, "dictionare"));
    }

    public void responseLanguageDictionare(Map<String, String> mapJson){
        Log.d(LOG_TAG, "Start responseLanguageDictionare");
        ApiDictionare apiDictionare = app.getApiDictionare();
        Call<LanguageDictionare> call = apiDictionare.languageDictionare(mapJson);

        call.enqueue(new Callback<LanguageDictionare>() {
            @Override
            public void onResponse(Call<LanguageDictionare> call, Response<LanguageDictionare> response) {
                try {
                    Log.d(LOG_TAG, "Start onResponse");
                    if (response.isSuccessful()){
                        LanguageDictionare languageDictionare = response.body();
                        Log.d(LOG_TAG, "listLanguage = " + (languageDictionare.getListLanguage().size()));
                        app.initDataBase(languageDictionare);
                        setIntiTextViewSelectLanguage();

                    } else {
                        //TODO написать обработку ошибок
                    }

                } catch (Exception e) {
                    Log.d(LOG_TAG, "Start Exception");
                    e.printStackTrace();
                    //TODO написать обработку ошибок
                }
            }

            @Override
            public void onFailure(Call<LanguageDictionare> call, Throwable t) {
                Log.d(LOG_TAG, "Start onFailure");
                Log.d(LOG_TAG, "exeption = " + t.toString());
                //TODO написать обработку ошибок
            }
        });
    }

    private void initView(View view) {
        Log.d(LOG_TAG, "Start initView");
        editText = (EditText)view.findViewById(R.id.edit_text);
        button = (Button)view.findViewById(R.id.button);
        textViewTranslate = (TextView)view.findViewById(R.id.text_view_translate);
        textViewLanguageText = (TextView)view.findViewById(R.id.text_view_language_text);
        textViewRevers = (TextView)view.findViewById(R.id.text_view_revers);
        textViewRevers.setText(R.string.revers);
        textViewLanguageTranslation = (TextView)view.findViewById(R.id.text_view_language_translator);
    }

    private void setIntiTextViewSelectLanguage(){
        Log.d(LOG_TAG, "Start setIntiTextViewSelectLanguage");
        if (textViewTranslate.getText().toString().equals("") &&
                textViewLanguageTranslation.getText().toString().equals("")){

            ArrayList<Language> listLanguageText = app.getListLanguage("en");
            textViewLanguageText.setText(listLanguageText.get(0).getLanguage());

            ArrayList<Language> listLanguageTranslation = app.getListLanguage("ru");
            textViewLanguageTranslation.setText(listLanguageTranslation.get(0).getLanguage());
        }

    }

    private void buttonBehavior() {
        Log.d(LOG_TAG, "Start buttonBehavior");
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

//                String textToYandex = getEditText(editText);
////                Log.d(LOG_TAG, "textToYandex = " + textToYandex);
//                String[] codeLanguageToRequest  = getcodeLanguageToRequest();
//                app.setCodeLangToRequest(codeLanguageToRequest);
//
//                responseTranslator(app.createMapJson(textToYandex, "translator"));

                break;
            case R.id.text_view_revers:
                reversTextViewLanguageSelect();
                break;
            case R.id.text_view_language_text:
                responseLanguageDictionare(app.createMapJson(null, "dictionare"));

                DialogLanguageSelect dialogFragmentSelectLanguageText = new DialogLanguageSelect();
                dialogFragmentSelectLanguageText.
                        setArguments(prepareBundleForDialogSelectLanguage(textViewLanguageText));
                dialogFragmentSelectLanguageText.setTargetFragment(this, CHANGE_DATE);
                dialogFragmentSelectLanguageText.show(getFragmentManager(),
                        dialogFragmentSelectLanguageText.getClass().getName());

                break;
            case R.id.text_view_language_translator:
                responseLanguageDictionare(app.createMapJson(null, "dictionare"));

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
        Log.d(LOG_TAG, "Start responseTranslator");

        ApiTranslator apiTranslator = app.getApiTranslator();

        Call<Translator> call = apiTranslator.translate(mapJson);

        call.enqueue(new Callback<Translator>() {
            @Override
            public void onResponse(Call<Translator> call, Response<Translator> response) {
                try {
                    String responseTranslator;
                    if (response.isSuccessful()){
                        responseTranslator = response.body().getText().toString();
                        String textTranslator = mapJson.get("text");
                        String translationDirection = mapJson.get("lang");
                        app.addToHistoryFavoritesTable(textTranslator, responseTranslator,
                                translationDirection, 0);
                    } else {
                        responseTranslator = getString(R.string.error_invalid_responce);
                    }
                    textViewTranslate.setText(responseTranslator);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "exeption onResponce " + e.toString());
                    //TODO написать обработку ошибок
                }
            }
            @Override
            public void onFailure(Call<Translator> call, Throwable t) {
                t.printStackTrace();
                Log.d(LOG_TAG, "exeption onFailure " + t.toString());
                //TODO написать обработку ошибок
            }
        });
    }

    private String[] getcodeLanguageToRequest(){
        Log.d(LOG_TAG, "Start getcodeLanguageToRequest");
        String codeLanguageText = textViewLanguageText.getText().toString();
        String codeLanguageToTranslation = textViewLanguageTranslation.getText().toString();
        return new String[] {codeLanguageText, codeLanguageToTranslation};

    }

    private String getEditText(EditText editText){
        Log.d(LOG_TAG, "Start getEditText");
        String text = editText.getText().toString();
        if (text.length() == 0) text = "";
        return text;
    }

    private void reversTextViewLanguageSelect(){
        Log.d(LOG_TAG, "Start reversTextViewLanguageSelect");
        String stringTextViewLanguageText = textViewLanguageText.getText().toString();
        String stringTextViewLanguageTranslation = textViewLanguageTranslation.getText().toString();
        textViewLanguageText.setText(stringTextViewLanguageTranslation);
        textViewLanguageTranslation.setText(stringTextViewLanguageText);
    }

    private Bundle prepareBundleForDialogSelectLanguage(TextView textView){
        Log.d(LOG_TAG, "Start prepareBundleForDialogSelectLanguage");
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
        Log.d(LOG_TAG, "Start onActivityResult");
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
