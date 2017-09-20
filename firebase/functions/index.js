'use strict';

const functions = require('firebase-functions');

// Listen for vote finish becoming true then create next vote

exports.finishUpdated = functions.database.ref('/stories/{storyId}/votes/{voteId}/finished')
.onUpdate(event => {

  const voteId = parseInt(event.params.voteId);
  const votesRef = event.data.ref.parent.parent;
  const finished = event.data.val();

  if (finished) {
    return votesRef.child(voteId + 1).set(
    {
      finished: false,
      timeCreated: new Date().getTime()
    });
  }
});