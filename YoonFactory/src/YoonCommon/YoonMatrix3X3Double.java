package YoonCommon;

public class YoonMatrix3X3Double implements IYoonMatrix<Double> {
    public double[][] Array = new double[3][3];

    public int GetLength() {
        return 3;
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

    public double GetMatrix13() {
        return Array[0][2];
    }

    public void SetMatrix13(double dValue) {
        Array[0][2] = dValue;
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

    public double GetMatrix23() {
        return Array[1][2];
    }

    public void SetMatrix23(double dValue) {
        Array[1][2] = dValue;
    }

    public double GetMatrix31() {
        return Array[2][0];
    }

    public void SetMatrix31(double dValue) {
        Array[2][0] = dValue;
    }

    public double GetMatrix32() {
        return Array[2][1];
    }

    public void SetMatrix32(double dValue) {
        Array[2][1] = dValue;
    }

    public double GetMatrix33() {
        return Array[2][2];
    }

    public void SetMatrix33(double dValue) {
        Array[2][2] = dValue;
    }

    public YoonMatrix3X3Double() {
        Array[0][0] = Array[1][1] = Array[2][2] = 1.0;
        Array[0][1] = Array[0][2] = Array[1][0] = Array[1][2] = Array[2][0] = Array[2][1] = 0.0;
    }

    public YoonMatrix3X3Double(IYoonMatrix pMatrix) {
        this.CopyFrom(pMatrix);
    }

    public Double GetDeterminant() {
        /*
        matrix_11 * matrix_22 * matrix_33 + matrix_21 * matrix_32 * matrix_13 + matrix_31 * matrix_12 * matrix_23
        - matrix_11 * matrix_32 * matrix_23 - matrix_31 * matrix_22 * matrix_13 - matrix_21 * matrix_12 * matrix_33;
        */
        double nSum = 0;
        for (int i = 0; i < GetLength(); i++)
            nSum += Array[0][i] * Cofactor(0, i);
        return nSum;
    }

    public IYoonMatrix Clone() {
        YoonMatrix3X3Double pMatrix = new YoonMatrix3X3Double();
        for (int i = 0; i < pMatrix.GetLength(); i++) {
            for (int j = 0; j < pMatrix.GetLength(); j++) {
                pMatrix.Array[i][j] = Array[i][j];
            }
        }
        return pMatrix;
    }

    public void CopyFrom(IYoonMatrix pMatrix) {
        if (pMatrix instanceof YoonMatrix3X3Double) {
            YoonMatrix3X3Double pObject = (YoonMatrix3X3Double) pMatrix;
            for (int i = 0; i < GetLength(); i++) {
                for (int j = 0; j < GetLength(); j++) {
                    Array[i][j] = pObject.Array[i][j];
                }
            }
        }
    }

    public Double Cofactor(int nRow, int nCol) {
        return Math.pow(-1, nRow + nCol) * (double) GetMinorMatrix(nRow, nCol).GetDeterminant();
    }

    public IYoonMatrix GetMinorMatrix(int nRow, int nCol) {
        if (nRow < 0 || nRow >= GetLength() || nCol < 0 || nCol >= GetLength())
            return new YoonMatrix2X2Double();

        double[][] pArray = new double[2][2];
        int iCount = 0;
        int jCount = 0;
        for (int i = 0; i < GetLength(); i++) {
            if (i == nRow) continue;
            for (int j = 0; j < GetLength(); j++) {
                if (j == nCol) continue;
                pArray[iCount][jCount] = Array[i][j];
                jCount++;
            }
            iCount++;
        }
        YoonMatrix2X2Double pMatrix = new YoonMatrix2X2Double();
        pMatrix.Array = pArray;
        return pMatrix;
    }

    public IYoonMatrix GetAdjointMatrix() {
        double[][] pArray = new double[3][3];
        for (int i = 0; i < GetLength(); i++) {
            for (int j = 0; j < GetLength(); j++) {
                pArray[i][j] = Cofactor(i, j);
            }
        }
        YoonMatrix3X3Double pMatrix = new YoonMatrix3X3Double();
        pMatrix.Array = pArray;
        return pMatrix.Transpose();
    }

    public IYoonMatrix Inverse() {
        if (GetDeterminant() == 0) return Unit();

        YoonMatrix3X3Double m = new YoonMatrix3X3Double(this);
        CopyFrom(m.GetAdjointMatrix().Divide(m.GetDeterminant()));
        return this;
    }

    public IYoonMatrix Transpose() {
        YoonMatrix3X3Double m = new YoonMatrix3X3Double(this);
        SetMatrix12(m.GetMatrix21());
        SetMatrix13(m.GetMatrix31());
        SetMatrix21(m.GetMatrix12());
        SetMatrix23(m.GetMatrix32());
        SetMatrix31(m.GetMatrix23());
        SetMatrix32(m.GetMatrix23());
        return this;
    }

    public IYoonMatrix Unit() {
        Array[0][0] = Array[1][1] = Array[2][2] = 1.0;
        Array[0][1] = Array[0][2] = Array[1][0] = Array[1][2] = Array[2][0] = Array[2][1] = 0.0;
        return this;
    }

    public IYoonMatrix Add(IYoonMatrix pObject) {
        YoonMatrix3X3Double m = new YoonMatrix3X3Double();
        YoonMatrix3X3Double pMatrix = (YoonMatrix3X3Double) pObject;
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = Array[i][j] + pMatrix.Array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix Substract(IYoonMatrix pObject) {
        YoonMatrix3X3Double m = new YoonMatrix3X3Double();
        YoonMatrix3X3Double pMatrix = (YoonMatrix3X3Double) pObject;
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = Array[i][j] - pMatrix.Array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix Multiple(Double dValue) {
        YoonMatrix3X3Double m = new YoonMatrix3X3Double();
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = dValue * Array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix Multiple(IYoonMatrix pObject) {
        YoonMatrix3X3Double m = new YoonMatrix3X3Double();
        YoonMatrix3X3Double pMatrix = (YoonMatrix3X3Double) pObject;
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = 0;
                for (int kValue = 0; kValue < m.GetLength(); kValue++)
                    m.Array[i][j] += (Array[i][kValue] * pMatrix.Array[kValue][j]);
            }
        }
        return m;
    }

    public IYoonMatrix Divide(Double dValue) {
        YoonMatrix3X3Double m = new YoonMatrix3X3Double();
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = Array[i][j] / dValue;
            }
        }
        return m;
    }
}