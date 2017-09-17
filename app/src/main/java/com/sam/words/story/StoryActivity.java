package com.sam.words.story;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sam.words.main.CardAdapter;
import com.sam.words.components.Page;
import com.sam.words.R;
import com.sam.words.models.Story;

import java.util.List;

public class StoryActivity extends AppCompatActivity {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("stories");

    private Story mStory;
    private StoryAdapter mStoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String storyId = intent.getStringExtra(CardAdapter.EXTRA_STORY);

        mStoryAdapter = new StoryAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(mStoryAdapter);

        Query query = ref.child(storyId);
        query.addValueEventListener(new StoryListener(this));
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

    public void setStory(Story story) {
        mStory = story;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        if (fragments != null) {
            for (Fragment fragment : fragments) {
                StoryFragment storyFragment = (StoryFragment) fragment;
                if (storyFragment != null) {
                    storyFragment.updateStory(story);
                }
            }
        }
    }

    public void gotPages(List<Page> pages) {
        if (mStoryAdapter.getCount() != pages.size())
            mStoryAdapter.setPages(pages.size());
    }

    public Story getStory() {
        return mStory;
    }

}
