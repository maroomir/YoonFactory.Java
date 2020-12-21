package YoonCommon;

public interface IYoonParameter {

    boolean IsEqual(IYoonParameter pParam);

    void CopyFrom(IYoonParameter pParam);

    IYoonParameter Clone();
}