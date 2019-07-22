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
    private Long id;

    @Column
    private Long[] featureVector;

    @Column
    private Long type;

    public FeatureVector() {
    }

    public FeatureVector(Long id, Long[] featureVector, Long type) {
        this.id = id;
        this.featureVector = featureVector;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long[] getFeatureVector() {
        return featureVector;
    }

    public void setFeatureVector(Long[] featureVector) {
        this.featureVector = featureVector;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
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
