package com.sam.words;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    WordsView wordsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_increase_font_size).setOnClickListener(this);
        findViewById(R.id.btn_start).setOnClickListener(this);
        wordsView = (WordsView) findViewById(R.id.words_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                Intent intent = new Intent(this, TabActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_increase_font_size:
                wordsView.increaseTextSize(10);
                wordsView.invalidate();
                break;
        }
    }
}
