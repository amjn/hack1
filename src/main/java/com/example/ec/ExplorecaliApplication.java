package com.example.ec;

import com.example.ec.domain.FeatureVector;
import com.example.ec.service.FeatureVectorService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
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
		//Create the default tour packages
//		tourPackageService.createTourPackage("BC", "Backpack Cal");
		featureVectorService.createFeatureVector( new FeatureVector(1l, new Long[]{1l,2l,3l}, 1l));
		featureVectorService.createFeatureVector( new FeatureVector(2l, new Long[]{1l,2l,3l}, 2l));
		featureVectorService.readFileForImages();
		featureVectorService.readFileForFeatureVectors();
		System.out.println("Number of feature vectors =" + featureVectorService.total());



//		//Persist the Tours to the database
//		importTours().forEach(t-> tourService.createTour(
//				t.title,
//				t.description,
//				t.blurb,
//				Integer.parseInt(t.price),
//				t.length,
//				t.bullets,
//				t.keywords,
//				t.packageType,
//				Difficulty.valueOf(t.difficulty),
//				Region.findByLabel(t.region)));
//		System.out.println("Number of tours =" + tourService.total());


	}

	/**
	 * Helper class to import the records in the ExploreCalifornia.json
	 */
	static class TourFromFile {
		//attributes as listed in the .json file
		private String packageType, title, description, blurb, price, length, bullets, keywords,  difficulty, region;

		/**
		 * Open the ExploreCalifornia.json, unmarshal every entry into a TourFromFile Object.
		 *
		 * @return a List of TourFromFile objects.
		 * @throws IOException if ObjectMapper unable to open file.
         */
		static List<TourFromFile> importTours() throws IOException {
			return new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).
					readValue(TourFromFile.class.getResourceAsStream("/ExploreCalifornia.json"),new TypeReference<List<TourFromFile>>(){});
		}
	}

}
