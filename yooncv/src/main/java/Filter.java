package com.yoonfactory.cv;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.naming.OperationNotSupportedException;

public class Filter {
    public static CVImage sobel(CVImage pSourceImage, int nOrderX, int nOrderY) throws OperationNotSupportedException {
        return new CVImage(sobel(pSourceImage.toMatrix(), nOrderX, nOrderY));
    }

    public static Mat sobel(Mat pSourceMatrix, int nOrderX, int nOrderY) {
        Mat pResultMatrix = new Mat();
        Imgproc.Sobel(pSourceMatrix, pResultMatrix, -1, nOrderX, nOrderY);
        return pResultMatrix;
    }

    public static CVImage scharr(CVImage pSourceImage, int nOrderX, int nOrderY) throws OperationNotSupportedException {
        return new CVImage(scharr(pSourceImage.toMatrix(), nOrderX, nOrderY));
    }

    public static Mat scharr(Mat pSourceMatrix, int nOrderX, int nOrderY) {
        Mat pResultMatrix = new Mat();
        Imgproc.Scharr(pSourceMatrix, pResultMatrix, -1, nOrderX, nOrderY);
        return pResultMatrix;
    }

    public static CVImage laplacian(CVImage pSourceImage) throws OperationNotSupportedException {
        return new CVImage(laplacian(pSourceImage.toMatrix()));
    }

    public static Mat laplacian(Mat pSourceMatrix) {
        Mat pResultMatrix = new Mat();
        Imgproc.Laplacian(pSourceMatrix, pResultMatrix, -1);
        return pResultMatrix;
    }

    public static CVImage gaussian(CVImage pSourceImage, int nSizeX, int nSizeY) throws OperationNotSupportedException {
        return new CVImage(gaussian(pSourceImage.toMatrix(), nSizeX, nSizeY));
    }

    public static Mat gaussian(Mat pSourceMatrix, int nSizeX, int nSizeY) {
        Mat pResultMatrix = new Mat();
        Imgproc.GaussianBlur(pResultMatrix, pResultMatrix, new Size(nSizeX, nSizeY), 1);
        return pResultMatrix;
    }

    public static CVImage canny(CVImage pSourceImage, double dThresholdMin, double dThresholdMax) throws OperationNotSupportedException {
        return new CVImage(canny(pSourceImage.toMatrix(), dThresholdMin, dThresholdMax));
    }

    public static Mat canny(Mat pSourceMatrix, double dThresholdMin, double dThresholdMax) {
        Mat pResultMatrix = new Mat();
        Imgproc.Canny(pSourceMatrix, pResultMatrix, dThresholdMin, dThresholdMax);
        return pResultMatrix;
    }
}