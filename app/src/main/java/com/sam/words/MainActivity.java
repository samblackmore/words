package com.sam.words;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    WordsView wordsView;

    private void setupButton(int buttonId, int colorId, int textColorId) {
        Button button = (Button) findViewById(buttonId);
        int color = getResources().getColor(colorId);
        int textColor = getResources().getColor(textColorId);
        button.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(textColor);
        button.setOnClickListener(this);
        button.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupButton(R.id.btn_increase_font_size, R.color.white, R.color.black);
        setupButton(R.id.btn_decrease_font_size, R.color.white, R.color.black);
        setupButton(R.id.btn_start, R.color.colorAccent, R.color.white);

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
            case R.id.btn_decrease_font_size:
                wordsView.decreaseTextSize(10);
                wordsView.invalidate();
                break;
        }
    }
}
