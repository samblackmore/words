package com.sam.words;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

class SignInListener implements OnCompleteListener<AuthResult> {

    private BrowseTabFragment fragment;
    private FirebaseAuth auth;

    SignInListener(BrowseTabFragment fragment) {
        this.fragment = fragment;
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            FirebaseUser user = auth.getCurrentUser();
            fragment.toast("Welcome!");
            fragment.updateUI(user);
        } else {
            fragment.toast("Authentication failed");
            fragment.updateUI(null);
        }
    }
}
