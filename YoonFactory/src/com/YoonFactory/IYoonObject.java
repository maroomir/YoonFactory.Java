package com.yoonfactory;

public interface IYoonObject {
    int getLabelNo();

    void setLabelNo(int nNo);

    double getScore();

    void setScore(double dScore);

    void copyFrom(IYoonObject pObject);
}