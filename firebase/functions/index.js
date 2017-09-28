'use strict';

const functions = require('firebase-functions');

// Listen for vote finish becoming true then create next vote

exports.finishUpdated = functions.database.ref('/poll/{storyId}/{chapterId}/{pollId}/finished')
.onUpdate(event => {

    const pollId = event.params.pollId;
    const pollRef = event.data.ref.parent;
    const chapterId = event.params.chapterId
    const storyId = event.params.storyId;
    const storyRef = event.data.ref.root.child('stories').child(storyId)
    const postsRef = event.data.ref.root.child('posts')
    const finished = event.data.val();

    if (finished) {

        return pollRef.once('value').then(pollSnapshot => {

            var highestScore = -1;
            var winningPost;

            pollSnapshot.child('posts').forEach(function(postSnapshot) {

                const post = postSnapshot.val()

                if (post.voteCount > highestScore) {
                    highestScore = post.voteCount
                    winningPost = post
                }
            })

            return storyRef.once('value').then(storySnapshot => {

                const round = parseInt(pollId)
                const rounds = parseInt(storySnapshot.child('chapterSize').val())

                if (round < rounds) {

                    // Poll not over

                    const newRound = round + 1

                    // Push winning post
                    const newPostRef = postsRef.child(storyId).child(chapterId).push();
                    const newPostId = newPostRef.key
                    newPostRef.set(winningPost)

                    // Update winning user's posts
                    postsRef.root.child('users').child(winningPost.userId).child('posts').child(newPostId).set(true)

                    // Increment postCount on story
                    storyRef.child('postCount').transaction(function (current_value) {
                      return (current_value || 0) + 1;
                    });

                    // Start new round
                    pollRef.parent.child(newRound).set({
                        round: newRound,
                        finished: false,
                        timeCreated: new Date().getTime()
                    })
                } else {

                    // Poll is over

                    const newChapter = parseInt(chapterId) + 1
                    const chapterLimit = parseInt(storySnapshot.child('chapterLimit').val())

                    if (newChapter == chapterLimit) {

                        // Story finished
                        storyRef.child('finished').set(true)

                    } else {
                        // Add the next chapter
                        storyRef.child('chapters').child(newChapter).set({chapter: newChapter})

                        // Increment chapterCount on story
                        storyRef.child('chapterCount').transaction(function (current_value) {
                          return (current_value || 0) + 1;
                        });

                        // Start first round of new chapter
                        pollRef.root.child('poll').child(storyId).child(newChapter).child(0).set({
                            round: 0,
                            finished: false,
                            timeCreated: new Date().getTime()
                        })
                    }

                    // Assign winning title to story chapter
                    storyRef.child('chapters').child(chapterId).child('title').set(winningPost.message)
                }
            })
        })
    }
});