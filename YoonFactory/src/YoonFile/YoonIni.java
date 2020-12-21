package YoonFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class YoonIni implements IYoonProperties {
    private String m_strFilePath;

    @Override
    public String GetFilePath() {
        return m_strFilePath;
    }

    public YoonIni(String strFilePath) {
        m_strFilePath = strFilePath;
    }

    @Override
    public void CopyFrom(IYoonProperties pFile) {
        if (pFile instanceof YoonIni) {
            YoonIni pIni = (YoonIni) pFile;
            m_strFilePath = pIni.GetFilePath();
        }
    }

    @Override
    public IYoonProperties Clone() {
        return new YoonIni(m_strFilePath);
    }

    @Override
    public boolean IsFileExist() {
        AtomicReference<String> refStrPath = new AtomicReference<>(m_strFilePath);
        return FileManagement.VerifyFileExtension(refStrPath, "ini", true, true);
    }

    @Override
    public String GetValue(String strKey) {
        if (!IsFileExist()) return null;
        try {
            Properties pPropIni = new Properties();
            pPropIni.load(new FileInputStream(m_strFilePath));
            return pPropIni.getProperty(strKey);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean SetValue(String strKey, String strValue) {
        if (!IsFileExist()) return false;
        try {
            Properties pPropIni = new Properties();
            pPropIni.setProperty(strKey, strValue);
            pPropIni.store(new FileOutputStream(m_strFilePath), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
