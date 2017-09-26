'use strict';

const functions = require('firebase-functions');

// Listen for vote finish becoming true then create next vote

exports.finishUpdated = functions.database.ref('/poll/{storyId}/{chapterId}/{pollId}/finished')
.onUpdate(event => {

    console.log('got update event')

    var promises = [];

    const pollId = event.params.pollId;
    const pollRef = event.data.ref.parent;
    const chapterId = event.params.chapterId
    const storyId = event.params.storyId;
    const storyRef = event.data.ref.root.child('stories').child(storyId)
    const postsRef = event.data.ref.root.child('posts')
    const finished = event.data.val();

    if (finished) {

        console.log('round finished, getting posts')

        return pollRef.once('value').then(snapshot => {

            console.log('got poll')

            var highestScore = -1;
            var winningPost;

            snapshot.child('posts').forEach(function(postSnapshot) {

                const post = postSnapshot.val()

                if (post.voteCount > highestScore) {
                    highestScore = post.voteCount
                    winningPost = post
                }
            })

            const rounds = parseInt(snapshot.child('rounds').val())

            console.log('poll', pollId, 'out of', rounds, 'winner', winningPost)

            if (pollId > rounds) {
                const newChapter = parseInt(chapterId) + 1
                console.log('winning title', winningPost.message)
                storyRef.child('chapters').child(chapterId).child('title').set(winningPost.message)
                storyRef.child('chapters').child(newChapter).set({chapter: newChapter})
            } else {
                const newRound = parseInt(pollId) + 1
                console.log('new poll')
                //return pollRef.parent.child(newRound).set(newPoll)
                pollRef.parent.child(newRound).set({
                    round: newRound,
                    rounds: rounds,
                    finished: false,
                    timeCreated: new Date().getTime()
                })
                postsRef.child(storyId).child(chapterId).push().set(winningPost)
            }
        })
    }
});