package YoonCommon;
import java.util.Map;

public interface IYoonContainer<T> {

    String getRootDirectory();

    void setRootDirectory(String strDir);

    void copyFrom(IYoonContainer pContainer);

    void clear();

    boolean loadValue(String strKey);

    boolean saveValue(String strKey);

    Map<String, T> getObjectMap();

    boolean add(String strKey, T pValue);

    boolean remove(String strKey);

    String getKey(T pValue);

    T getValue(String strKey);

    void setValue(String strKey, T pValue);
}