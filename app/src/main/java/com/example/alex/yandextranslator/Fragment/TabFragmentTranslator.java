package com.example.alex.yandextranslator.Fragment;

import android.database.Cursor;
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

import com.example.alex.yandextranslator.DialogLanguageSelect;
import com.example.alex.yandextranslator.R;
import com.example.alex.yandextranslator.adapter.CursorToMapLanguageAdapter;
import com.example.alex.yandextranslator.adapter.LanguageDictionareAdapter;
import com.example.alex.yandextranslator.data.Contract;
import com.example.alex.yandextranslator.model.language.Language;
import com.example.alex.yandextranslator.model.response.LanguageDictionare;
import com.example.alex.yandextranslator.rest.ApiClient;
import com.example.alex.yandextranslator.rest.ApiDictionare;
import com.example.alex.yandextranslator.rest.ApiLanguageDetection;
import com.example.alex.yandextranslator.rest.ApiTranslator;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 20.04.17.
 */

public class TabFragmentTranslator extends Fragment implements View.OnClickListener {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private TextView textViewTranslate;
    private TextView textViewLanguageText, textViewRevers, textViewLanguageTranslation;
    private EditText editText;
    private Button button;

    private DialogLanguageSelect dialogFragmentSelectLanguageText, dialogFragmentSelectTranslator;

    private ApiTranslator apiTranslator;
    private ApiLanguageDetection apiLanguageDetection;
    private ApiDictionare apiDictionare;

    private ArrayList<Language> listLanguage;
    private ArrayList<Language> listLanguageText, listLanguageTranslation;

    private Map<String, String> mapJson;

    private Gson gson;

