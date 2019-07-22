package com.example.ec.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class FeatureVector {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String featureVector;

    public FeatureVector(Integer id, String featureVector) {
        this.id = id;
        this.featureVector = featureVector;
    }

    public FeatureVector() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFeatureVector() {
        return featureVector;
    }

    public void setFeatureVector(String featureVector) {
        this.featureVector = featureVector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeatureVector that = (FeatureVector) o;
        return id.equals(that.id) &&
                Objects.equals(featureVector, that.featureVector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
