package YoonCommon;

public interface IYoonMatrix<T> {

    int GetLength();

    T GetDeterminant();

    T Cofactor(int nRow, int nCol);   // 여인자

    IYoonMatrix GetMinorMatrix(int nRow, int nCol);   // 소행렬

    IYoonMatrix GetAdjointMatrix();   // 수반행렬

    void CopyFrom(IYoonMatrix pMatrix);

    IYoonMatrix Clone();

    IYoonMatrix Inverse();

    IYoonMatrix Transpose();

    IYoonMatrix Unit();

    IYoonMatrix Add(IYoonMatrix pObject);

    IYoonMatrix Substract(IYoonMatrix pObject);

    IYoonMatrix Multiple(T value);

    IYoonMatrix Multiple(IYoonMatrix pObject);

    IYoonMatrix Divide(T value);
}