    private final String KEY = "trnsl.1.1.20170407T081255Z.343fc6903b3656af.58d14da04ebc826dbc32072d91d8e3034d99563f";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.translator_tab_fragment_layout, container, false);

        initDictionare();

        initView(view);

        initApiLanguageDetection();

        initApiTranslator();

        buttonBehavior();

        return view;
    }

    private void initDictionare() {
        createMapJson(null, "dictionare", null);
        initApiLanguageDictionare();
        responseLanguageDictionare();

    }

    private void createMapJson(String textToYandex, String key, String lang) {
        Log.d(LOG_TAG, "Start createMapJson");
        if (mapJson == null) {
            mapJson = new HashMap<>();
        } else {
            mapJson.clear();
        }

        switch (key) {
            case "dictionare":
//                Log.d(LOG_TAG, "Start createMapJson, case \"dictionare\"");
                mapJson.put("key", KEY);
                mapJson.put("ui", "ru");
//                Log.d(LOG_TAG, "mapJson = " + mapJson.toString());
                break;
            case "languageDetection":
//                Log.d(LOG_TAG, "Start createMapJson, case \"languageDetection\"");
                mapJson.put("key", KEY);
                mapJson.put("text", textToYandex);
//                Log.d(LOG_TAG, "mapJson = " + mapJson.toString());
                break;
            case "translator":
//                Log.d(LOG_TAG, "Start createMapJson, case \"translator\"");
                mapJson.put("key", KEY);
                mapJson.put("text", textToYandex);
                mapJson.put("lang", lang);
//                Log.d(LOG_TAG, "mapJson = " + mapJson.toString());
                break;
        }
    }

    private void initApiLanguageDictionare(){
        initGson();
        apiDictionare = ApiClient.getClient(gson).create(ApiDictionare.class);
    }

    private void initGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LanguageDictionare.class, new LanguageDictionareAdapter());
        gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
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
        listLanguageText = getListLanguage("en");
        listLanguageTranslation = getListLanguage("ru");
        textViewLanguageText.setText(listLanguageText.get(0).getLanguage());
        textViewLanguageTranslation.setText(listLanguageTranslation.get(0).getLanguage());
    }

    private ArrayList<Language> getListLanguage(String codeLanguage){
        String selection = Contract.Language.COLUMN_CODE_LANGUAGE + "=?";
        String[] selectionArgs = {codeLanguage};

        Cursor cursor = getActivity().getContentResolver().query(Contract.Language.CONTENT_URI,
                null, selection, selectionArgs, null);
        CursorToMapLanguageAdapter cursorToMapLanguageAdapter = new CursorToMapLanguageAdapter(cursor);

        return cursorToMapLanguageAdapter.getListToCursor();
    }

    private void initApiLanguageDetection(){
        apiLanguageDetection = ApiClient.getClient().create(ApiLanguageDetection.class);
    }

    private void initApiTranslator(){
        apiTranslator = ApiClient.getClient().create(ApiTranslator.class);
    }

    private void buttonBehavior() {
        button.setOnClickListener(this);
        textViewLanguageText.setOnClickListener(this);
        textViewRevers.setOnClickListener(this);
        textViewLanguageTranslation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        Log.d(LOG_TAG, "Start onClick");
        switch (v.getId()) {
            case R.id.button:

                String textToYandex = getEditText(editText);
//                Log.d(LOG_TAG, "textToYandex = " + textToYandex);

                createMapJson(textToYandex, "languageDetection", null);

                responseLanguageDetection();

                String lang = setLang();
                Log.d(LOG_TAG, "lang = " + lang);

                createMapJson(textToYandex, "translator", lang);

                responseTranslator();

                break;
            case R.id.text_view_revers:
                reversTextViewLanguageSelect();
                break;
            case R.id.text_view_language_text:
                responseLanguageDictionare();
                prepareBundleForDialogSelectLanguage(textViewLanguageText);

                dialogFragmentSelectLanguageText  = new DialogLanguageSelect();
                dialogFragmentSelectLanguageText.
                        setArguments(prepareBundleForDialogSelectLanguage(textViewLanguageText));
                dialogFragmentSelectLanguageText.show(getFragmentManager(), "dialog1");

                break;
            case R.id.text_view_language_translator:
                responseLanguageDictionare();
                prepareBundleForDialogSelectLanguage(textViewLanguageTranslation);

                dialogFragmentSelectTranslator = new DialogLanguageSelect();
                dialogFragmentSelectTranslator.
                        setArguments(prepareBundleForDialogSelectLanguage(textViewLanguageTranslation));
                dialogFragmentSelectTranslator.show(getFragmentManager(), "dialog2");

                break;
        }
    }

    private String getEditText(EditText editText){
        String text = editText.getText().toString();
        if (text.length() == 0) text = "";
        return text;
    }

    private String setLang(){
        String languageText = getCodeLanguage(textViewLanguageText.getText().toString());
        String languageTranslator = getCodeLanguage(textViewLanguageTranslation.getText().toString());
        String lang = languageText + "-" + languageTranslator;

        return lang;
    }

    private void reversTextViewLanguageSelect(){
        String stringTextViewLanguageText = textViewLanguageText.getText().toString();
        String stringTextViewLanguageTranslation = textViewLanguageTranslation.getText().toString();
        textViewLanguageText.setText(stringTextViewLanguageTranslation);
        textViewLanguageTranslation.setText(stringTextViewLanguageText);
    }

    private String getCodeLanguage(String language){
        for (Language item : listLanguage){
            String l = item.getLanguage();
            if (l.equals(language)){
                return item.getCodeLanguage();
            }
        }
        return "";//TODO написать обработку ошибки
    }

    private Bundle prepareBundleForDialogSelectLanguage(TextView textView){
        String[] language = convertArrayList(listLanguage);
        Bundle args = new Bundle();
        args.putStringArray("language", language);
        Log.d(LOG_TAG, "languages = " + language.length);
        args.putString("languageSelect", textView.getText().toString());
        args.putInt("idTextView", textView.getId());

        return args;
    }

    private String[] convertArrayList (ArrayList<Language> list) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (Language item : list){
            String newItem = item.getLanguage();
            stringArrayList.add(newItem);
        }
        return stringArrayList.toArray(new String[stringArrayList.size()]);
    }
}
