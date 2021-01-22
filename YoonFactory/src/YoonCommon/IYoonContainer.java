package YoonCommon;

import java.util.Map;

public interface IYoonContainer<T, V> extends Map<T, V> {

    String getRootDirectory();

    void setRootDirectory(String strDir);

    void copyFrom(IYoonContainer pContainer);

    void clear();

    boolean loadValue(T pKey);

    boolean saveValue(T pKey);
}