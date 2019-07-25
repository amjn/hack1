package com.example.ec.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class FeatureVectorWithType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double[] featureVector;

    @Column
    private Long type;

    public FeatureVectorWithType() {
    }

    public FeatureVectorWithType(double[] featureVector, Long type) {
        this.featureVector = featureVector;
        this.type = type;
    }

    public double[] getFeatureVector() {
        return featureVector;
    }

    public void setFeatureVector(double[] featureVector) {
        this.featureVector = featureVector;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        FeatureVector that = (FeatureVector) o;
//        return id.equals(that.id) &&
//                Objects.equals(featureVector, that.featureVector);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
}
