package com.sam.words.story;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sam.words.R;
import com.sam.words.components.Page;
import com.sam.words.components.WordsView;
import com.sam.words.main.CardAdapter;
import com.sam.words.models.Story;

import java.util.ArrayList;
import java.util.List;


public class StoryActivity extends AppCompatActivity implements View.OnClickListener{

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("stories");

    private WordsView rootWordsView;
    private StoryAdapter mStoryAdapter;
    private List<Page> pages = new ArrayList<>();
    private Story story;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        rootWordsView = (WordsView) findViewById(R.id.words_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStoryAdapter = new StoryAdapter(getSupportFragmentManager(), pages);

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(mStoryAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        String storyId = getIntent().getStringExtra(CardAdapter.EXTRA_STORY);

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

    /**
     * Callback for {@link StoryListener} once current story retrieved from Firebase
     * @param story
     */
    public void gotStory(Story story) {
        this.story = story;
        pages = rootWordsView.calculatePages(story.getChapters());

        mStoryAdapter = new StoryAdapter(getSupportFragmentManager(), pages);

        ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(mStoryAdapter);
        loading.setVisibility(View.GONE);

        //mStoryAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Got story", Toast.LENGTH_SHORT).show();
    }

    public List<Page> getPages() {
        return pages;
    }

    public Story getStory() {
        return story;
    }

    @Override
    public void onClick(View v) {
        viewPager.setCurrentItem(mStoryAdapter.getCount() - 1);
    }
}
