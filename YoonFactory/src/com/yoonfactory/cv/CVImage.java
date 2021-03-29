package com.yoonfactory.cv;

import com.yoonfactory.image.YoonImage;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import javax.naming.OperationNotSupportedException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class CVImage extends YoonImage {

    public CVImage(Mat pMat) throws OperationNotSupportedException {
        if (!fromMat(pMat))
            throw new OperationNotSupportedException();
    }

    public Mat copyMat() {
        return toMat().clone();
    }

    public Mat toMat() {
        byte[] pBuffer = ((DataBufferByte) m_pImage.getRaster().getDataBuffer()).getData();
        int nTypeMat = CvType.CV_8U;
        switch (m_pImage.getType()) {
            case BufferedImage.TYPE_BYTE_GRAY:
            case BufferedImage.TYPE_BYTE_INDEXED:
            case BufferedImage.TYPE_BYTE_BINARY:
                nTypeMat = CvType.CV_8U;
                break;
            case BufferedImage.TYPE_USHORT_GRAY:
            case BufferedImage.TYPE_USHORT_555_RGB:
            case BufferedImage.TYPE_USHORT_565_RGB:
                nTypeMat = CvType.CV_16U;
                break;
            case BufferedImage.TYPE_3BYTE_BGR:
                nTypeMat = CvType.CV_8UC3;
                break;
            case BufferedImage.TYPE_4BYTE_ABGR:
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
                nTypeMat = CvType.CV_8UC4;
                break;
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_ARGB_PRE:
            case BufferedImage.TYPE_INT_BGR:
            case BufferedImage.TYPE_INT_RGB:
                nTypeMat = CvType.CV_32SC1;
                break;
        }
        return new Mat(getWidth(), getHeight(), nTypeMat, ByteBuffer.wrap(pBuffer));
    }

    public boolean fromMat(Mat pMat) {
        try {
            MatOfByte pBufferMat = new MatOfByte();
            Imgcodecs.imencode(".jpg", pMat, pBufferMat); // Encoding
            byte[] pBuffer = pBufferMat.toArray();
            InputStream pStream = new ByteArrayInputStream(pBuffer);
            m_pImage = ImageIO.read(pStream);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
