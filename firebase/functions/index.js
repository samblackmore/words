const functions = require('firebase-functions');

// Listens for new posts added to /stories/:storyId/vote
// and creates a vote end time at /stories/:storyId/voteEnds

exports.gotPost = functions.database.ref('/stories/{storyId}/vote')
    .onWrite(event => {

        newPost = event.data.val()

        console.log('got post', newPost, 'checking votes')

        event.data.ref.parent.child('votes').once("value",
        function(snapshot) {
          if (snapshot.val() === null) {
            return event.data.ref.parent.child('votes').push.set(
            {
            timeout: new Date().getTime() + 1000 * 60 * 5,
            posts: [newPost]
            }
            )
          }
        },
        function (errorObject) {
          console.log("The read failed: " + errorObject.code);
        });



      //return event.data.ref.parent.child('voteEnds').set(new Date().getTime() + 1000 * 60 * 5);
    });