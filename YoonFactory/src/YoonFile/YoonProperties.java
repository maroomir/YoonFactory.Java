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
    public String GetFilePath() {
        return m_strFilePath;
    }

    public YoonProperties(String strFilePath) {
        m_strFilePath = strFilePath;
    }

    @Override
    public void CopyFrom(IYoonFile pFile) {
        if (pFile instanceof YoonProperties) {
            YoonProperties pProperties = (YoonProperties) pFile;
            m_strFilePath = pProperties.GetFilePath();
        }
    }

    @Override
    public IYoonFile Clone() {
        return new YoonProperties(m_strFilePath);
    }

    @Override
    public boolean IsFileExist() {
        AtomicReference<String> refStrPath = new AtomicReference<>(m_strFilePath);
        return FileFactory.VerifyFileExtension(refStrPath, "properties", false, false);
    }

    public String GetValue(String strKey) {
        if (!IsFileExist()) return null;
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

    public boolean SetValue(String strKey, String strValue) {
        if (!IsFileExist()) return false;
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