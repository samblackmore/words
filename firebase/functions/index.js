'use strict';

const functions = require('firebase-functions');

// Listen for vote finish becoming true then create next vote

exports.finishUpdated = functions.database.ref('/stories/{storyId}/votes/{voteId}/finished')
.onUpdate(event => {

  var promises = [];

  const voteId = parseInt(event.params.voteId);
  const voteRef = event.data.ref.parent;
  const finished = event.data.val();

  if (finished) {

    const chooseWinnerPromise = voteRef.child('posts').once('value').then(snapshot => {

      var highestScore = -1;
      var winningPostId = -1;

      snapshot.forEach(function(childSnapshot) {
        const postId = parseInt(childSnapshot.key)
        const postScore = parseInt(childSnapshot.child('voteCount').val())

        if (postScore > highestScore) {
          highestScore = postScore
          winningPostId = postId
        }
      })

      return voteRef.child('winner').set(winningPostId)
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