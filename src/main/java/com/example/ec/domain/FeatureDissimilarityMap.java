package com.example.ec.domain;

import java.util.HashMap;
import java.util.Map;

public class FeatureDissimilarityMap {
    private static FeatureDissimilarityMap _instance = null;
    private Map<String, FeatureDissimilarity> fsMap = new HashMap<>();

    private FeatureDissimilarityMap(){}

    public static FeatureDissimilarityMap getInstance()
    {
        if (_instance == null)
        {
            _instance = new FeatureDissimilarityMap();
        }

        return _instance;
    }

    public Map<String, FeatureDissimilarity> Get()
    {
        return fsMap;
    }
}
