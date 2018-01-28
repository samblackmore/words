package com.sam.words.main;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sam.words.R;
import com.sam.words.main.newstory.NewStoryFragment;
import com.sam.words.settings.SettingsActivity;
import com.sam.words.utils.GoogleSignInActivity;

public class MainActivity extends GoogleSignInActivity {

    private FirebaseAuth mAuth;
    private TabAdapter mTabAdapter;
    private FloatingActionButton fab;
    private ViewPager viewPager;

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
                DialogFragment fragment = new NewStoryFragment();
                fragment.show(getFragmentManager(), "newstory");
                break;
        }
    }

    public void showAddStoryButton(boolean show) {
        fab.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab, menu);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            MenuItem signOut = menu.findItem(R.id.action_sign_out);
            signOut.setVisible(false);
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
    }
}
