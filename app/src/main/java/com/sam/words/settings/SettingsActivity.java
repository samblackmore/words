package com.sam.words.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sam.words.R;
import com.sam.words.components.WordsView;
import com.sam.words.main.MainActivity;
import com.sam.words.models.Chapter;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    WordsView wordsView;
    static Chapter chapter = new Chapter("Chapter", "Hello there this is some test text");
    static List<Chapter> chapters = new ArrayList<>();

    static {
        chapters.add(chapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        setupButton(R.id.btn_increase_font_size, R.color.white, R.color.black);
        setupButton(R.id.btn_decrease_font_size, R.color.white, R.color.black);
        setupButton(R.id.btn_start, R.color.colorAccent, R.color.white);

        wordsView = (WordsView) findViewById(R.id.words_view);
        //wordsView.setChapters(chapters);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:

                SharedPreferences sharedPrefs = getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt(getString(R.string.saved_text_size), wordsView.getTextSize());
                editor.apply();

                Toast.makeText(SettingsActivity.this,
                        "Saved text size: " + wordsView.getTextSize(),
                        Toast.LENGTH_LONG)
                        .show();

                Intent intent = new Intent(this, MainActivity.class);
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

    private void setupButton(int buttonId, int colorId, int textColorId) {
        Button button = (Button) findViewById(buttonId);
        int color = getResources().getColor(colorId);
        int textColor = getResources().getColor(textColorId);
        button.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(textColor);
        button.setOnClickListener(this);
        button.invalidate();
    }
}
