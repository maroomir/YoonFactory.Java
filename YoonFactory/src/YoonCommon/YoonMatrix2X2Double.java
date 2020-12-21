package YoonCommon;

public class YoonMatrix2X2Double implements IYoonMatrix<Double> {

    public double[][] Array = new double[2][2];

    public int GetLength() {
        return 2;
    }

    public double GetMatrix11() {
        return Array[0][0];
    }

    public void SetMatrix11(double dValue) {
        Array[0][0] = dValue;
    }

    public double GetMatrix12() {
        return Array[0][1];
    }

    public void SetMatrix12(double dValue) {
        Array[0][1] = dValue;
    }

    public double GetMatrix21() {
        return Array[1][0];
    }

    public void SetMatrix21(double dValue) {
        Array[1][0] = dValue;
    }

    public double GetMatrix22() {
        return Array[1][1];
    }

    public void SetMatrix22(double dValue) {
        Array[1][1] = dValue;
    }

    public YoonMatrix2X2Double() {
        Array[0][0] = Array[1][1] = 1.0;
        Array[0][1] = Array[1][0] = 0.0;
    }

    public YoonMatrix2X2Double(IYoonMatrix m) {
        this.CopyFrom(m);
    }

    public Double GetDeterminant() {
        return GetMatrix11() * GetMatrix22() - GetMatrix12() * GetMatrix21();
    }

    public Double Cofactor(int nRow, int nCol) {
        return 0.0;
    }

    public IYoonMatrix GetMinorMatrix(int nRow, int nCol) {
        return new YoonMatrix2X2Double();
    }

    public IYoonMatrix GetAdjointMatrix() {
        return new YoonMatrix2X2Double();
    }

    public IYoonMatrix Clone() {
        YoonMatrix2X2Double pMatrix = new YoonMatrix2X2Double();
        pMatrix.SetMatrix11(GetMatrix11());
        pMatrix.SetMatrix12(GetMatrix12());
        pMatrix.SetMatrix21(GetMatrix21());
        pMatrix.SetMatrix22(GetMatrix22());
        return pMatrix;
    }

    public void CopyFrom(IYoonMatrix pMatrix) {
        if (pMatrix instanceof YoonMatrix2X2Double) {
            YoonMatrix2X2Double pObject = (YoonMatrix2X2Double) pMatrix;
            SetMatrix11(pObject.GetMatrix11());
            SetMatrix12(pObject.GetMatrix12());
            SetMatrix21(pObject.GetMatrix21());
            SetMatrix22(pObject.GetMatrix22());
        }
    }

    public IYoonMatrix Inverse() {
        if (GetDeterminant() == 0) return Unit();

        YoonMatrix2X2Double m = new YoonMatrix2X2Double(this);
        SetMatrix11(m.GetMatrix22() / m.GetDeterminant());
        SetMatrix12(-m.GetMatrix12() / m.GetDeterminant());
        SetMatrix21(-m.GetMatrix21() / m.GetDeterminant());
        SetMatrix22(m.GetMatrix11() / m.GetDeterminant());
        return this;
    }

    public IYoonMatrix Transpose() {
        YoonMatrix2X2Double m = new YoonMatrix2X2Double(this);
        SetMatrix12(m.GetMatrix21());
        SetMatrix21(m.GetMatrix12());
        return this;
    }

    public IYoonMatrix Unit() {
        Array[0][0] = Array[1][1] = 1.0;
        Array[0][1] = Array[1][0] = 0.0;
        return this;
    }

    public IYoonMatrix Add(IYoonMatrix pObject) {
        YoonMatrix2X2Double m = new YoonMatrix2X2Double();
        YoonMatrix2X2Double pMatrix = (YoonMatrix2X2Double) pObject;
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = Array[i][j] + pMatrix.Array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix Substract(IYoonMatrix pObject) {
        YoonMatrix2X2Double m = new YoonMatrix2X2Double();
        YoonMatrix2X2Double pMatrix = (YoonMatrix2X2Double) pObject;
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = Array[i][j] - pMatrix.Array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix Multiple(Double dValue) {
        YoonMatrix2X2Double m = new YoonMatrix2X2Double();
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = dValue * Array[i][j];
            }
        }
        return m;
    }

    public YoonMatrix2X2Double Multiple(IYoonMatrix pObject) {
        YoonMatrix2X2Double m = new YoonMatrix2X2Double();
        YoonMatrix2X2Double pMatrix = (YoonMatrix2X2Double) pObject;
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = 0;
                for (int kValue = 0; kValue < m.GetLength(); kValue++)
                    m.Array[i][j] += (Array[i][kValue] * pMatrix.Array[kValue][j]);
            }
        }
        return m;
    }

    public YoonMatrix2X2Double Divide(Double dValue) {
        YoonMatrix2X2Double m = new YoonMatrix2X2Double();
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = Array[i][j] / dValue;
            }
        }
        return m;
    }
}