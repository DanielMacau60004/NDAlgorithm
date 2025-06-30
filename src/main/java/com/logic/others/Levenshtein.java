package com.logic.others;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Set;

public class Levenshtein {

    public static String findMostSimilar(Set<String> set, String target) {
        LevenshteinDistance levenshtein = new LevenshteinDistance();
        String bestMatch = null;
        int lowestDistance = Integer.MAX_VALUE;

        for (String candidate : set) {
            int distance = levenshtein.apply(candidate, target);
            if (distance < lowestDistance) {
                lowestDistance = distance;
                bestMatch = candidate;
            }
        }

        return bestMatch;
    }

}
