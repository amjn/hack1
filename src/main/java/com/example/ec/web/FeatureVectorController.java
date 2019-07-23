package com.example.ec.web;

import com.example.ec.repo.FeatureVectorRepository;
import com.example.ec.service.FeatureVectorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/GetRecommendations")
@CrossOrigin(origins = "http://localhost:8080")
public class FeatureVectorController {

    FeatureVectorService service;

    @Autowired
    public FeatureVectorController(FeatureVectorService service) {
        this.service = service;
    }

    protected FeatureVectorController() {

    }

    class Replace {
        public String from;
        public String to;

        public Replace(String from, String to) {
            this.from = from;
            this.to = to;
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/liked/{selectedId:.+}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public @ResponseBody String getRecommendations(@PathVariable(value = "selectedId") String likedId, @RequestBody Map<String, Boolean> visibleData) {
        Map<String, String> map = service.getReplacementContext(visibleData, likedId);
//        map.put("1.jpg", "2.jpg");
//        map.put("3.jpg", "4.jpg");

        List<Replace> replaceList = new ArrayList<>();
        Replace replace;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            replace = new Replace(entry.getKey(), entry.getValue());
            replaceList.add(replace);
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(replaceList);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/disliked/{selectedId:.+}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public @ResponseBody String getRecommendationsForDislike(@PathVariable(value = "selectedId") String dislikedId,
                                                             @RequestBody Map<String, Boolean> visibleData) {
        Map<String, String> map = service.getDislikedReplacementContext(visibleData, dislikedId);

        List<Replace> replaceList = new ArrayList<>();
        Replace replace;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            replace = new Replace(entry.getKey(), entry.getValue());
            replaceList.add(replace);
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(replaceList);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }



}
