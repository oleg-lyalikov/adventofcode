package org.lialikov.adventofcode.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    public static String replaceAt(String string, int position, char value) {
        String end = position == string.length() ? "" : string.substring(position + 1);
        return string.substring(0, position) + value + end;
    }

    public static char[][] toCharArrays(List<String> lines) {
        char[][] res = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(0).length(); j++) {
                res[i][j] = lines.get(i).charAt(j);
            }
        }
        return res;
    }

    public static List<Integer> findWord(String textString, String word) {
        List<Integer> indexes = new ArrayList<>();
        int index = 0;
        while (index != -1) {
            index = textString.indexOf(word, index);
            if (index != -1) {
                indexes.add(index);
                index++;
            }
        }
        return indexes;
    }
}
