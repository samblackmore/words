package com.sam.story.story;

import com.google.firebase.auth.FirebaseUser;

/**
 * An interface for fragments created in a {@link com.sam.story.utils.GoogleSignInActivity}
 */

public interface GoogleSignInFragment {
    void onSignInSignOut(FirebaseUser firebaseUser);
    void showSignInLoading(boolean show);
}
