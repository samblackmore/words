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

        return pollRef.once('value').then(snapshot => {

            var highestScore = -1;
            var winningPost;

            snapshot.child('posts').forEach(function(postSnapshot) {

                const post = postSnapshot.val()

                if (post.voteCount > highestScore) {
                    highestScore = post.voteCount
                    winningPost = post
                }
            })

            const round = parseInt(pollId)
            const rounds = parseInt(snapshot.child('rounds').val())

            if (round < rounds) {
                const newRound = parseInt(pollId) + 1

                // Push winning post
                postsRef.child(storyId).child(chapterId).push().set(winningPost)

                // Start new round
                pollRef.parent.child(newRound).set({
                    round: newRound,
                    rounds: rounds,
                    finished: false,
                    timeCreated: new Date().getTime()
                })
            } else {
                const newChapter = parseInt(chapterId) + 1

                // Assign winning title to story chapter
                storyRef.child('chapters').child(chapterId).child('title').set(winningPost.message)

                // Add the next chapter (not sure if needed)
                storyRef.child('chapters').child(newChapter).set({chapter: newChapter})

                pollRef.root.child('poll').child(storyId).child(newChapter).child(0).set({
                    round: 0,
                    rounds: rounds,
                    finished: false,
                    timeCreated: new Date().getTime()
                })
            }
        })
    }
});