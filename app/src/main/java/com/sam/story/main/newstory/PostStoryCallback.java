package com.sam.story.main.newstory;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.sam.story.main.MainActivity;
import com.sam.story.models.Chapter;
import com.sam.story.models.Poll;
import com.sam.story.models.Post;
import com.sam.story.models.Story;

import java.util.HashMap;
import java.util.Map;

/**
 * Callback after checking bad words
 */

public class PostStoryCallback implements BadWordsCallback {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private NewStoryFragment newStoryFragment;
    private String title;
    private String author;
    private String content;

    public PostStoryCallback(NewStoryFragment newStoryFragment, String title, String author, String content) {
        this.newStoryFragment = newStoryFragment;
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public void badWordsFound(BadWordsDialog badWordsDialog) {
        badWordsDialog.show(((MainActivity) newStoryFragment.getActivity()).getSupportFragmentManager(), "bad-words");
    }

    public void databaseError(String message) {
        Toast.makeText(newStoryFragment.getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    // Post story
    public void allWordsClean() {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(newStoryFragment.getActivity(), "Not signed in!", Toast.LENGTH_SHORT).show();
            return;
        }

        int chapter = 0;
        int pollRound = 0;
        String chapterId = String.valueOf(chapter);
        String newPollId = String.valueOf(pollRound);

        String userId = user.getUid();
        String newStoryId = database.getReference("stories").push().getKey();
        String newPostId = database.getReference("posts").child(newStoryId).child(chapterId).push().getKey();

        Story newStory = new Story(title, userId, author);
        Post newPost = new Post(newStoryId, userId, user.getDisplayName(), content);
        Poll newPoll = new Poll(pollRound);
        Chapter firstChapter = new Chapter(chapter, null);
        newStory.addChapter(firstChapter);
        newStory.addLike(userId);
        newStory.addContributor(userId);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/stories/" + newStoryId, newStory);
        childUpdates.put("/posts/" + newStoryId + "/" + chapterId + "/" + newPostId, newPost);
        childUpdates.put("/poll/" + newStoryId + "/" + chapterId + "/" + newPollId, newPoll);
        childUpdates.put("/users/" + userId + "/activity/" + newStoryId + "/postCount", 1);
        childUpdates.put("/users/" + userId + "/activity/" + newStoryId + "/chapterCount", 1);
        childUpdates.put("/users/" + userId + "/activity/" + newStoryId + "/contributorsCount", 1);
        childUpdates.put("/users/" + userId + "/stories/" + newStoryId, true);
        childUpdates.put("/users/" + userId + "/posts/" + newPostId, true);

        database.getReference().updateChildren(childUpdates);

        newStoryFragment.dismiss();
    }
}
