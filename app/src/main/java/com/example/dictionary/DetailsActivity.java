package com.example.dictionary;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {
    String KEY_1 = "ENCRYPTION";
    TextView wordTxt;
    TextView meaningTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        wordTxt = findViewById(R.id.wordTxt);
        meaningTxt = findViewById(R.id.meaningTxt);


        String stringExtra = getIntent().getStringExtra(KEY_1);
        String stringArray [] = stringExtra.split(":");

        wordTxt.setText(stringArray[0]);
        meaningTxt.setText(stringArray[1]);

    }
}
