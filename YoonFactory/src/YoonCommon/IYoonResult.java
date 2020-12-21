package YoonCommon;

public interface IYoonResult {

    boolean IsEqual(IYoonResult pResult);

    void CopyFrom(IYoonResult pResult);

    IYoonResult Clone();
}
