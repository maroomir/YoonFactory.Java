package YoonFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class YoonProperties implements IYoonFile {
    private String m_strFilePath;

    @Override
    public String getFilePath() {
        return m_strFilePath;
    }

    public YoonProperties(String strFilePath) {
        m_strFilePath = strFilePath;
    }

    @Override
    public void copyFrom(IYoonFile pFile) {
        if (pFile instanceof YoonProperties) {
            YoonProperties pProperties = (YoonProperties) pFile;
            m_strFilePath = pProperties.getFilePath();
        }
    }

    @Override
    public IYoonFile clone() {
        return new YoonProperties(m_strFilePath);
    }

    @Override
    public boolean isFileExist() {
        AtomicReference<String> refStrPath = new AtomicReference<>(m_strFilePath);
        return FileFactory.verifyFileExtension(refStrPath, "properties", false, false);
    }

    public String getValue(String strKey) {
        if (!isFileExist()) return null;
        try {
            Properties pProp = new Properties();
            pProp.load(new FileInputStream(m_strFilePath));
            return pProp.getProperty(strKey);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean setValue(String strKey, String strValue) {
        if (!isFileExist()) return false;
        try {
            Properties pProp = new Properties();
            pProp.setProperty(strKey, strValue);
            pProp.store(new FileOutputStream(m_strFilePath), null);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}