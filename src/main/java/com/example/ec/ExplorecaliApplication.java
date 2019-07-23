package com.example.ec;

import com.example.ec.domain.FeatureSimilarity;
import com.example.ec.domain.FeatureSimilarityMap;
import com.example.ec.domain.FeatureVectorMap;
import com.example.ec.domain.FeatureVectorWithType;
import com.example.ec.service.FeatureVectorService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Class for the Spring Boot Application
 *
 * Created by Mary Ellen Bowman
 */
@SpringBootApplication
public class ExplorecaliApplication implements CommandLineRunner {
//
//	@Autowired
//	private TourPackageService tourPackageService;
//	@Autowired
//	private TourService tourService;

	@Autowired
	private FeatureVectorService featureVectorService;

	public static void main(String[] args) {
		SpringApplication.run(ExplorecaliApplication.class, args);
	}

	/**
	 * Method invoked after this class has been instantiated by Spring container
	 * Initializes the in-memory database with all the TourPackages and Tours.
	 *
	 * @param strings
	 * @throws Exception if problem occurs.
     */
	@Override
	public void run(String... strings) throws Exception {
		readFileForImages();
		readFileForFeatureVectors();
		System.out.println("Number of feature vectors =" + featureVectorService.total());

	}

	private void readFileForImages() {
		try {
		File file = ResourceUtils.getFile("classpath:IdAndSimilarIds.csv");
		InputStream in = new FileInputStream(file);
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		FeatureSimilarityMap map = FeatureSimilarityMap.getInstance();

			InputStreamReader inReader = new InputStreamReader(in);
			br = new BufferedReader(inReader);
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] imageIds = line.split(cvsSplitBy);
				ArrayList<Long> similarIds = new ArrayList<>();
				for(int i=1;i<imageIds.length;i++){
					similarIds.add(Long.parseLong(imageIds[i]));
				}
				map.Get().put(Long.parseLong(imageIds[0]), new FeatureSimilarity(Long.parseLong(imageIds[0]), similarIds));

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//list.Get();
	}

	private void readFileForFeatureVectors() {
		try{
			File file = ResourceUtils.getFile("classpath:IdAndFeatureVectors.csv");
			InputStream in = new FileInputStream(file);
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";
			FeatureVectorMap map = FeatureVectorMap.getInstance();
			InputStreamReader inReader = new InputStreamReader(in);
			br = new BufferedReader(inReader);
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
		}
	}
}
