package com.sam.words.utils;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sam.words.models.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AdminActivity extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("words");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AssetManager am = getAssets();
        InputStream stream = null;
        try {
            stream = am.open("words3000.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert stream != null;

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = r.readLine()) != null) {
                ref.child(line.replace(".", ",")).setValue(new Word());
            }
        } catch (IOException e) {
            //
        }
    }
}
