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
    private ArrayList<Long> similarIds;

    public FeatureSimilarity()
    {}

    public FeatureSimilarity(Long id, ArrayList<Long> similarIds) {
        this.id = id;
        this.similarIds = similarIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<Long> getSimilarIds() {
        return similarIds;
    }

    public void setSimilarIds(ArrayList<Long> similarIds) {
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
