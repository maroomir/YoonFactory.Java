package YoonFile;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class YoonIni implements IYoonFile {
    private String m_strFilePath;

    @Override
    public String GetFilePath() {
        return m_strFilePath;
    }

    public YoonIni(String strFilePath) {
        m_strFilePath = strFilePath;
    }

    @Override
    public void CopyFrom(IYoonFile pFile) {
        if (pFile instanceof YoonIni) {
            YoonIni pIni = (YoonIni) pFile;
            m_strFilePath = pIni.GetFilePath();
        }
    }

    @Override
    public IYoonFile Clone() {
        return new YoonIni(m_strFilePath);
    }

    @Override
    public boolean IsFileExist() {
        AtomicReference<String> refStrPath = new AtomicReference<>(m_strFilePath);
        return FileManagement.VerifyFileExtension(refStrPath, "ini", false, false);
    }

    public String GetValue(String strSection, String strKey) {
        if (!IsFileExist()) return null;
        try {
            Ini pIni = new Ini(new File(m_strFilePath));
            return pIni.get(strSection, strKey);
        } catch (InvalidFileFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean SetValue(String strSection, String strKey, String strValue) {
        if (!IsFileExist()) return false;
        try {
            Wini pIni = new Wini(new File(m_strFilePath));
            pIni.put(strSection, strKey, strValue);
            pIni.store();
            return true;
        } catch (InvalidFileFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}