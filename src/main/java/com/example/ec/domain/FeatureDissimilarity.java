package com.example.ec.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Objects;

@Entity
public class FeatureDissimilarity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private ArrayList<String> disimilarIds;

    public FeatureDissimilarity()
    {}

    public FeatureDissimilarity(ArrayList<String> disimilarIds) {
        this.disimilarIds = disimilarIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<String> getDissimilarIds() {
        return disimilarIds;
    }

    public void setDissimilarIds(ArrayList<String> disimilarIds) {
        this.disimilarIds = disimilarIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeatureDissimilarity that = (FeatureDissimilarity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
