package com.sam.words.story;

import com.google.firebase.auth.FirebaseUser;

/**
 * An interface for fragments created in a {@link com.sam.words.utils.GoogleSignInActivity}
 */

public interface GoogleSignInFragment {
    void onSignInSignOut(FirebaseUser firebaseUser);
    void showSignInLoading(boolean show);
}
