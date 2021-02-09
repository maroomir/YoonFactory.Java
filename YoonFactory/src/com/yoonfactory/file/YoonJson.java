package com.yoonfactory.file;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

public class YoonJson implements IYoonFile {
    private String m_strFilePath;

    @Override
    public String getFilePath() {
        return m_strFilePath;
    }

    public YoonJson(String strFilePath) {
        m_strFilePath = strFilePath;
    }

    @Override
    public void copyFrom(IYoonFile pFile) {
        if (pFile instanceof YoonJson) {
            YoonJson pJson = (YoonJson) pFile;
            m_strFilePath = pJson.getFilePath();
        }
    }

    @Override
    public IYoonFile clone() {
        return new YoonJson(m_strFilePath);
    }

    @Override
    public boolean isFileExist() {
        AtomicReference<String> refStrPath = new AtomicReference<>(m_strFilePath);
        return FileFactory.verifyFileExtension(refStrPath, "json", false, false);
    }

    public Object loadFile(Type pType) throws FileNotFoundException {
        if (!isFileExist()) throw new FileNotFoundException();
        try {
            Gson pGson = new Gson();
            FileReader pReader = new FileReader(m_strFilePath);
            return pGson.fromJson(pReader, pType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveFile(Object pObject, Type pType) {
        try {
            Gson pGson = new Gson();
            String strJsonData = pGson.toJson(pObject, pType);
            return FileFactory.setTextToFile(m_strFilePath, strJsonData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}