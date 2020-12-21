package YoonCommon;

public interface IYoonObject {
    int GetLabelNo();

    void SetLabelNo(int nNo);

    double GetScore();

    void SetScore(double dScore);

    void CopyFrom(IYoonObject pObject);

    IYoonObject Clone();
}