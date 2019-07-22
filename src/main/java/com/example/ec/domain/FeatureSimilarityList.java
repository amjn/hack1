package com.example.ec.domain;

import java.util.ArrayList;

public class FeatureSimilarityList {
    private static FeatureSimilarityList _instance = null;
    private ArrayList<FeatureSimilarity> fsList = new ArrayList<FeatureSimilarity>();

    private FeatureSimilarityList(){}

    public static FeatureSimilarityList getInstance()
    {
        if (_instance == null)
        {
            _instance = new FeatureSimilarityList();
        }

        return _instance;
    }

    ArrayList<FeatureSimilarity> Get()
    {
        return fsList;
    }
}
