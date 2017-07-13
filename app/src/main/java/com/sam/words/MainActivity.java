package com.sam.words;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WordsView wordsView = (WordsView) findViewById(R.id.words_view);
        View embiggen = findViewById(R.id.btn_increase_font_size);

        embiggen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                wordsView.increaseTextSize(10);
                wordsView.invalidate();
            }
        });
    }
}
