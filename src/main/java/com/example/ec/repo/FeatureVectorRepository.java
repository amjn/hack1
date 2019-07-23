package com.example.ec.repo;

import com.example.ec.domain.FeatureVectorWithType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource()
public interface FeatureVectorRepository extends CrudRepository<FeatureVectorWithType, Long> {
}
