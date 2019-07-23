package com.example.ec.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Objects;

@Entity
public class FeatureSimilarity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private ArrayList<String> similarIds;

    public FeatureSimilarity()
    {}

    public FeatureSimilarity(ArrayList<String> similarIds) {
        this.similarIds = similarIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<String> getSimilarIds() {
        return similarIds;
    }

    public void setSimilarIds(ArrayList<String> similarIds) {
        this.similarIds = similarIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeatureSimilarity that = (FeatureSimilarity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
