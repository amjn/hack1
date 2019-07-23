package com.example.ec.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeatureVectorMap {

    private static FeatureVectorMap _instance = null;
    private Map<Long, FeatureVectorWithType> fvMap = new HashMap<Long, FeatureVectorWithType>();

    private FeatureVectorMap(){}

    public static FeatureVectorMap getInstance()
    {
        if (_instance == null)
        {
            _instance = new FeatureVectorMap();
        }

        return _instance;
    }

    public Map<Long, FeatureVectorWithType> Get()
    {
        return fvMap;
    }

    public Long[] getFeatureVectorForId(Long imageId) {
        return fvMap.get(imageId).getFeatureVector(); //null check
    }

}
