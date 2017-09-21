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

      var highestScore = -1;
      var winningPost;

      snapshot.forEach(function(childSnapshot) {
        const post = childSnapshot.val()

        if (post.voteCount > highestScore) {
          highestScore = post.voteCount
          winningPost = post
        }
      })

      return storyRef.child('chapters').limitToLast(1).once('value').then(chaptersSnapshot => {

        var addToStoryPromises = []

        var latestChapterId = -1

        chaptersSnapshot.forEach(function(chapterSnapshot) {
          latestChapterId = chapterSnapshot.key
        })

        const chapterRef = storyRef.child('chapters').child(latestChapterId)

        const addPostPromise = chapterRef.child('posts').push().set(winningPost)
        const addMessagePromise = chapterRef.child('content').transaction(function(content) {


          console.log('transaction old content:', content)
          console.log('transaction append:', winningPost.message)

          if (content) {
            content += winningPost.message
          }
          return content;
        })

        addToStoryPromises.push(addPostPromise)
        addToStoryPromises.push(addMessagePromise)

        return Promise.all(addToStoryPromises)
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