'use strict';

const functions = require('firebase-functions');

exports.startRound = functions.database.ref('/posts/{storyId}/{roundId}')
.onCreate(event => {
    const storyId = event.params.storyId;
    const roundId = event.params.roundId;
    const pollRef = event.data.ref.root.child('poll').child(storyId);

    console.log('got new round ' + roundId + ' in story ' + storyId)

    pollRef.update({
      endTime: Date.now() + 24 * 60 * 60 * 1000,
      round: parseInt(roundId)
      })
})

// Listen for vote finish becoming true then create next vote

exports.finishUpdated = functions.database.ref('/poll/{storyId}/finished')
.onWrite(event => {

    const finished = event.data.val();
    const storyId = event.params.storyId;
    const pollRef = event.data.ref.root.child('poll').child(storyId)
    const storyRef = event.data.ref.root.child('stories').child(storyId)
    const winnersRef = event.data.ref.root.child('winners').child(storyId)
    const usersRef = event.data.ref.root.child('users')

    if (finished) {

        return pollRef.once('value').then(pollSnapshot => {

            const roundId = pollSnapshot.child('round').val()
            const round = parseInt(roundId)
            const postsRef = event.data.ref.root.child('posts').child(storyId).child(roundId)

            return postsRef.once('value').then(postsSnapshot => {

                var highestScore = -1;
                var winningPost;
                var winningPostId;

                postsSnapshot.forEach(function(postSnapshot) {

                    const post = postSnapshot.val()
                    const postId = postSnapshot.key

                    if (post.voteCount > highestScore) {
                        highestScore = post.voteCount
                        winningPost = post
                        winningPostId = postId
                    }
                })

                return storyRef.once('value').then(storySnapshot => {

                    const rounds = parseInt(storySnapshot.child('chapterSize').val())
                    const chapterLimit = parseInt(storySnapshot.child('chapterLimit').val())

                    if (round % rounds !== 0) {

                        console.log('adding winning post', winningPost)

                        // Push winning post
                        winnersRef.child(winningPostId).set(winningPost)

                        // Update winning user's posts
                        usersRef.child(winningPost.userId).child('posts').child(winningPostId).set(true)

                        // Increment postCount on story
                        storyRef.child('postCount').transaction(function (current_value) {
                          return (current_value || 0) + 1;
                        });

                        // Start new round
                        pollRef.update({
                            round: round + 1
                        })
                    } else {

                        // Chapter is over

                        const chapterId = round / rounds
                        const newChapter = chapterId + 1

                        console.log('chapter', chapterId, 'over')

                        // Assign winning title to story chapter
                        storyRef.child('chapters').child(chapterId).child('title').set(winningPost.message)

                        if (newChapter == chapterLimit) {

                            // Story finished
                            storyRef.child('finished').set(true)
                            pollRef.remove()

                        } else {
                            // Add the next chapter
                            storyRef.child('chapters').child(newChapter).set({chapter: newChapter})

                            // Increment chapterCount on story
                            storyRef.child('chapterCount').transaction(function (current_value) {
                              return (current_value || 0) + 1;
                            });

                            // Start new round
                            pollRef.update({
                                round: round + 1
                            })
                        }
                    }
                })
            })
        })
    }
})