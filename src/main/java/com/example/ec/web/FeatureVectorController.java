package com.example.ec.web;

import com.example.ec.repo.FeatureVectorRepository;
import com.example.ec.service.FeatureVectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/GetRecommendations")
public class FeatureVectorController {

    FeatureVectorService service;

    @Autowired
    public FeatureVectorController(FeatureVectorService service) {
        this.service = service;
    }

    protected FeatureVectorController() {

    }

    @RequestMapping(method = RequestMethod.POST, path = "/liked/{selectedId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> getRecommendations(@PathVariable(value = "selectedId") String likedId, @RequestBody Map<String, Boolean> visibleData) {
        Map map = service.getReplacementContext(visibleData, likedId);
        return map;
    }



}
