package com.sam.words;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import static com.sam.words.StoryListAdapter.EXTRA_STORY;

public class StoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String story = intent.getStringExtra(EXTRA_STORY);

        StoryPageAdapter storyPageAdapter = new StoryPageAdapter(getSupportFragmentManager(), story);

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(storyPageAdapter);

        EditText input = (EditText) findViewById(R.id.input);
        Button submit = (Button) findViewById(R.id.submit);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_swipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
