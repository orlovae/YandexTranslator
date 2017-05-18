package com.example.alex.yandextranslator.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.example.alex.yandextranslator.App;
import com.example.alex.yandextranslator.DialogLanguageSelect;
import com.example.alex.yandextranslator.R;
import com.example.alex.yandextranslator.model.language.Language;
import com.example.alex.yandextranslator.model.response.LanguageDictionare;
import com.example.alex.yandextranslator.model.response.Translator;
import com.example.alex.yandextranslator.model.response.dictionaryentry.Def;
import com.example.alex.yandextranslator.model.response.dictionaryentry.DictionaryEntry;
import com.example.alex.yandextranslator.model.response.dictionaryentry.Ex;
import com.example.alex.yandextranslator.model.response.dictionaryentry.Mean;
import com.example.alex.yandextranslator.model.response.dictionaryentry.Syn;
import com.example.alex.yandextranslator.model.response.dictionaryentry.Tr;
import com.example.alex.yandextranslator.model.response.dictionaryentry.Tr_;
import com.example.alex.yandextranslator.rest.ApiDictionare;
import com.example.alex.yandextranslator.rest.ApiDictionaryEntry;
import com.example.alex.yandextranslator.rest.ApiTranslator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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

    private final int KEY_API_DICTIONARY_TEXT = 100;
    private final int KEY_API_DICTIONARY_TS = 200;
    private final int KEY_API_DICTIONARY_PART_OF_SPEECH = 300;
    private final int KEY_API_DICTIONARY_TRANSLATE = 400;
    private final int KEY_API_DICTIONARY_MEAN = 500;
    private final int KEY_API_DICTIONARY_EX = 600;

    private final int MARGIN_LEFT_MEAN = 30;
    private final int MARGIN_LEFT_EX = 50;

    private TextView textViewTranslate;
    private TextView textViewLanguageText, textViewRevers, textViewLanguageTranslation;
    private EditText editText;
    private Button button;
    private LinearLayout linearLayout;

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
                responseTranslatorDictionary(app.createMapJson(textToYandex, "dictionaryEntry"));
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
        linearLayout = (LinearLayout)view.findViewById(R.id.linear_layout_translate);
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

    public void responseTranslatorDictionary(Map<String, String> mapJson) {
        Log.d(LOG_TAG, "Start responseTranslator");

        ApiDictionaryEntry apiDictionaryEntry = app.getApiDictionaryEntry();

        Call<DictionaryEntry> call = apiDictionaryEntry.dictionaryEntry(mapJson);

        call.enqueue(new Callback<DictionaryEntry>() {
            @Override
            public void onResponse(Call<DictionaryEntry> call, Response<DictionaryEntry> response) {
                try {
                    String responseTranslator;
                    if (response.isSuccessful()){
//                        responseTranslator = response.body().toString();
//
                        List<Def> defList = response.body().getDef();
//                        Log.d(LOG_TAG, "def.size = " + defList.size());
//
//                        if (defList.size() != 0) {
//
//                            int countDef = 0;
//
//                            for (Def item : defList
//                                    ) {
//
//                                Log.d(LOG_TAG, "Text = " + item.getText());
//
//                                String ts = String.format(getActivity().getString(R.string.
//                                        transcription), item.getTs());
//                                Log.d(LOG_TAG, "Ts = " + item.getTs());
//
//                                if (countDef <= 0) { /*нужна транскрипция, только 1 элемента, иначе
//                                транскрипция выводится для всех частей речи - сущ., гл., прилаг, и
//                                т.п.*/
//                                    addTextView(KEY_API_DICTIONARY_TS, 0, item.getText(), ts, null);
//                                }
//                                countDef++;
//
//
//                                item.getPos();
//                                Log.d(LOG_TAG, "Pos = " + item.getPos());
//
//                                addTextView(KEY_API_DICTIONARY_PART_OF_SPEECH, 0, item.getPos(),
//                                        null, null);
//
//                                List<Tr> trList = item.getTr();
//                                Log.d(LOG_TAG, "trList.size = " + trList.size());
//
//                                int countTrList = 0;
//                                for (Tr itemTr : trList
//                                        ) {
////                                    itemTr.getPos();/*какая часть речи, в данном случае не нужна,
////                                      так как идёт дублирование*/
////                                    Log.d(LOG_TAG, "Tr Pos = " + itemTr.getPos());
//
//                                    Log.d(LOG_TAG, "Tr Text = " + itemTr.getText());
//
//                                    Log.d(LOG_TAG, "gen Text = " + itemTr.getGen());
//
//                                    TreeMap<String, String> synListTextAndGen = new TreeMap<>();
//
//                                    synListTextAndGen.put(itemTr.getText(), itemTr.getGen());
//
//                                    List<Syn> synList = itemTr.getSyn();
//
//                                    countTrList++;
//
//                                    if (synList != null) {
//                                        Log.d(LOG_TAG, "synList.size = " + synList.size());
//
//                                        for (Syn itemSyn : synList
//                                                ) {
//                                            synListTextAndGen.put(itemSyn.getText(),
//                                                    itemSyn.getGen());
//                                        }
//                                        addTextView(KEY_API_DICTIONARY_TRANSLATE, countTrList,
//                                                null, null, synListTextAndGen);
//                                    } else {
//                                        Log.d(LOG_TAG, "synList is null " + (synList == null));
//                                        addTextView(KEY_API_DICTIONARY_TRANSLATE, countTrList,
//                                                null, null, synListTextAndGen);
//                                    }
//
//                                    String massiveMean = "(";
//
//                                    List<Mean> meanList = itemTr.getMean();
//                                    if (meanList != null) {
//                                        Log.d(LOG_TAG, "meanList.size = " + meanList.size());
//
//                                        for (int countMean = 0; countMean < meanList.size();
//                                             countMean++) {
//                                            if (countMean == meanList.size() - 1) {
//                                                massiveMean = massiveMean + meanList.get(countMean)
//                                                        .getText() + ")";
//                                            } else {
//                                                massiveMean = massiveMean + meanList.get(countMean)
//                                                        .getText() + ", ";
//                                            }
//                                        }
//                                        addTextView(KEY_API_DICTIONARY_MEAN, 0, massiveMean,
//                                                null, null);
//
//                                    } else {
//                                        Log.d(LOG_TAG, "meanList is null " + (meanList == null));
//                                    }
//
//                                    List<Ex> exList = itemTr.getEx();
//                                    if (exList != null) {
//                                        Log.d(LOG_TAG, "exList.size = " + exList.size());
//
//                                        for (Ex itemEx : exList
//                                                ) {
//                                            String ex = itemEx.getText() + " " +
//                                                    getActivity().getString(R.string.tire) + " ";
//                                            Log.d(LOG_TAG, "Ex Text = " + itemEx.getText());
//
//                                            List<Tr_> tr_List = itemEx.getTr();
//                                            if (tr_List != null) {
//                                                Log.d(LOG_TAG, "tr_List.size = " + tr_List.size());
//
//                                                String tr_ = "";
//
//                                                for (Tr_ itemTr_ : tr_List
//                                                        ) {
//                                                    tr_ = tr_ + itemTr_.getText();
//                                                    Log.d(LOG_TAG, "tr_ Text = " + itemTr_.getText());
//                                                }
//
//                                                ex = ex + tr_;
//
//                                                addTextView(KEY_API_DICTIONARY_EX, 0, ex, null, null);
//
//                                            } else {
//                                                Log.d(LOG_TAG, "tr_List is null " + (tr_List == null));
//                                            }
//                                        }
//
//                                    } else {
//                                        Log.d(LOG_TAG, "exList it null " + (exList == null));
//                                    }
//                                }
//                            }
//                        } else {
//                            // приходит пустой ответ без перевода.
//                        }
                        app.addToDictionaryEntriesAllTable(defList);
                    } else {
                        responseTranslator = getString(R.string.error_invalid_responce);
                    }
//                    textViewTranslate.setText(responseTranslator);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "exeption onResponce " + e.toString());
                    //TODO написать обработку ошибок
                }
            }
            @Override
            public void onFailure(Call<DictionaryEntry> call, Throwable t) {
                t.printStackTrace();
                Log.d(LOG_TAG, "exeption onFailure " + t.toString());
                //TODO написать обработку ошибок
            }
        });
    }

    private void addTextView(int key, int count, String text, String transcription,
                             TreeMap<String, String> synListTextAndGen){
        Log.d(LOG_TAG, "Start addTextView");
        switch (key){
            case KEY_API_DICTIONARY_TS:
                Log.d(LOG_TAG, "Start addTextView|TS");

                LinearLayout linearLayoutText = new LinearLayout(getActivity());
                linearLayoutText.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutText.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));

                TextView tvText = new TextView(getActivity());
                tvText.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));

                if (Build.VERSION.SDK_INT < 23) {
                    tvText.setTextAppearance(getActivity(), R.style.Text);
                } else {
                    tvText.setTextAppearance(R.style.Text);
                }

                tvText.setText(text + " ");

                linearLayoutText.addView(tvText);

                TextView tvTs = new TextView(getActivity());
                tvTs.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));

                if (Build.VERSION.SDK_INT < 23) {
                    tvTs.setTextAppearance(getActivity(), R.style.Ts);
                } else {
                    tvTs.setTextAppearance(R.style.Ts);
                }

                tvTs.setText(transcription);

                linearLayoutText.addView(tvTs);
                linearLayout.addView(linearLayoutText);
                break;
            case KEY_API_DICTIONARY_PART_OF_SPEECH:
                Log.d(LOG_TAG, "Start addTextView|PART_OF_SPEECH");

                TextView tvPos = new TextView(getActivity());
                tvPos.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));

                if (Build.VERSION.SDK_INT < 23) {
                    tvPos.setTextAppearance(getActivity(), R.style.PartOfSpeech);
                } else {
                    tvPos.setTextAppearance(R.style.PartOfSpeech);
                }


                if (text.equals(getString(R.string.adjective_en))){
                    tvPos.setText(getString(R.string.adjective_ru));
                } else if (text.equals(getString(R.string.noun_en))){
                    tvPos.setText(getString(R.string.noun_ru));
                } else if (text.equals(getString(R.string.verb_en))){
                    tvPos.setText(getString(R.string.verb_ru));
                }
                linearLayout.addView(tvPos);
                break;

            case KEY_API_DICTIONARY_TRANSLATE:
                Log.d(LOG_TAG, "Start addTextView|TRANSLATE");

                LinearLayout linearLayoutTranslate = new LinearLayout(getActivity());
                linearLayoutTranslate.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutTranslate.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));

                TextView tvCountTranslate = new TextView(getActivity());
                tvCountTranslate.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));

                if (Build.VERSION.SDK_INT < 23) {
                    tvCountTranslate.setTextAppearance(getActivity(), R.style.TranslateCount);
                } else {
                    tvCountTranslate.setTextAppearance(R.style.TranslateCount);
                }
                tvCountTranslate.setText(count + "  "); /*два пробела нужны для формирования отступа
                после цифры*/

                linearLayoutTranslate.addView(tvCountTranslate);

                Iterator<Map.Entry<String,String>> iterator = synListTextAndGen.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();

                    TextView tvTranslate = new TextView(getActivity());
                    tvTranslate.setLayoutParams(new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));

                    if (Build.VERSION.SDK_INT < 23) {
                        tvTranslate.setTextAppearance(getActivity(), R.style.Translate);
                    } else {
                        tvTranslate.setTextAppearance(R.style.Translate);
                    }
                    tvTranslate.setText(entry.getKey());

                    TextView tvGen = new TextView(getActivity());
                    tvTranslate.setLayoutParams(new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));

                    if (Build.VERSION.SDK_INT < 23) {
                        tvGen.setTextAppearance(getActivity(), R.style.Gen);
                    } else {
                        tvGen.setTextAppearance(R.style.Gen);
                    }

                    if (entry.getValue() != null) {
                        tvGen.setText(" " + entry.getValue());
                    } else {
                        tvGen.setVisibility(View.INVISIBLE);
                    }

                    TextView tvComma = new TextView(getActivity());
                    tvComma.setLayoutParams(new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));

                    if (Build.VERSION.SDK_INT < 23) {
                        tvComma.setTextAppearance(getActivity(), R.style.Translate);
                    } else {
                        tvComma.setTextAppearance(R.style.Translate);
                    }

                    String comma = getActivity().getString(R.string.comma) + " ";

                    tvComma.setText(comma);

                    if (iterator.hasNext()) {
                        linearLayoutTranslate.addView(tvTranslate);
                        linearLayoutTranslate.addView(tvGen);
                        linearLayoutTranslate.addView(tvComma);

                    } else {
                        linearLayoutTranslate.addView(tvTranslate);
                        linearLayoutTranslate.addView(tvGen);
                    }
                }

                linearLayout.addView(linearLayoutTranslate);
                break;
            case KEY_API_DICTIONARY_MEAN:
                Log.d(LOG_TAG, "Start addTextView|MEAN");

                LayoutParams layoutParamsMean = new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT);
                layoutParamsMean.setMargins(MARGIN_LEFT_MEAN, 0, 0, 0);

                TextView tvMean = new TextView(getActivity());
                tvMean.setLayoutParams(layoutParamsMean);

                if (Build.VERSION.SDK_INT < 23) {
                    tvMean.setTextAppearance(getActivity(), R.style.Mean);
                } else {
                    tvMean.setTextAppearance(R.style.Mean);
                }

                tvMean.setText(text);

                linearLayout.addView(tvMean);
                break;
            case KEY_API_DICTIONARY_EX:
                Log.d(LOG_TAG, "Start addTextView|EX");

                LayoutParams layoutParamsEx = new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT);
                layoutParamsEx.setMargins(MARGIN_LEFT_EX, 0, 0, 0);

                TextView tvEx = new TextView(getActivity());
                tvEx.setLayoutParams(layoutParamsEx);

                if (Build.VERSION.SDK_INT < 23) {
                    tvEx.setTextAppearance(getActivity(), R.style.Ex);
                } else {
                    tvEx.setTextAppearance(R.style.Ex);
                }

                tvEx.setText(text);

                linearLayout.addView(tvEx);
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
