package com.example.ec.service;


import com.example.ec.domain.FeatureSimilarity;
import com.example.ec.domain.FeatureSimilarityList;
import com.example.ec.domain.FeatureVector;
import com.example.ec.domain.FeatureVectorList;
import com.example.ec.repo.FeatureVectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeatureVectorService {
    private FeatureVectorRepository featureVectorRepository;

    @Autowired
    public FeatureVectorService(FeatureVectorRepository featureVectorRepository) {
        this.featureVectorRepository = featureVectorRepository;
    }


    public FeatureVector createFeatureVector(FeatureVector fv) {
        if (featureVectorRepository.findOne(fv.getId()) == null) {
            return featureVectorRepository.save(fv);
        } else {
            return null;
        }
    }

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
        FeatureVectorList list = FeatureVectorList.getInstance();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] strings = line.split(cvsSplitBy);
                Long[] fv = new Long[1024];
                for(int i=1;i<strings.length-2;i++){
                    fv[i-1] = Long.parseLong(strings[i]);
                }
                list.Get().add(new FeatureVector(Long.parseLong(strings[0]), fv, Long.parseLong(strings[strings.length-1])));

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

    public long total() {
        return featureVectorRepository.count();
    }


}
