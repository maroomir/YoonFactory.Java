package YoonFile;

import YoonCommon.IYoonContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Paths;
import java.util.*;

public class YoonContainer implements IYoonContainer<String, YoonParameter> {
    private String m_strDirRoot;
    private List<String> m_pListKeyOrdered;
    private Map<String, YoonParameter> m_pMapObject;

    @Override
    public String getRootDirectory() {
        return m_strDirRoot;
    }

    @Override
    public void setRootDirectory(String strDir) {
        m_strDirRoot = strDir;
    }

    @Override
    public void copyFrom(IYoonContainer pContainer) {
        if (pContainer instanceof YoonContainer) {
            YoonContainer pParamContainer = (YoonContainer) pContainer;
            m_pMapObject.clear();
            m_strDirRoot = pParamContainer.m_strDirRoot;
            m_pMapObject = new HashMap<>(pParamContainer);
        }
    }

    @Override
    public IYoonContainer clone() {
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
    public int size() {
        return m_pMapObject.size();
    }

    @Override
    public boolean isEmpty() {
        return m_pMapObject.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return m_pMapObject.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return m_pMapObject.containsValue(value);
    }

    @Override
    public YoonParameter get(Object key) {
        return m_pMapObject.get(key);
    }

    @Nullable
    @Override
    public YoonParameter put(String key, YoonParameter value) {
        return m_pMapObject.put(key, value);
    }

    @Override
    public YoonParameter remove(Object key) {
        return m_pMapObject.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends YoonParameter> m) {
        m_pMapObject.putAll(m);
    }

    @Override
    public void clear() {
        m_pMapObject.clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return m_pMapObject.keySet();
    }

    @NotNull
    @Override
    public Collection<YoonParameter> values() {
        return m_pMapObject.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, YoonParameter>> entrySet() {
        return m_pMapObject.entrySet();
    }

    @Override
    public boolean loadValue(String strKey) {
        if (m_strDirRoot == "" || strKey == "")
            return false;
        YoonParameter pParam = m_pMapObject.get(strKey);
        pParam.RootDirectory = m_strDirRoot;
        if (!m_pMapObject.containsKey(strKey))
            m_pMapObject.put(strKey, new YoonParameter());
        if (pParam.loadParameter(strKey, true)) {
            m_pMapObject.replace(strKey, pParam);
            return true;
        } else return false;
    }

    @Override
    public boolean saveValue(String strKey) {
        if (m_strDirRoot == "" || strKey == "" || !m_pMapObject.containsKey(strKey))
            return false;
        YoonParameter pParam = m_pMapObject.get(strKey);
        pParam.RootDirectory = m_strDirRoot;
        return pParam.saveParameter(strKey);
    }
}