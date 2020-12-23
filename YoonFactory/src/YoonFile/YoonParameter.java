package YoonFile;

import YoonCommon.IYoonParameter;

import java.lang.reflect.Type;
import java.nio.file.Paths;

public class YoonParameter {

    public IYoonParameter Parameter;
    public String RootDirectory;
    private Type m_pTypeParam;

    public Type GetType() {
        return m_pTypeParam;
    }

    public YoonParameter() {
        Parameter = null;
        m_pTypeParam = Object.class;
        RootDirectory = Paths.get("", "YoonFactory").toString();
    }

    public YoonParameter(IYoonParameter pParam, Type pType) {
        Parameter = pParam.Clone();
        m_pTypeParam = pType;
        RootDirectory = Paths.get("", "YoonFactory").toString();
    }

    public void SetParameter(IYoonParameter pParam, Type pType) {
        Parameter = pParam.Clone();
        m_pTypeParam = pType;
    }

    public boolean IsEqual(YoonParameter pParam) {
        if (pParam.Parameter.IsEqual(Parameter) && pParam.GetType() == m_pTypeParam && pParam.RootDirectory == RootDirectory)
            return true;
        else return false;
    }

    public boolean SaveParameter() {
        return SaveParameter(m_pTypeParam.getTypeName());
    }

    public boolean SaveParameter(String strFileName) {
        if (RootDirectory == "" || Parameter == null) return false;

        String strFilePath = Paths.get(RootDirectory, strFileName + ".json").toString();
        YoonJson pJson = new YoonJson(strFilePath);
        if (pJson.SaveFile(Parameter, m_pTypeParam))
            return true;
        else
            return false;
    }

    public boolean LoadParameter(boolean bSaveIfFalse) {
        return LoadParameter(m_pTypeParam.getTypeName(), bSaveIfFalse);
    }

    public boolean LoadParameter(String strFileName, boolean bSaveIfFalse) {
        if (RootDirectory == "" || Parameter == null) return false;

        String strFilePath = Paths.get(RootDirectory, strFileName + ".json").toString();
        IYoonParameter pParamBk = Parameter.Clone();
        YoonJson pJson = new YoonJson(strFilePath);
        Object pParam = pJson.LoadFile(m_pTypeParam);
        if (pParam != null) {
            Parameter = (IYoonParameter) pParam;
            return true;
        }
        if (bSaveIfFalse) return SaveParameter();
        return false;
    }
}
