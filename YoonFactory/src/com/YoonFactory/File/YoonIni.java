package com.yoonfactory.file;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class YoonIni implements IYoonFile {
    private String m_strFilePath;

    @Override
    public String getFilePath() {
        return m_strFilePath;
    }

    public YoonIni(String strFilePath) {
        m_strFilePath = strFilePath;
    }

    @Override
    public void copyFrom(IYoonFile pFile) {
        if (pFile instanceof YoonIni) {
            YoonIni pIni = (YoonIni) pFile;
            m_strFilePath = pIni.getFilePath();
        }
    }

    @Override
    public IYoonFile clone() {
        return new YoonIni(m_strFilePath);
    }

    @Override
    public boolean isFileExist() {
        AtomicReference<String> refStrPath = new AtomicReference<>(m_strFilePath);
        return FileFactory.verifyFileExtension(refStrPath, "ini", false, false);
    }

    public Object getValue(String strSection, String strKey) {
        if (!isFileExist()) return null;
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

    public boolean setValue(String strSection, String strKey, Object value) {
        if (!isFileExist()) return false;
        try {
            Wini pIni = new Wini(new File(m_strFilePath));
            pIni.put(strSection, strKey, value);
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