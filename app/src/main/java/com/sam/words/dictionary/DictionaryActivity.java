package com.sam.words.dictionary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sam.words.R;
import com.sam.words.models.Word;

import java.util.List;

public class DictionaryActivity extends AppCompatActivity {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("words");
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.words_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Query query = ref.child("pending").orderByChild("score").limitToLast(100);
        query.addValueEventListener(new DictionaryListener(this));
    }

    public void gotWords(List<Word> words) {
        RecyclerView.Adapter mAdapter = new DictionaryAdapter(words);
        mRecyclerView.setAdapter(mAdapter);
    }
}
