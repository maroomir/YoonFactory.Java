package YoonFile;

public interface IYoonProperties {
    String GetFilePath();

    void CopyFrom(IYoonProperties pFile);

    IYoonProperties Clone();

    boolean IsFileExist();

    String GetValue(String strKey);

    boolean SetValue(String strKey, String strValue);
}