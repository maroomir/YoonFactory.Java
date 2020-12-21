package YoonCommon;

public interface IYoonTemplate {

    int GetNo();

    void SetNo(int nNo);

    String GetName();

    void SetName(String strName);

    String GetRootDirectory();

    void SetRootDirectory(String strDir);

    void CopyFrom(IYoonTemplate pTemplate);

    IYoonTemplate Clone();

    boolean SaveTemplate();

    boolean LoadTemplate();

    IYoonContainer GetContainer();

    void SetContainer(IYoonContainer pContainer);
}
