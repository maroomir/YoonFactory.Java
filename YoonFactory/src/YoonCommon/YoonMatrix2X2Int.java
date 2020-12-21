package YoonCommon;

public class YoonMatrix2X2Int implements IYoonMatrix<Integer> {

    public int[][] Array = new int[2][2];

    public int GetLength() {
        return 2;
    }

    public int GetMatrix11() {
        return Array[0][0];
    }

    public void SetMatrix11(int nValue) {
        Array[0][0] = nValue;
    }

    public int GetMatrix12() {
        return Array[0][1];
    }

    public void SetMatrix12(int nValue) {
        Array[0][1] = nValue;
    }

    public int GetMatrix21() {
        return Array[1][0];
    }

    public void SetMatrix21(int nValue) {
        Array[1][0] = nValue;
    }

    public int GetMatrix22() {
        return Array[1][1];
    }

    public void SetMatrix22(int nValue) {
        Array[1][1] = nValue;
    }

    public YoonMatrix2X2Int() {
        Array[0][0] = Array[1][1] = 1;
        Array[0][1] = Array[1][0] = 0;
    }

    public YoonMatrix2X2Int(IYoonMatrix m) {
        this.CopyFrom(m);
    }

    public Integer GetDeterminant() {
        return GetMatrix11() * GetMatrix22() - GetMatrix12() * GetMatrix21();
    }

    public Integer Cofactor(int nRow, int nCol) {
        return 0;
    }

    public IYoonMatrix GetMinorMatrix(int nRow, int nCol) {
        return new YoonMatrix2X2Int();
    }

    public IYoonMatrix GetAdjointMatrix() {
        return new YoonMatrix2X2Int();
    }

    public IYoonMatrix Clone() {
        YoonMatrix2X2Int pMatrix = new YoonMatrix2X2Int();
        pMatrix.SetMatrix11(GetMatrix11());
        pMatrix.SetMatrix12(GetMatrix12());
        pMatrix.SetMatrix21(GetMatrix21());
        pMatrix.SetMatrix22(GetMatrix22());
        return pMatrix;
    }

    public void CopyFrom(IYoonMatrix pMatrix) {
        if (pMatrix instanceof YoonMatrix2X2Int) {
            YoonMatrix2X2Int pObject = (YoonMatrix2X2Int) pMatrix;
            SetMatrix11(pObject.GetMatrix11());
            SetMatrix12(pObject.GetMatrix12());
            SetMatrix21(pObject.GetMatrix21());
            SetMatrix22(pObject.GetMatrix22());
        }
    }

    public IYoonMatrix Inverse() {
        if (GetDeterminant() == 0) return Unit();

        YoonMatrix2X2Int m = new YoonMatrix2X2Int(this);
        SetMatrix11(m.GetMatrix22() / m.GetDeterminant());
        SetMatrix12(-m.GetMatrix12() / m.GetDeterminant());
        SetMatrix21(-m.GetMatrix21() / m.GetDeterminant());
        SetMatrix22(m.GetMatrix11() / m.GetDeterminant());
        return this;
    }

    public IYoonMatrix Transpose() {
        YoonMatrix2X2Int m = new YoonMatrix2X2Int(this);
        SetMatrix12(m.GetMatrix21());
        SetMatrix21(m.GetMatrix12());
        return this;
    }

    public IYoonMatrix Unit() {
        Array[0][0] = Array[1][1] = 1;
        Array[0][1] = Array[1][0] = 0;
        return this;
    }

    public IYoonMatrix Add(IYoonMatrix pObject) {
        YoonMatrix2X2Int m = new YoonMatrix2X2Int();
        YoonMatrix2X2Int pMatrix = (YoonMatrix2X2Int) pObject;
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = Array[i][j] + pMatrix.Array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix Substract(IYoonMatrix pObject) {
        YoonMatrix2X2Int m = new YoonMatrix2X2Int();
        YoonMatrix2X2Int pMatrix = (YoonMatrix2X2Int) pObject;
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = Array[i][j] - pMatrix.Array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix Multiple(Integer nValue) {
        YoonMatrix2X2Int m = new YoonMatrix2X2Int();
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = nValue * Array[i][j];
            }
        }
        return m;
    }

    public YoonMatrix2X2Int Multiple(IYoonMatrix pObject) {
        YoonMatrix2X2Int m = new YoonMatrix2X2Int();
        YoonMatrix2X2Int pMatrix = (YoonMatrix2X2Int) pObject;
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = 0;
                for (int kValue = 0; kValue < m.GetLength(); kValue++)
                    m.Array[i][j] += (Array[i][kValue] * pMatrix.Array[kValue][j]);
            }
        }
        return m;
    }

    public YoonMatrix2X2Int Divide(Integer nValue) {
        YoonMatrix2X2Int m = new YoonMatrix2X2Int();
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = Array[i][j] / nValue;
            }
        }
        return m;
    }
}