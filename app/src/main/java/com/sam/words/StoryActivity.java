package com.sam.words;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.sam.words.StoryListAdapter.EXTRA_STORY;

public class StoryActivity extends AppCompatActivity {

    private String mStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mStory = intent.getStringExtra(EXTRA_STORY);

        final StoryPageAdapter storyPageAdapter = new StoryPageAdapter(getSupportFragmentManager(), mStory);

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(storyPageAdapter);

        final EditText input = (EditText) findViewById(R.id.input);
        Button submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStory += " " + input.getText();
                storyPageAdapter.setStory(mStory);
            }
        });
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
