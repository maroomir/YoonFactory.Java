package YoonCommon;

public interface IYoonVector<T> {
    int GetCount();

    IYoonVector Clone();

    void CopyFrom(IYoonVector pVector);

    void Zero();

    IYoonVector Unit();

    IYoonVector Reverse();

    double Length();

    double Distance(IYoonVector pVector);

    IYoonVector Add(IYoonVector pObject);

    IYoonVector Substract(IYoonVector pObject);

    IYoonVector Multiple(T value);

    IYoonVector Multiple(IYoonMatrix pObject);

    T Multiple(IYoonVector pObject);

    IYoonVector Divide(T value);
}