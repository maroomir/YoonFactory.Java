package com.yoonfactory.imagej;

import ij.process.ImageProcessor;

public class Filter {
    public static IJImage gaussian(IJImage pImage, double dSigma){
        ImageProcessor pProcessor = pImage.toIJProcessor();
        pProcessor.blurGaussian(dSigma);
        return new IJImage(pProcessor);
    }

    public static IJImage sharpen(IJImage pImage){
        ImageProcessor pProcessor = pImage.toIJProcessor();
        pProcessor.sharpen();
        return new IJImage(pProcessor);
    }

    public static IJImage median(IJImage pImage) {
        ImageProcessor pProcessor = pImage.toIJProcessor();
        pProcessor.medianFilter();
        return new IJImage(pProcessor);
    }
}
