package com.sam.words;

enum BrowseTab {

    TOP, NEW, ME;

    static BrowseTab getSection(int num) {
        BrowseTab r = null;
        switch (num) {
            case 1: r = BrowseTab.TOP; break;
            case 2: r = BrowseTab.NEW; break;
            case 3: r = BrowseTab.ME; break;
        }
        assert r != null;
        return r;
    }
}
