package com.example.alex.yandextranslator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView;
    private EditText editText;
    private Button button;

    private final String LOG_TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        buttonBehavior();
    }

    private void buttonBehavior() {
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(LOG_TAG, "Start onClick");
        switch (v.getId()) {
            case R.id.button:
                String textToYandex = getEditText(editText);
                Log.d(LOG_TAG, "textToYandex = " + textToYandex);

                break;
        }
    }

    private void initView() {
        editText = (EditText)findViewById(R.id.edit_text);
        button = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.text_view);
    }

    private String getEditText(EditText editText){
        String text = editText.getText().toString();
        if (text.length() == 0) text = "";
        return text;
    }

}
