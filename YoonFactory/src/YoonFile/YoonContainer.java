package YoonFile;

import YoonCommon.IYoonContainer;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class YoonContainer implements IYoonContainer<YoonParameter> {
    private String m_strDirRoot;
    private Map<String, YoonParameter> m_pMapObject;

    @Override
    public String GetRootDirectory() {
        return m_strDirRoot;
    }

    @Override
    public void SetRootDirectory(String strDir) {
        m_strDirRoot = strDir;
    }

    @Override
    public void CopyFrom(IYoonContainer pContainer) {
        if (pContainer instanceof YoonContainer) {
            YoonContainer pParamContainer = (YoonContainer) pContainer;
            m_pMapObject.clear();
            m_strDirRoot = pParamContainer.m_strDirRoot;
            m_pMapObject = new HashMap<>(pParamContainer.GetObjectMap());
        }
    }

    @Override
    public IYoonContainer Clone() {
        YoonContainer pContainer = new YoonContainer(m_pMapObject);
        pContainer.m_strDirRoot = m_strDirRoot;
        return pContainer;
    }

    public YoonContainer() {
        m_pMapObject = new HashMap<String, YoonParameter>();
        m_strDirRoot = Paths.get("", "YoonFactory").toString();
    }

    public YoonContainer(Map<String, YoonParameter> pMap) {
        m_pMapObject = new HashMap<>(pMap);
        m_strDirRoot = Paths.get("", "YoonFactory").toString();
    }

    @Override
    public void Clear() {
        m_pMapObject.clear();
    }

    @Override
    public boolean LoadValue(String strKey) {
        if (m_strDirRoot == "" || strKey == "")
            return false;
        YoonParameter pParam = m_pMapObject.get(strKey);
        pParam.RootDirectory = m_strDirRoot;
        if (!m_pMapObject.containsKey(strKey))
            m_pMapObject.put(strKey, new YoonParameter());
        if (pParam.LoadParameter(strKey, true)) {
            m_pMapObject.replace(strKey, pParam);
            return true;
        } else return false;
    }

    @Override
    public boolean SaveValue(String strKey) {
        if (m_strDirRoot == "" || strKey == "" || !m_pMapObject.containsKey(strKey))
            return false;
        YoonParameter pParam = m_pMapObject.get(strKey);
        pParam.RootDirectory = m_strDirRoot;
        return pParam.SaveParameter(strKey);
    }

    @Override
    public Map<String, YoonParameter> GetObjectMap() {
        return m_pMapObject;
    }

    @Override
    public boolean Add(String strKey, YoonParameter pValue) {
        try {
            m_pMapObject.put(strKey, pValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean Remove(String strKey) {
        return m_pMapObject.remove(strKey, m_pMapObject.get(strKey));
    }

    @Override
    public String GetKey(YoonParameter pParam) {
        for (String strKey : m_pMapObject.keySet()) {
            if (pParam.Parameter.IsEqual(m_pMapObject.get(strKey).Parameter))
                return strKey;
        }
        return "";
    }

    @Override
    public YoonParameter GetValue(String strKey) {
        if (m_pMapObject.containsKey(strKey))
            return m_pMapObject.get(strKey);
        else
            return null;
    }

    @Override
    public void SetValue(String strKey, YoonParameter pValue) {
        if (m_pMapObject.containsKey(strKey))
            m_pMapObject.replace(strKey, pValue);
        else
            m_pMapObject.put(strKey, pValue);
    }
}