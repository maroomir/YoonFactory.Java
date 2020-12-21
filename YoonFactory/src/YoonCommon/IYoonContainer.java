package YoonCommon;
import java.util.Map;

public interface IYoonContainer<T> {

    String GetRootDirectory();

    void SetRootDirectory(String strDir);

    void CopyFrom(IYoonContainer pContainer);

    IYoonContainer Clone();

    void Clear();

    boolean LoadValue(String strKey);

    boolean SaveValue(String strKey);

    Map<String, T> GetObjectMap();

    boolean Add(String strKey, T pValue);

    boolean Remove(String strKey);

    String GetKey(T pValue);

    T GetValue(String strKey);

    void SetValue(String strKey, T pValue);
}