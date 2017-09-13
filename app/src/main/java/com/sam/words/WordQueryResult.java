package com.sam.words;

class WordQueryResult {

    private boolean found;
    private String word;

    public WordQueryResult(boolean found, String word) {
        this.found = found;
        this.word = word;
    }

    public String word() {
        return word;
    }

    public boolean found() {
        return found;
    }
}
