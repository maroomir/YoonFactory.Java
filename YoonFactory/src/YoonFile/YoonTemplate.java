package YoonFile;

import YoonCommon.IYoonContainer;
import YoonCommon.IYoonTemplate;

import java.nio.file.Paths;
import java.util.Map;

public class YoonTemplate implements IYoonTemplate {

    public int No;
    public String Name;
    public String RootDirectory;
    private IYoonContainer m_pContainer;

    public YoonTemplate() {
        No = 0;
        Name = "Default";
        RootDirectory = Paths.get("", "YoonFactory").toString();
        m_pContainer = new YoonContainer();
    }

    @Override
    public void CopyFrom(IYoonTemplate pTemplate) {
        if (pTemplate instanceof YoonTemplate) {
            YoonTemplate pTempOrigin = (YoonTemplate) pTemplate;
            No = pTempOrigin.No;
            Name = pTempOrigin.Name;
            RootDirectory = pTempOrigin.RootDirectory;
            m_pContainer = pTempOrigin.GetContainer().Clone();
        }
    }

    @Override
    public IYoonTemplate Clone() {
        YoonTemplate pTemplate = new YoonTemplate();
        pTemplate.No = No;
        pTemplate.Name = Name;
        pTemplate.RootDirectory = RootDirectory;
        pTemplate.m_pContainer = (YoonContainer) m_pContainer.Clone();
        return pTemplate;
    }

    @Override
    public boolean LoadTemplate() {
        if (RootDirectory == "" || m_pContainer == null)
            return false;
        boolean bResult = true;
        String strSection = String.format("%d_%s", No, Name);
        String strIniFilePath = Paths.get(RootDirectory, "YoonTemplate.ini").toString();
        m_pContainer.SetRootDirectory(Paths.get(RootDirectory, strSection).toString());
        //// Load the template information for Ini files
        YoonIni pIni = new YoonIni(strIniFilePath);
        int nCount = Integer.parseInt(pIni.GetValue(strSection, "Count"));
        for (int iParam = 0; iParam < nCount; iParam++) {
            String strKey = pIni.GetValue(strSection, Integer.toString(iParam));
            if (!m_pContainer.LoadValue(strKey))
                bResult = false;
        }
        return bResult;
    }

    @Override
    public IYoonContainer GetContainer() {
        return m_pContainer;
    }

    @Override
    public void SetContainer(IYoonContainer pContainer) {
        m_pContainer = pContainer;
    }

    @Override
    public boolean SaveTemplate() {
        if (RootDirectory == "" || m_pContainer == null)
            return false;

        boolean bResult = true;
        int iParam = 0;
        String strSection = String.format("%d_%s", No, Name);
        String strIniFilePath = Paths.get(RootDirectory, "YoonTemplate.ini").toString();
        m_pContainer.SetRootDirectory(Paths.get(RootDirectory, strSection).toString());
        //// Save the Information of templates to Ini files
        YoonIni pIni = new YoonIni(strIniFilePath);
        Map<String, YoonParameter> pMap = m_pContainer.GetObjectMap();
        pIni.SetValue(strSection, "Count", Integer.toString(pMap.size()));
        for (String strKey : pMap.keySet()) {
            pIni.SetValue(strSection, Integer.toString(iParam), strKey);
            if (!m_pContainer.SaveValue(strKey))
                bResult = false;
            iParam++;
        }
        return bResult;
    }
}