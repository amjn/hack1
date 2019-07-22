package com.example.ec.domain;

import java.util.ArrayList;

public class FeatureVectorList {

    private static FeatureVectorList _instance = null;
    private ArrayList<FeatureVector> fvList = new ArrayList<FeatureVector>();

    private FeatureVectorList(){}

    public static FeatureVectorList getInstance()
    {
        if (_instance == null)
        {
            _instance = new FeatureVectorList();
        }

        return _instance;
    }

    ArrayList<FeatureVector> Get()
    {
        return fvList;
    }

}
