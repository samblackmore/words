'use strict';

const functions = require('firebase-functions');

// Listen for vote finish becoming true then create next vote

exports.finishUpdated = functions.database.ref('/poll/{storyId}/{chapterId}/{pollId}/finished')
.onUpdate(event => {

  var promises = [];

  const pollId = event.params.pollId;
  const chapterId = event.params.chapterId
  const storyId = event.params.storyId;
  const pollRef = event.data.ref.parent;
  const postsRef = event.data.ref.root.child('posts')
  const finished = event.data.val();

  if (finished) {

    const chooseWinnerPromise = pollRef.child('posts').once('value').then(snapshot => {

      var highestScore = -1;
      var winningPost;

      snapshot.forEach(function(childSnapshot) {
        const post = childSnapshot.val()

        if (post.voteCount > highestScore) {
          highestScore = post.voteCount
          winningPost = post
        }

        console.log('post', post)
        console.log('winning', winningPost)
      })

      console.log('winner', winningPost)

      console.log('postsRed', postsRef)
      console.log('storyId', storyId)
      console.log('chapterId', chapterId)

      postsRef.child(storyId).child(chapterId).child('posts').push().set(winningPost)
    })

    const newRound = parseInt(pollId) + 1

    const newVotePromise = pollRef.parent.child(newRound).set({
      round: newRound,
      finished: false,
      timeCreated: new Date().getTime()
    });

    promises.push(newVotePromise);
    promises.push(chooseWinnerPromise);

    return Promise.all(promises)
  }
});