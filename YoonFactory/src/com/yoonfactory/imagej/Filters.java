package com.yoonfactory.imagej;

import ij.process.ImageProcessor;

public class Filters {
    public static IJImage GaussianFilter(IJImage pImage, double dSigma){
        ImageProcessor pProcessor = pImage.toIJProcessor();
        pProcessor.blurGaussian(dSigma);
        return new IJImage(pProcessor);
    }
}
