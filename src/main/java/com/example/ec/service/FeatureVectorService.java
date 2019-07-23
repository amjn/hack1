package com.example.ec.service;


import com.example.ec.domain.FeatureSimilarity;
import com.example.ec.domain.FeatureSimilarityList;
import com.example.ec.domain.FeatureVectorWithType;
import com.example.ec.domain.FeatureVectorMap;
import com.example.ec.domain.FeatureVectorMap;
import com.example.ec.domain.FeatureVectorWithType;
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


//    public FeatureVector createFeatureVector(FeatureVector fv) {
//        if (featureVectorRepository.findOne(fv.getId()) == null) {
//            return featureVectorRepository.save(fv);
//        } else {
//            return null;
//        }
//    }

    public void readFileForImages() {
        String csvFile = "C:/Users/snvemula/IdeaProjects/hack1/IdAndSimilarIds.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        FeatureSimilarityList list = FeatureSimilarityList.getInstance();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] imageIds = line.split(cvsSplitBy);
                ArrayList<Long> similarIds = new ArrayList<>();
                for(int i=1;i<imageIds.length;i++){
                    similarIds.add(Long.parseLong(imageIds[i]));
                }
                list.Get().add(new FeatureSimilarity(Long.parseLong(imageIds[0]), similarIds));

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //list.Get();
    }

    public void readFileForFeatureVectors() {
        String csvFile = "C:/Users/snvemula/IdeaProjects/hack1/IdAndFeatureVectors.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        FeatureVectorMap map = FeatureVectorMap.getInstance();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] strings = line.split(cvsSplitBy);
                Long[] fv = new Long[1024];
                for(int i=1;i<strings.length-2;i++){
                    fv[i-1] = Long.parseLong(strings[i]);
                }
                map.Get().put(Long.parseLong(strings[0]), (new FeatureVectorWithType(fv, Long.parseLong(strings[strings.length-1]))));

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
       // System.out.println(list.Get().size()+"ssssssssss");
    }

    public Map<Long, Long> getReplacementContext(Map<Long, Long> visibleData, Long likedId)
    {
        Map<Long, Long> replacements = new HashMap<>();

        ArrayList<Long> ids =  GetReplaceableIds(visibleData, likedId);
        if (ids.size() > 0)
        {
//            TODO: find the top similar ones which are not in the visible list
        }



        return replacements;
    }

    public Long[] getFeatureVectorForId(Long imageId) {
        return FeatureVectorMap.getInstance().getFeatureVectorForId(imageId);
    }

    private ArrayList<Long> GetReplaceableIds(Map<Long, Long> visibleData, Long likedId)
    {
        ArrayList<Long> replaceableIds = new ArrayList<>();
        int numUnliked = Collections.frequency(visibleData.values(), 0);
        Long[] likedFv = getFeatureVectorForId(likedId);
        double highestCosine1 = Double.MAX_VALUE;
        double highestCosine2 = Double.MAX_VALUE;
        Long id1 = null, id2 = null;
        AtomicReference<Double> cosineTemp = new AtomicReference<>((double) 0);
        if (numUnliked > 2)
        {
            // get unliked ones
            Map<Long, Long> collect = visibleData.entrySet().stream()
                    .filter(x -> x.getValue() == 0)
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            List<Long> unlikedIds = (List<Long>) collect.keySet();
            for (Long id : unlikedIds) {
                Long[] fv = getFeatureVectorForId(id);
                cosineTemp.set(getCosineSimilarity(likedFv, fv));
                if (highestCosine1 == Double.MAX_VALUE) {
                    highestCosine1 = cosineTemp.get();
                    id1 = id;
                } else if(highestCosine2 == Double.MAX_VALUE) {
                    highestCosine2 = cosineTemp.get();
                    id2 = id;
                } else if(cosineTemp.get() > highestCosine1){
                    highestCosine1 = cosineTemp.get();
                    id1 = id;
                } else if(cosineTemp.get() > highestCosine2) {
                    highestCosine2 = cosineTemp.get();
                    id2 = id;
                }
            }
            replaceableIds.add(id1);
            replaceableIds.add(id2);

        }
        else if (numUnliked > 0 && numUnliked <= 2)
        {
            for (Map.Entry<Long, Long> entry : visibleData.entrySet()) {
                if (entry.getValue() == 0)
                {
                    replaceableIds.add(entry.getKey());
                }

            }
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
            System.out.println(" Invalid vector found");
        }
        return output;
    }


    public long total() {
        return featureVectorRepository.count();
    }


}
