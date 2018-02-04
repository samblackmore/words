package com.sam.story.utils;

public class TextUtil {
    public static String capitalize(String s) {
        if (s.length() == 0)
            return s;

        String[] words = s.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            String cap = word.substring(0, 1).toUpperCase() + word.substring(1);
            builder.append(cap);
            builder.append(" ");
        }
        return builder.toString();
    }

    public static String numToString(int num) {
        switch (num) {
            case 1: return "one";
            case 2: return "two";
            case 3: return "three";
            case 4: return "four";
            case 5: return "five";
            case 6: return "six";
            case 7: return "seven";
            case 8: return "eight";
            case 9: return "nine";
            case 10: return "ten";
        }
        throw new IllegalArgumentException("Only accepts 1-10");
    }
}
