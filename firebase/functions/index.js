'use strict';

const functions = require('firebase-functions');

// Listen for vote finish becoming true then create next vote

exports.finishUpdated = functions.database.ref('/stories/{storyId}/votes/{voteId}/finished')
.onUpdate(event => {

  var promises = [];

  const voteId = parseInt(event.params.voteId);
  const voteRef = event.data.ref.parent;
  const storyRef = voteRef.parent.parent;
  const finished = event.data.val();

  if (finished) {

    const chooseWinnerPromise = voteRef.child('posts').once('value').then(snapshot => {

      var winnerPromises = [];

      var highestScore = -1;
      var winningPostId = -1;

      snapshot.forEach(function(childSnapshot) {
        const postId = parseInt(childSnapshot.key)
        const postScore = parseInt(childSnapshot.child('voteCount').val())
        const postMessage = childSnapshot.child('message').val()

        if (postScore > highestScore) {
          highestScore = postScore
          winningPostId = postId
        }
      })

      const setWinnerPromise = voteRef.child('winner').set(winningPostId);
      const addToStoryPromise = storyRef.child('chapters').limitToLast(1).once('value').then(chaptersSnapshot => {

        var latestChapterId = -1

        chaptersSnapshot.forEach(function(chapterSnapshot) {
          latestChapterId = chapterSnapshot.key
        })

        return storyRef.child('chapters').child(latestChapterId).child('content').TRANSACTION
      })
    })

    const newVotePromise = voteRef.parent.child(voteId + 1).set({
      finished: false,
      timeCreated: new Date().getTime()
    });

    promises.push(newVotePromise);
    promises.push(chooseWinnerPromise);

    return Promise.all(promises)
  }
});