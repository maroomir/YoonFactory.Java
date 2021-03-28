package com.yoonfactory.imagej;

import com.yoonfactory.image.YoonImage;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class IJImage extends YoonImage {

    public IJImage(ImageProcessor pProcessor){
        m_pImage = pProcessor.getBufferedImage();
    }

    public IJImage(ImagePlus pImage) {
        m_pImage = pImage.getBufferedImage();
    }

    @Override
    public boolean loadImage(String strImagePath) {
        m_strFilePath = strImagePath;
        ImagePlus pIJ = IJ.openImage(strImagePath);
        m_pImage = pIJ.getBufferedImage();
        if (m_pImage != null) return true;
        else return false;
    }

    @Override
    public boolean saveImage(String strImagePath) {
        ImagePlus pIJ = toIJImage("");
        IJ.save(pIJ, strImagePath);
        return true;
    }

    public ImageProcessor toIJProcessor() {
        return new ByteProcessor(m_pImage);
    }

    public ImagePlus toIJImage(String strTag) {
        ImageProcessor pProcessor = new ByteProcessor(m_pImage);
        return new ImagePlus(strTag, pProcessor);
    }

    public void fromIJProcessor(ImageProcessor pProcessor) {
        m_pImage = pProcessor.getBufferedImage();
    }

    public void fromIJImage(ImagePlus pImage) {
        m_pImage = pImage.getBufferedImage();
    }
}