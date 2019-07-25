package com.example.ec.service;


import com.example.ec.domain.*;
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

    public Map<String, String> getDislikedReplacementContext(Map<String, Boolean> visibleData, String dislikedId)
    {
        Map<String, String> replacements = new HashMap<>();

        ArrayList<String> replaceableIds =  GetReplaceableIdsForDislike(visibleData, dislikedId);
        replaceableIds.add(dislikedId);
        // disliked Id will always be the one to be replaced
        if (replaceableIds.size() > 1)
        {
//           find the top dissimilar ones which are not in the visible list
            FeatureDissimilarityMap featureDissimilarityMap = FeatureDissimilarityMap.getInstance();
            Map<String, FeatureDissimilarity> fsMap = featureDissimilarityMap.Get();

            if (fsMap.containsKey(dislikedId))
            {
                ArrayList<String> dissimilarIds = fsMap.get(dislikedId).getDissimilarIds();
                if (dissimilarIds != null)
                {
                    for (String id: dissimilarIds)
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

    private double[] getFeatureVectorForId(String imageId) {
        return FeatureVectorMap.getInstance().getFeatureVectorForId(imageId);
    }

    private ArrayList<String> GetReplaceableIds(Map<String, Boolean> visibleData, String likedId)
    {
        ArrayList<String> replaceableIds = new ArrayList<>();
        int numUnliked = Collections.frequency(visibleData.values(), false);
        double[] likedFv = getFeatureVectorForId(likedId);
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
            List<String> unlikedIds = new ArrayList<> (collect.keySet());

            if (unlikedIds.contains(likedId))
            {
                unlikedIds.remove(likedId);
            }

//            int rand1 = getRandomNumberInRange(0, unlikedIds.size() - 1);
//            replaceableIds.add(unlikedIds.get(rand1));
//            int rand2 = getRandomNumberInRange(0, unlikedIds.size() - 1);
//            if (rand2 != rand1)
//            {
//                replaceableIds.add(unlikedIds.get(rand2));
//            }
//            else
//            {
//                rand2 = getRandomNumberInRange(0, unlikedIds.size() - 1);
//                if (rand2 != rand1)
//                {
//                    replaceableIds.add(unlikedIds.get(rand2));
//                }
//            }
//
            for (String id : unlikedIds) {
                double[] fv = getFeatureVectorForId(id);
                cosineTemp.set(getEuclideanDistance(likedFv, fv));
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

    private ArrayList<String> GetReplaceableIdsForDislike(Map<String, Boolean> visibleData, String dislikedId)
    {
        ArrayList<String> replaceableIds = new ArrayList<>();
        int numUnliked = Collections.frequency(visibleData.values(), false);
        double[] likedFv = getFeatureVectorForId(dislikedId);
        double leastCosine = Double.MIN_VALUE;
        double secondleastCosine = Double.MIN_VALUE;
        String id1 = null, id2 = null;
        AtomicReference<Double> cosineTemp = new AtomicReference<>((double) 0);
        if (numUnliked > 2)
        {
            // get unliked ones
            Map<String, Boolean> collect = visibleData.entrySet().stream()
                    .filter(x -> !x.getValue())
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            List<String> unlikedIds = new ArrayList<> (collect.keySet());

            if (unlikedIds.contains(dislikedId))
            {
                unlikedIds.remove(dislikedId);
            }
//            int rand1 = getRandomNumberInRange(0, unlikedIds.size() - 1);
//            replaceableIds.add(unlikedIds.get(rand1));
//            int rand2 = getRandomNumberInRange(0, unlikedIds.size() - 1);
//            if (rand2 != rand1)
//            {
//                replaceableIds.add(unlikedIds.get(rand2));
//            }
//            else
//            {
//                rand2 = getRandomNumberInRange(0, unlikedIds.size() - 1);
//                if (rand2 != rand1)
//                {
//                    replaceableIds.add(unlikedIds.get(rand2));
//                }
//            }

            for (String id : unlikedIds) {
                double[] fv = getFeatureVectorForId(id);
                cosineTemp.set(getEuclideanDistance(likedFv, fv));
                if (leastCosine == Double.MIN_VALUE) {
                    leastCosine = cosineTemp.get();
                    id1 = id;
                } else if(secondleastCosine == Double.MIN_VALUE) {
                    if(leastCosine > cosineTemp.get()){
                        double tempCosine = leastCosine;
                        String tempId = id1;
                        leastCosine = cosineTemp.get();
                        id1 = id;
                        secondleastCosine = tempCosine;
                        id2 = tempId;
                    } else {
                        secondleastCosine = cosineTemp.get();
                        id2 = id;
                    }
                } else if(cosineTemp.get() < leastCosine){
                    double tempCosine = leastCosine;
                    String tempId = id1;
                    leastCosine = cosineTemp.get();
                    id1 = id;
                    secondleastCosine = tempCosine;
                    id2 = tempId;
                } else if(cosineTemp.get() < secondleastCosine) {
                    secondleastCosine = cosineTemp.get();
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

    private int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private double getCosineSimilarity(double[] featureVector1, double[] featureVector2)
    {
        double output = 0;
        double sqSum1 = 0, sqSum2 = 0;

        if (featureVector1 != null && featureVector2 != null && featureVector1.length == featureVector2.length)
        {
            for (int i = 0; i < featureVector1.length-1; i++)
            {
                output += featureVector1[i] * featureVector2[i];
                sqSum1 += featureVector1[i] * featureVector1[i];
                sqSum2 += featureVector2[i] * featureVector2[i];
            }
            output  = 1 - (output / (Math.sqrt(sqSum1) * Math.sqrt(sqSum2)));
        }else
        {
            System.out.println("Invalid vector found");
        }
        return output;
    }

    private double getEuclideanDistance(double[] featureVector1, double[] featureVector2)
    {
        double output = 0;
        double diff = 0;

        if (featureVector1 != null && featureVector2 != null && featureVector1.length == featureVector2.length)
        {
            for (int i = 0; i < featureVector1.length-1; i++)
            {
                diff += Math.pow((featureVector1[i] - featureVector2[i]), 2);
            }
            output  = Math.sqrt(diff);
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
