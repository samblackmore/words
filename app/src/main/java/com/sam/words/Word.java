package com.sam.words;

class Word {
    private String word;
    private String submittedBy;
    private long dateSubmitted;
    private Long dateApproved;
    private int votes;

    // For admin use to upload default words
    Word() {
        submittedBy = "default";
        dateSubmitted = System.currentTimeMillis();
        dateApproved = System.currentTimeMillis();
        votes = 0;
    }

    Word(String submittedBy) {
        this.submittedBy = submittedBy;
        dateSubmitted = System.currentTimeMillis();
        dateApproved = null;
        votes = 0;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public long getDateSubmitted() {
        return dateSubmitted;
    }

    public Long getDateApproved() {
        return dateApproved;
    }

    public int getVotes() {
        return votes;
    }
}
