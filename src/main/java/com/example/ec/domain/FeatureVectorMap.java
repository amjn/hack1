package com.example.ec.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeatureVectorMap {

    private static FeatureVectorMap _instance = null;
    private Map<String, FeatureVectorWithType> fvMap = new HashMap<String, FeatureVectorWithType>();

    private FeatureVectorMap(){}

    public static FeatureVectorMap getInstance()
    {
        if (_instance == null)
        {
            _instance = new FeatureVectorMap();
        }

        return _instance;
    }

    public Map<String, FeatureVectorWithType> Get()
    {
        return fvMap;
    }

    public Long[] getFeatureVectorForId(String imageId) {
        return fvMap.get(imageId).getFeatureVector(); //null check
    }

}
