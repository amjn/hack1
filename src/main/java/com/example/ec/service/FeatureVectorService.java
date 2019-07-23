package com.example.ec.service;


import com.example.ec.domain.FeatureSimilarity;
import com.example.ec.domain.FeatureSimilarityMap;
import com.example.ec.domain.FeatureVectorWithType;
import com.example.ec.domain.FeatureVectorMap;
import com.example.ec.repo.FeatureVectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class FeatureVectorService {
    private FeatureVectorRepository featureVectorRepository;

    @Autowired
    public FeatureVectorService(FeatureVectorRepository featureVectorRepository) {
        this.featureVectorRepository = featureVectorRepository;
    }

    public Map<String, String> getReplacementContext(Map<String, Boolean> visibleData, String likedId)
    {
        Map<String, String> replacements = new HashMap<>();

        ArrayList<String> replaceableIds =  GetReplaceableIds(visibleData, likedId);
        if (replaceableIds.size() > 0)
        {
//           find the top similar ones which are not in the visible list
            FeatureSimilarityMap featureSimilarityMap = FeatureSimilarityMap.getInstance();
            Map<String, FeatureSimilarity> fsMap = featureSimilarityMap.Get();

            if (fsMap.containsKey(likedId))
            {
                ArrayList<String> similarIds = fsMap.get(likedId).getSimilarIds();
                if (similarIds != null)
                {
                    for (String id: similarIds)
                    {
                        if (!visibleData.containsKey(id))
                        {
                           if (replaceableIds.size() > 0)
                           {
                              replacements.put(replaceableIds.get(0), id);
                              replaceableIds.remove(0);
                           }else {
                               break;
                           }
                        }
                    }
                }
            }
        }

        return replacements;
    }

    private Long[] getFeatureVectorForId(String imageId) {
        return FeatureVectorMap.getInstance().getFeatureVectorForId(imageId);
    }

    private ArrayList<String> GetReplaceableIds(Map<String, Boolean> visibleData, String likedId)
    {
        ArrayList<String> replaceableIds = new ArrayList<>();
        int numUnliked = Collections.frequency(visibleData.values(), false);
        Long[] likedFv = getFeatureVectorForId(likedId);
        double highestCosine = Double.MAX_VALUE; //highest
        double secondHighestCosine = Double.MAX_VALUE; //secondhighest
        String id1 = null, id2 = null;
        AtomicReference<Double> cosineTemp = new AtomicReference<>((double) 0);
        if (numUnliked > 2)
        {
            // get unliked ones
            Map<String, Boolean> collect = visibleData.entrySet().stream()
                    .filter(x -> !x.getValue())
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            List<String> unlikedIds = (List<String>) collect.keySet();
            for (String id : unlikedIds) {
                Long[] fv = getFeatureVectorForId(id);
                cosineTemp.set(getCosineSimilarity(likedFv, fv));
                if (highestCosine == Double.MAX_VALUE) {
                    highestCosine = cosineTemp.get();
                    id1 = id;
                } else if(secondHighestCosine == Double.MAX_VALUE) {
                    if(highestCosine < cosineTemp.get()){
                        double tempCosine = highestCosine;
                        String tempId = id1;
                        highestCosine = cosineTemp.get();
                        id1 = id;
                        secondHighestCosine = tempCosine;
                        id2 = tempId;
                    } else {
                        secondHighestCosine = cosineTemp.get();
                        id2 = id;
                    }
                } else if(cosineTemp.get() > highestCosine){
                    double tempCosine = highestCosine;
                    String tempId = id1;
                    highestCosine = cosineTemp.get();
                    id1 = id;
                    secondHighestCosine = tempCosine;
                    id2 = tempId;
                } else if(cosineTemp.get() > secondHighestCosine) {
                    secondHighestCosine = cosineTemp.get();
                    id2 = id;
                }
            }
            replaceableIds.add(id1);
            replaceableIds.add(id2);

        }
        else if (numUnliked > 0 && numUnliked <= 2)
        {
            Map<String, Boolean> collect = visibleData.entrySet().stream()
                    .filter(x -> !x.getValue())
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            List<String> unlikedIds = new ArrayList<>(collect.keySet());
            replaceableIds.addAll(unlikedIds);
        }

        return replaceableIds;
    }

    private double getCosineSimilarity(Long[] featureVector1, Long[] featureVector2)
    {
        double output = 0;
        double degrees = 45.0;
        double radians = Math.toRadians(degrees);

        if (featureVector1 != null && featureVector2 != null && featureVector1.length == featureVector2.length)
        {

            for (int i = 0; i < featureVector1.length; i++)
            {
                output += featureVector1[i] * featureVector2[i];
            }
            output *= Math.cos(radians);
        }else
        {
            System.out.println("Invalid vector found");
        }
        return output;
    }


    public long total() {
        return featureVectorRepository.count();
    }


}
