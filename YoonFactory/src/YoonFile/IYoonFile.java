package YoonFile;

public interface IYoonFile {
    String GetFilePath();

    void CopyFrom(IYoonFile pFile);

    IYoonFile Clone();

    boolean IsFileExist();
}
