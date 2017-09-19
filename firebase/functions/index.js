const functions = require('firebase-functions');

// Listens for new posts added to /stories/:storyId/vote
// and creates a vote end time at /stories/:storyId/voteEnds

exports.setVoteEndTime = functions.database.ref('/stories/{storyId}/vote')
    .onWrite(event => {
      return event.data.ref.parent.child('voteEnds').set(new Date().getTime() + 1000 * 60 * 5);
    });