package YoonFile;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

public class YoonJson implements IYoonFile {
    private String m_strFilePath;

    @Override
    public String GetFilePath() {
        return m_strFilePath;
    }

    public YoonJson(String strFilePath) {
        m_strFilePath = strFilePath;
    }

    @Override
    public void CopyFrom(IYoonFile pFile) {
        if (pFile instanceof YoonJson) {
            YoonJson pJson = (YoonJson) pFile;
            m_strFilePath = pJson.GetFilePath();
        }
    }

    @Override
    public IYoonFile Clone() {
        return new YoonJson(m_strFilePath);
    }

    @Override
    public boolean IsFileExist() {
        AtomicReference<String> refStrPath = new AtomicReference<>(m_strFilePath);
        return FileManagement.VerifyFileExtension(refStrPath, "json", false, false);
    }

    public Object LoadFile(Type pType) {
        if (!IsFileExist()) return null;
        try {
            Gson pGson = new Gson();
            FileReader pReader = new FileReader(m_strFilePath);
            return pGson.fromJson(pReader, pType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean SaveFile(Object pObject, Type pType) {
        if (!IsFileExist()) return false;
        try {
            Gson pGson = new Gson();
            String strJsonData = pGson.toJson(pObject, pType);
            return FileManagement.SetTextToFile(m_strFilePath, strJsonData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}