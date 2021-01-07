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
    public void copyFrom(IYoonTemplate pTemplate) {
        if (pTemplate instanceof YoonTemplate) {
            YoonTemplate pTempOrigin = (YoonTemplate) pTemplate;
            No = pTempOrigin.No;
            Name = pTempOrigin.Name;
            RootDirectory = pTempOrigin.RootDirectory;
            m_pContainer.copyFrom(pTempOrigin.getContainer());
        }
    }

    @Override
    public IYoonTemplate clone() {
        YoonTemplate pTemplate = new YoonTemplate();
        pTemplate.No = No;
        pTemplate.Name = Name;
        pTemplate.RootDirectory = RootDirectory;
        pTemplate.m_pContainer.copyFrom(m_pContainer);
        return pTemplate;
    }

    @Override
    public boolean loadTemplate() {
        if (RootDirectory == "" || m_pContainer == null)
            return false;
        boolean bResult = true;
        String strSection = String.format("%d_%s", No, Name);
        String strIniFilePath = Paths.get(RootDirectory, "YoonTemplate.ini").toString();
        m_pContainer.setRootDirectory(Paths.get(RootDirectory, strSection).toString());
        //// Load the template information for Ini files
        YoonIni pIni = new YoonIni(strIniFilePath);
        int nCount = Integer.parseInt(pIni.getValue(strSection, "Count"));
        for (int iParam = 0; iParam < nCount; iParam++) {
            String strKey = pIni.getValue(strSection, Integer.toString(iParam));
            if (!m_pContainer.loadValue(strKey))
                bResult = false;
        }
        return bResult;
    }

    @Override
    public IYoonContainer getContainer() {
        return m_pContainer;
    }

    @Override
    public void setContainer(IYoonContainer pContainer) {
        m_pContainer = pContainer;
    }

    @Override
    public boolean saveTemplate() {
        if (RootDirectory == "" || m_pContainer == null)
            return false;

        boolean bResult = true;
        int iParam = 0;
        String strSection = String.format("%d_%s", No, Name);
        String strIniFilePath = Paths.get(RootDirectory, "YoonTemplate.ini").toString();
        m_pContainer.setRootDirectory(Paths.get(RootDirectory, strSection).toString());
        //// Save the Information of templates to Ini files
        YoonIni pIni = new YoonIni(strIniFilePath);
        Map<String, YoonParameter> pMap = m_pContainer.getObjectMap();
        pIni.setValue(strSection, "Count", Integer.toString(pMap.size()));
        for (String strKey : pMap.keySet()) {
            pIni.setValue(strSection, Integer.toString(iParam), strKey);
            if (!m_pContainer.saveValue(strKey))
                bResult = false;
            iParam++;
        }
        return bResult;
    }
}