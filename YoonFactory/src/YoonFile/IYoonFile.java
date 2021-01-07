package YoonFile;

public interface IYoonFile {
    String getFilePath();

    void copyFrom(IYoonFile pFile);

    boolean isFileExist();
}
