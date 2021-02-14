package com.yoonfactory.image;

import com.yoonfactory.file.FileFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

public class YoonImage {
    private BufferedImage m_pImage = null;

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

    public boolean loadImageFromPath(String strImagePath) {
        if (!isReadable(strImagePath)) return false;
        try {
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

    public boolean saveImageToPath(String strImagePath) {
        if (m_pImage == null) return false;
        AtomicReference<String> refStrPath = new AtomicReference<>(new String());
        if (!isWritable(strImagePath, refStrPath)) return false;
        try {
            File pFileObject = new File(strImagePath);
            ImageIO.write(m_pImage, refStrPath.get(), pFileObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
