package com.example.ec.service;


import com.example.ec.domain.FeatureVector;
import com.example.ec.repo.FeatureVectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeatureVectorService {
    private FeatureVectorRepository featureVectorRepository;

    @Autowired
    public FeatureVectorService(FeatureVectorRepository featureVectorRepository) {
        this.featureVectorRepository = featureVectorRepository;
    }


    public FeatureVector createFeatureVector(int id, String featureVector) {
        if (featureVectorRepository.findOne(id) == null) {
            return featureVectorRepository.save(new FeatureVector(id, featureVector));
        } else {
            return null;
        }
    }

    public long total() {
        return featureVectorRepository.count();
    }


}
