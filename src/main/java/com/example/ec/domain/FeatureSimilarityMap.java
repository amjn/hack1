package com.example.ec.domain;

import java.util.HashMap;
import java.util.Map;

public class FeatureSimilarityMap {
    private static FeatureSimilarityMap _instance = null;
    private Map<Long, FeatureSimilarity> fsMap = new HashMap<>();

    private FeatureSimilarityMap(){}

    public static FeatureSimilarityMap getInstance()
    {
        if (_instance == null)
        {
            _instance = new FeatureSimilarityMap();
        }

        return _instance;
    }

    public Map<Long, FeatureSimilarity> Get()
    {
        return fsMap;
    }
}
