package com.sam.words.story;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.sam.words.R;
import com.sam.words.components.Page;
import com.sam.words.components.WordsView;
import com.sam.words.main.CardAdapter;
import com.sam.words.models.Chapter;
import com.sam.words.models.Post;
import com.sam.words.models.Story;
import com.sam.words.utils.GoogleSignInActivity;

import java.util.ArrayList;
import java.util.List;


public class StoryActivity extends GoogleSignInActivity implements View.OnClickListener{

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private WordsView rootWordsView;
    private StoryAdapter mStoryAdapter;
    private List<Page> pages = new ArrayList<>();
    private Story story;
    private Post latestPost;
    private int postCount = 0;
    private ViewPager viewPager;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        rootWordsView = (WordsView) findViewById(R.id.words_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStoryAdapter = new StoryAdapter(getSupportFragmentManager(), pages);

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(mStoryAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        String storyId = getIntent().getStringExtra(CardAdapter.EXTRA_STORY);

        database.getReference("stories").child(storyId)
                .addValueEventListener(new StoryListener(this));

        database.getReference("posts").child(storyId)
                .addValueEventListener(new PostsListener(this));
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
     */
    public void gotPostsByChapter(List<List<Post>> postsByChapter) {

        ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
        loading.setVisibility(View.GONE);

        List<Post> latestChapter = postsByChapter.get(postsByChapter.size() - 1);
        latestPost = latestChapter.get(latestChapter.size() - 1);

        int newPostCount = 0;
        for (List<Post> chapter : postsByChapter)
            newPostCount += chapter.size();

        if (newPostCount != postCount) {

            if (story != null) {
                List<String> chapterTitles = new ArrayList<>();
                for (Chapter chapter : story.getChapters())
                    if (chapter.getTitle() != null)
                        chapterTitles.add(chapter.getTitle());
                rootWordsView.setChapterTitles(chapterTitles);
            }

            pages = rootWordsView.calculatePages(postsByChapter);

            if (mStoryAdapter != null) {
                mStoryAdapter.update(pages);
                mStoryAdapter.notifyDataSetChanged();
            }

            postCount = newPostCount;
        }
    }

    public List<Page> getPages() {
        return pages;
    }

    public void gotStory(Story story) {
        this.story = story;
        if (story.isFinished())
            showFab(false);
        mStoryAdapter.notifyDataSetChanged();
    }

    public Story getStory() {
        return story;
    }

    public Post getLatestPost() {
        return latestPost;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.fab:
                viewPager.setCurrentItem(mStoryAdapter.getCount() - 1);
                break;
        }
    }

    public void goToPage(int num) {
        viewPager.setCurrentItem(num);
    }

    public void showFab(boolean show) {
        fab.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
