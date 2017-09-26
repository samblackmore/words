package com.sam.words.story;

import com.google.firebase.auth.FirebaseUser;

/**
 * An interface for fragments created in a {@link com.sam.words.utils.GoogleSignInActivity}
 */

public interface GoogleSignInFragment {
    void updateUI(FirebaseUser firebaseUser);
    void showLoading(boolean show);
}
