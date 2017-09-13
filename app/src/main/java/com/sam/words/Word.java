package com.sam.words;

public class Word {
    private Boolean approved;
    private String submittedBy;
    private long dateSubmitted;
    private long dateApproved;
    private int votes;
    private boolean explicit;

    public Word() {
        approved = true;
        submittedBy = "default";
        dateSubmitted = System.currentTimeMillis();
        dateApproved = System.currentTimeMillis();
        votes = 0;
        explicit = false;
    }

    public Boolean getApproved() {
        return approved;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public long getDateSubmitted() {
        return dateSubmitted;
    }

    public long getDateApproved() {
        return dateApproved;
    }

    public int getVotes() {
        return votes;
    }

    public boolean isExplicit() {
        return explicit;
    }
}
