package com.yoonfactory.image;

import com.yoonfactory.file.FileFactory;
import com.yoonfactory.file.IYoonFile;

import javax.imageio.ImageIO;
import javax.naming.OperationNotSupportedException;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

public class YoonImage implements IYoonFile {
    public final int DEFAULT_WIDTH = 640;
    public final int DEFAULT_HEIGHT = 480;
    private String m_strFilePath = "";
    private BufferedImage m_pImage = null;

    @Override
    public String getFilePath() {
        return m_strFilePath;
    }

    public void setFilePath(String strFilePath) {
        this.m_strFilePath = strFilePath;
    }

    private boolean isReadable(String strImagePath) {
        AtomicReference<String> refStrPath = new AtomicReference<>(strImagePath);
        for (String strExt : ImageIO.getReaderFormatNames()) {
            if (FileFactory.verifyFileExtension(refStrPath, strExt, false, false))
                return true;
        }
        return false;
    }

    private boolean isWritable(String strImagePath, AtomicReference<String> refStrExt) {
        AtomicReference<String> refStrPath = new AtomicReference<>(strImagePath);
        for (String strExt : ImageIO.getWriterFormatNames()) {
            if (FileFactory.verifyFileExtension(refStrPath, strExt, false, false)) {
                refStrExt.set(strExt);
                return true;
            }
        }
        return false;
    }

    public YoonImage() {
        m_pImage = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
    }

    public YoonImage(int nWidth, int nHeight, int nPlane) {
        switch (nPlane) {
            case 1:
                m_pImage = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_BYTE_GRAY);
            case 2:
                m_pImage = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_USHORT_GRAY);
            case 3:
                m_pImage = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_3BYTE_BGR);
            case 4:
                m_pImage = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_INT_ARGB);
            default:
                m_pImage = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
        }
    }

    public YoonImage(byte[] pBuffer) throws OperationNotSupportedException {
        if (!fromByteArray(pBuffer))
            throw new OperationNotSupportedException();
    }

    public YoonImage(int[] pBuffer, int nWidth, int nHeight) throws  OperationNotSupportedException {
        if (!fromIntegerArray(pBuffer, nWidth, nHeight))
            throw new OperationNotSupportedException();
    }

    public int getWidth() {
        return m_pImage.getWidth();
    }

    public int getHeight() {
        return m_pImage.getHeight();
    }

    public int getPlane() {
        switch (m_pImage.getType()) {
            case BufferedImage.TYPE_BYTE_GRAY:
            case BufferedImage.TYPE_BYTE_BINARY:
            case BufferedImage.TYPE_BYTE_INDEXED:
                return 1;
            case BufferedImage.TYPE_USHORT_GRAY:
            case BufferedImage.TYPE_USHORT_555_RGB:
            case BufferedImage.TYPE_USHORT_565_RGB:
                return 2;
            case BufferedImage.TYPE_3BYTE_BGR:
                return 3;
            case BufferedImage.TYPE_4BYTE_ABGR:
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_ARGB_PRE:
            case BufferedImage.TYPE_INT_BGR:
            case BufferedImage.TYPE_INT_RGB:
                return 4;
            default:
                return 0;
        }
    }

    @Override
    public void copyFrom(IYoonFile pFile) {
        if (pFile instanceof YoonImage) {
            YoonImage pImage = (YoonImage) pFile;
            m_strFilePath = pImage.getFilePath();
            m_pImage = pImage.copyImage();
        }
    }

    @Override
    public boolean isFileExist() {
        AtomicReference<String> refStrPath = new AtomicReference<>(new String());
        return (isReadable(m_strFilePath) && isWritable(m_strFilePath, refStrPath));
    }

    @Override
    public boolean loadFile() {
        return loadImage(m_strFilePath);
    }

    @Override
    public boolean saveFile() {
        return saveImage(m_strFilePath);
    }

    public boolean loadImage(String strImagePath) {
        if (!isReadable(strImagePath)) return false;
        try {
            m_strFilePath = strImagePath;
            File pFileSource = new File(strImagePath);
            m_pImage = ImageIO.read(pFileSource);
            if (m_pImage != null) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loadImageFromUrl(String strURL) {
        try {
            URL pLinkSource = new URL(strURL);
            m_pImage = ImageIO.read(pLinkSource);
            if (m_pImage != null) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveImage(String strImagePath) {
        if (m_pImage == null) return false;
        AtomicReference<String> refStrPath = new AtomicReference<>(new String());
        if (!isWritable(strImagePath, refStrPath)) return false;
        try {
            m_strFilePath = strImagePath;
            File pFileObject = new File(strImagePath);
            ImageIO.write(m_pImage, refStrPath.get(), pFileObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public BufferedImage copyImage() {
        ColorModel pModel = m_pImage.getColorModel();
        boolean bAlphaPre = pModel.isAlphaPremultiplied();
        WritableRaster pRaster = m_pImage.copyData(null);
        return new BufferedImage(pModel, pRaster, bAlphaPre, null);
    }

    public byte[] toByteArray() throws IOException, NullPointerException {
        if (m_pImage == null) throw new NullPointerException();
        ByteArrayOutputStream pStream = new ByteArrayOutputStream();
        ImageIO.write(m_pImage, "PNG", pStream);
        return pStream.toByteArray();
    }

    public short[] toShortArray() throws IOException, NullPointerException {
        if (m_pImage == null) throw new NullPointerException();
        if (m_pImage.getType() == BufferedImage.TYPE_USHORT_GRAY || m_pImage.getType() == BufferedImage.TYPE_USHORT_555_RGB ||
                m_pImage.getType() == BufferedImage.TYPE_USHORT_565_RGB) {
            short[] pResult = ((DataBufferShort) m_pImage.getRaster().getDataBuffer()).getData();
            return pResult;
        }
        throw new IOException();
    }

    public int[] toIntegerArray() throws IOException, NullPointerException {
        if (m_pImage == null) throw new NullPointerException();
        if (m_pImage.getType() == BufferedImage.TYPE_INT_RGB || m_pImage.getType() == BufferedImage.TYPE_INT_BGR ||
                m_pImage.getType() == BufferedImage.TYPE_INT_ARGB || m_pImage.getType() == BufferedImage.TYPE_INT_ARGB_PRE) {
            int[] pResult = ((DataBufferInt) m_pImage.getRaster().getDataBuffer()).getData();
            return pResult;
        }
        throw new IOException();
    }

    public boolean fromByteArray(byte[] pArray) {
        try {
            InputStream pStream = new ByteArrayInputStream(pArray);
            m_pImage = ImageIO.read(pStream);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean fromIntegerArray(int[] pArray, int nWidth, int nHeight) {
        try {
            int[] pBitMasks = new int[]{0xFF0000, 0xFF00, 0xFF, 0xFF000000}; // RGBA (3, 2, 1, 4)
            SinglePixelPackedSampleModel pModel = new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT, nWidth, nHeight, pBitMasks);
            DataBufferInt pBuffer = new DataBufferInt(pArray, pArray.length);
            WritableRaster pWritableRaster = Raster.createWritableRaster(pModel, pBuffer, new Point());
            m_pImage = new BufferedImage(ColorModel.getRGBdefault(), pWritableRaster, false, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}