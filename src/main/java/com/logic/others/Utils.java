package com.logic.others;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String getToken(String kind) {
        Pattern pattern = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");
        Matcher matcher = pattern.matcher(kind);

        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String hexCode = matcher.group(1);
            int codePoint = Integer.parseInt(hexCode, 16);
            matcher.appendReplacement(result, String.valueOf((char) codePoint));
        }

        matcher.appendTail(result);
        return result.toString();
    }

    public static void printTruthTable(Map<Map<String, Boolean>, Boolean> table) {
        if (table.isEmpty()) {
            System.out.println("The truth table is empty.");
            return;
        }

        Map<String, Boolean> firstInterpretation = table.keySet().iterator().next();
        List<String> headers = new ArrayList<>(firstInterpretation.keySet());

        System.out.print("| ");
        for (String header : headers) {
            System.out.print(header + " | ");
        }
        System.out.println("Result");

        for (Map.Entry<Map<String, Boolean>, Boolean> entry : table.entrySet()) {
            Map<String, Boolean> interpretation = entry.getKey();
            Boolean result = entry.getValue();

            System.out.print("| ");
            for (String header : headers) {
                System.out.print((interpretation.get(header) ? "T" : "F") + " | ");
            }

            System.out.println((result ? "T" : "F"));
        }
    }


}
