package com.example.ec.repo;

import com.example.ec.domain.FeatureVector;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource()
public interface FeatureVectorRepository extends CrudRepository<FeatureVector, Integer> {
}
