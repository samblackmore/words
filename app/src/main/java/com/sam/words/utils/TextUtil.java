package com.sam.words.utils;

public class TextUtil {
    public static String capitalize(String s) {
        String[] words = s.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            String cap = word.substring(0, 1).toUpperCase() + word.substring(1);
            builder.append(cap);
            builder.append(" ");
        }
        return builder.toString();
    }
}
