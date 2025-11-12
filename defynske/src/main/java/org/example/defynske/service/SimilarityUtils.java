package org.example.defynske.service;

import java.util.*;

public class SimilarityUtils {

    public static double cosineSimilarity(Map<Long, Double> a, Map<Long, Double> b) {
        double dot = 0, magA = 0, magB = 0;

        for (var key : a.keySet()) {
            double va = a.get(key);
            double vb = b.getOrDefault(key, 0.0);
            dot += va * vb;
        }
        for (double va : a.values()) magA += va * va;
        for (double vb : b.values()) magB += vb * vb;

        if (magA == 0 || magB == 0) return 0;
        return dot / (Math.sqrt(magA) * Math.sqrt(magB));
    }
}
