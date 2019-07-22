package com.example.ec.web;

import com.example.ec.repo.FeatureVectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/feature-vectors/{selectedId}")
public class FeatureVectorController {

    FeatureVectorRepository repository;

    @Autowired
    public FeatureVectorController(FeatureVectorRepository repository) {
        this.repository = repository;
    }

    protected FeatureVectorController() {

    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Map<Integer, Integer> getRecommendations(@PathVariable(value = "selectedId") int tourId, @RequestBody FeatureVectorDto dto) {

        Map map = new HashMap<>();
        map.put(1,1);
        map.put(2,2);
        return map;

    }
}
