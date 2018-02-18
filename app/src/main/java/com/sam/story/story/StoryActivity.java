package com.sam.story.story;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.story.R;
import com.sam.story.components.Page;
import com.sam.story.components.WordsView;
import com.sam.story.main.CardAdapter;
import com.sam.story.models.Chapter;
import com.sam.story.models.Post;
import com.sam.story.models.Story;
import com.sam.story.utils.GoogleSignInActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StoryActivity extends GoogleSignInActivity implements View.OnClickListener{

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private WordsView rootWordsView;
    private StoryAdapter mStoryAdapter;
    private List<Page> pages = new ArrayList<>();
    private Story story;
    private Post latestPost;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private String storyId;
    private List<List<Post>> postsByChapter;
    private ValueEventListener postsListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        rootWordsView = (WordsView) findViewById(R.id.words_view);
        rootWordsView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                tryCalcPages();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStoryAdapter = new StoryAdapter(getSupportFragmentManager(), pages);

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(mStoryAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        storyId = getIntent().getStringExtra(CardAdapter.EXTRA_STORY);

        database.getReference("stories").child(storyId)
                .addValueEventListener(new StoryListener(this));
    }

    public void gotStory(Story story) {
        this.story = story;

        ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
        loading.setVisibility(View.GONE);

        if (!story.isFinished())
            showFab(true);

        DatabaseReference postsRef = database.getReference("winners").child(storyId);

        if (postsListener != null)
            postsRef.removeEventListener(postsListener);

        postsListener = postsRef.addValueEventListener(new PostsListener(this, story.getChapterSize()));

        mStoryAdapter.notifyDataSetChanged();
    }

    /**
     * Callback for {@link StoryListener} once current story retrieved from Firebase
     */
    public void gotPostsByChapter(List<List<Post>> postsByChapter) {

        this.postsByChapter = postsByChapter;

        List<Post> latestChapter = postsByChapter.get(postsByChapter.size() - 1);
        latestPost = latestChapter.get(latestChapter.size() - 1);

        tryCalcPages();
    }

    private void tryCalcPages() {

        if (story != null && postsByChapter != null && rootWordsView.getWidth() > 0) {

            List<String> chapterTitles = new ArrayList<>();
            for (Chapter chapter : story.getChapters())
                if (chapter.getTitle() != null)
                    chapterTitles.add(chapter.getTitle());

            rootWordsView.setChapterTitles(chapterTitles);
            pages = rootWordsView.calculatePages(postsByChapter);

            if (mStoryAdapter != null) {
                mStoryAdapter.update(pages);
                mStoryAdapter.notifyDataSetChanged();
            }
        }
    }

    public List<Page> getPages() {
        return pages;
    }

    @Override
    public void onStop() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            final String userId = user.getUid();
            database.getReference("stories").child(storyId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Story story = snapshot.getValue(Story.class);
                    if (story != null) {
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/users/" + userId + "/activity/" + storyId + "/postCount", story.getPostCount());
                        childUpdates.put("/users/" + userId + "/activity/" + storyId + "/chapterCount", story.getChapterCount());
                        childUpdates.put("/users/" + userId + "/activity/" + storyId + "/contributorsCount", story.getContributorsCount());
                        database.getReference().updateChildren(childUpdates);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        super.onStop();
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
                showFab(false);
                break;
        }
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

    public void goToPage(int num) {
        viewPager.setCurrentItem(num);
    }

    public void showFab(boolean show) {
        fab.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
