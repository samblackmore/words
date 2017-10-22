package com.sam.words.main;

enum TabEnum {

    ACTIVITY ("Home"),
    NEW ("New"),
    TOP ("Top");

    public static final int TAB_COUNT = 3;
    private String name;

    TabEnum(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    static TabEnum getSection(int num) {
        TabEnum r = null;
        switch (num) {
            case 1: r = TabEnum.ACTIVITY; break;
            case 2: r = TabEnum.NEW; break;
            case 3: r = TabEnum.TOP; break;
        }
        assert r != null;
        return r;
    }
}
