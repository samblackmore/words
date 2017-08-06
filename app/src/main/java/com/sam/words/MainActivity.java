package com.sam.words;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    WordsView wordsView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private void setupButton(int buttonId, int colorId, int textColorId) {
        Button button = (Button) findViewById(buttonId);
        int color = getResources().getColor(colorId);
        int textColor = getResources().getColor(textColorId);
        button.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(textColor);
        button.setOnClickListener(this);
        button.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupButton(R.id.btn_increase_font_size, R.color.white, R.color.black);
        setupButton(R.id.btn_decrease_font_size, R.color.white, R.color.black);
        setupButton(R.id.btn_start, R.color.colorAccent, R.color.white);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        signInAnonymously();

        wordsView = (WordsView) findViewById(R.id.words_view);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(MainActivity.this, "Authentication success!", Toast.LENGTH_LONG).show();
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                    //updateUI(null);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                Intent intent = new Intent(this, BrowseActivity.class);
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
}
