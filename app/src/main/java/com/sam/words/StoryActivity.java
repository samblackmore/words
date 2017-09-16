package com.sam.words;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import static com.sam.words.BrowseListAdapter.EXTRA_STORY;

public class StoryActivity extends AppCompatActivity {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("stories");

    private Story mStory;
    private StoryPageAdapter mStoryPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String storyId = intent.getStringExtra(EXTRA_STORY);

        mStoryPageAdapter = new StoryPageAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(mStoryPageAdapter);

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
                StoryPageFragment storyPageFragment = (StoryPageFragment) fragment;
                if (storyPageFragment != null) {
                    storyPageFragment.updateStory(story);
                }
            }
        }
    }

    public void gotPages(List<Page> pages) {
        if (mStoryPageAdapter.getCount() != pages.size())
            mStoryPageAdapter.setPages(pages.size());
    }

    public Story getStory() {
        return mStory;
    }

}
