package com.sam.story.main;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.story.R;
import com.sam.story.components.SimpleDialog;
import com.sam.story.main.newstory.NewStoryFragment;
import com.sam.story.settings.SettingsActivity;
import com.sam.story.utils.DownloadImageTask;
import com.sam.story.utils.GoogleSignInActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends GoogleSignInActivity {

    private FirebaseAuth mAuth;
    private TabAdapter mTabAdapter;
    private FloatingActionButton fab;
    private ViewPager viewPager;
    private boolean reachedStoryLimit = false;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private CircleImageView profilePic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        initLayout();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.add_story:
                if (!reachedStoryLimit) {
                    DialogFragment fragment = new NewStoryFragment();
                    fragment.show(getFragmentManager(), "newstory");
                } else {
                    SimpleDialog dialog = SimpleDialog.newInstance("Sorry! You can only have 10 unfinished stories active at once. Please let one of your stories reach its end before starting a new one or, to finish a story at any time, open the story and tap its title 5 times.", R.string.ok);
                    dialog.show(getSupportFragmentManager(), "story-limit-error");
                }

                break;
        }
    }

    public void showAddStoryButton(boolean show) {
        fab.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void gotActiveStoryCount(int count) {
        reachedStoryLimit = count >= 10;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab, menu);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        MenuItem profile = menu.findItem(R.id.action_profile);
        MenuItem signOut = menu.findItem(R.id.action_sign_out);

        if (currentUser == null) {
            signOut.setVisible(false);
            profile.setVisible(false);
            profilePic.setVisibility(View.GONE);
        } else {
            profile.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_launcher));
            profile.setEnabled(false);
            profile.setTitle(currentUser.getDisplayName());
            profilePic.setVisibility(View.VISIBLE);
            getProfilePic();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_sign_out:
                mAuth.signOut();
                Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
                onSignInSignOut();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSignInSignOut() {
        // Hacky! Should use notifyDataSetChanged on the adapter
        viewPager.setAdapter(mTabAdapter);
        invalidateOptionsMenu();
    }
    
    private void initLayout() {
        setContentView(R.layout.activity_browse);

        fab = (FloatingActionButton) findViewById(R.id.add_story);
        fab.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTabAdapter = new TabAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(mTabAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        profilePic = (CircleImageView) findViewById(R.id.profile_pic);
        getProfilePic();
    }

    private void getProfilePic() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            profilePic.setVisibility(View.VISIBLE);
            database.getReference("users").child(user.getUid()).child("pic").addListenerForSingleValueEvent(new ProfilePicGetter());
        }
    }

    class ProfilePicGetter implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String picURL = dataSnapshot.getValue(String.class);
            if (picURL != null)
                new DownloadImageTask(profilePic).execute(picURL);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
