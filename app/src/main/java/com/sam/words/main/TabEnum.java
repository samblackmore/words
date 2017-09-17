package com.sam.words.main;

enum TabEnum {

    TOP, NEW, ME;

    static TabEnum getSection(int num) {
        TabEnum r = null;
        switch (num) {
            case 1: r = TabEnum.TOP; break;
            case 2: r = TabEnum.NEW; break;
            case 3: r = TabEnum.ME; break;
        }
        assert r != null;
        return r;
    }
}
