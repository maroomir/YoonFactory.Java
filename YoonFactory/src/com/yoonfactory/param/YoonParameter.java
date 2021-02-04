package com.yoonfactory.param;

import com.yoonfactory.file.YoonJson;

import java.lang.reflect.Type;
import java.nio.file.Paths;

public class YoonParameter {

    public IYoonParameter Parameter;
    public String RootDirectory;
    private Type m_pTypeParam;

    public Type getType() {
        return m_pTypeParam;
    }

    public YoonParameter() {
        Parameter = null;
        m_pTypeParam = Object.class;
        RootDirectory = Paths.get("", "YoonFactory").toString();
    }

    public YoonParameter(IYoonParameter pParam, Type pType) {
        Parameter.copyFrom(pParam);
        m_pTypeParam = pType;
        RootDirectory = Paths.get("", "YoonFactory").toString();
    }

    public void setParameter(IYoonParameter pParam, Type pType) {
        Parameter.copyFrom(pParam);
        m_pTypeParam = pType;
    }

    public boolean isEqual(YoonParameter pParam) {
        return pParam.Parameter.isEqual(Parameter) && pParam.getType() == m_pTypeParam && pParam.RootDirectory == RootDirectory;
    }

    public boolean saveParameter() {
        return saveParameter(m_pTypeParam.getTypeName());
    }

    public boolean saveParameter(String strFileName) {
        if (RootDirectory == "" || Parameter == null) return false;

        String strFilePath = Paths.get(RootDirectory, strFileName + ".json").toString();
        YoonJson pJson = new YoonJson(strFilePath);
        return pJson.saveFile(Parameter, m_pTypeParam);
    }

    public boolean loadParameter(boolean bSaveIfFalse) {
        return loadParameter(m_pTypeParam.getTypeName(), bSaveIfFalse);
    }

    public boolean loadParameter(String strFileName, boolean bSaveIfFalse) {
        if (RootDirectory == "" || Parameter == null) return false;

        String strFilePath = Paths.get(RootDirectory, strFileName + ".json").toString();
        IYoonParameter pParamBk = Parameter;
        YoonJson pJson = new YoonJson(strFilePath);
        Object pParam = pJson.loadFile(m_pTypeParam);
        if (pParam != null) {
            Parameter = (IYoonParameter) pParam;
            return true;
        }
        if (bSaveIfFalse) return saveParameter();
        return false;
    }
}
