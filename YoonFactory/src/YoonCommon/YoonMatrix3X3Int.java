package YoonCommon;

public class YoonMatrix3X3Int implements IYoonMatrix<Integer> {
    public int[][] Array = new int[3][3];

    public int GetLength() {
        return 3;
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

    public int GetMatrix13() {
        return Array[0][2];
    }

    public void SetMatrix13(int nValue) {
        Array[0][2] = nValue;
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

    public int GetMatrix23() {
        return Array[1][2];
    }

    public void SetMatrix23(int nValue) {
        Array[1][2] = nValue;
    }

    public int GetMatrix31() {
        return Array[2][0];
    }

    public void SetMatrix31(int nValue) {
        Array[2][0] = nValue;
    }

    public int GetMatrix32() {
        return Array[2][1];
    }

    public void SetMatrix32(int nValue) {
        Array[2][1] = nValue;
    }

    public int GetMatrix33() {
        return Array[2][2];
    }

    public void SetMatrix33(int nValue) {
        Array[2][2] = nValue;
    }

    public YoonMatrix3X3Int() {
        Array[0][0] = Array[1][1] = Array[2][2] = 1;
        Array[0][1] = Array[0][2] = Array[1][0] = Array[1][2] = Array[2][0] = Array[2][1] = 0;
    }

    public YoonMatrix3X3Int(IYoonMatrix pMatrix) {
        this.CopyFrom(pMatrix);
    }

    public Integer GetDeterminant() {
        /*
        matrix_11 * matrix_22 * matrix_33 + matrix_21 * matrix_32 * matrix_13 + matrix_31 * matrix_12 * matrix_23
        - matrix_11 * matrix_32 * matrix_23 - matrix_31 * matrix_22 * matrix_13 - matrix_21 * matrix_12 * matrix_33;
        */
        int nSum = 0;
        for (int i = 0; i < GetLength(); i++)
            nSum += Array[0][i] * Cofactor(0, i);
        return nSum;
    }

    public IYoonMatrix Clone() {
        YoonMatrix3X3Int pMatrix = new YoonMatrix3X3Int();
        for (int i = 0; i < pMatrix.GetLength(); i++) {
            for (int j = 0; j < pMatrix.GetLength(); j++) {
                pMatrix.Array[i][j] = Array[i][j];
            }
        }
        return pMatrix;
    }

    public void CopyFrom(IYoonMatrix pMatrix) {
        if (pMatrix instanceof YoonMatrix3X3Int) {
            YoonMatrix3X3Int pObject = (YoonMatrix3X3Int) pMatrix;
            for (int i = 0; i < GetLength(); i++) {
                for (int j = 0; j < GetLength(); j++) {
                    Array[i][j] = pObject.Array[i][j];
                }
            }
        }
    }

    public Integer Cofactor(int nRow, int nCol) {
        return (int) Math.pow(-1, nRow + nCol) * (int) GetMinorMatrix(nRow, nCol).GetDeterminant();
    }

    public IYoonMatrix GetMinorMatrix(int nRow, int nCol) {
        if (nRow < 0 || nRow >= GetLength() || nCol < 0 || nCol >= GetLength())
            return new YoonMatrix2X2Int();

        int[][] pArray = new int[2][2];
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
        YoonMatrix2X2Int pMatrix = new YoonMatrix2X2Int();
        pMatrix.Array = pArray;
        return pMatrix;
    }

    public IYoonMatrix GetAdjointMatrix() {
        int[][] pArray = new int[3][3];
        for (int i = 0; i < GetLength(); i++) {
            for (int j = 0; j < GetLength(); j++) {
                pArray[i][j] = Cofactor(i, j);
            }
        }
        YoonMatrix3X3Int pMatrix = new YoonMatrix3X3Int();
        pMatrix.Array = pArray;
        return pMatrix.Transpose();
    }

    public IYoonMatrix Inverse() {
        if (GetDeterminant() == 0) return Unit();

        YoonMatrix3X3Int m = new YoonMatrix3X3Int(this);
        CopyFrom(m.GetAdjointMatrix().Divide(m.GetDeterminant()));
        return this;
    }

    public IYoonMatrix Transpose() {
        YoonMatrix3X3Int m = new YoonMatrix3X3Int(this);
        SetMatrix12(m.GetMatrix21());
        SetMatrix13(m.GetMatrix31());
        SetMatrix21(m.GetMatrix12());
        SetMatrix23(m.GetMatrix32());
        SetMatrix31(m.GetMatrix23());
        SetMatrix32(m.GetMatrix23());
        return this;
    }

    public IYoonMatrix Unit() {
        Array[0][0] = Array[1][1] = Array[2][2] = 1;
        Array[0][1] = Array[0][2] = Array[1][0] = Array[1][2] = Array[2][0] = Array[2][1] = 0;
        return this;
    }

    public IYoonMatrix Add(IYoonMatrix pObject) {
        YoonMatrix3X3Int m = new YoonMatrix3X3Int();
        YoonMatrix3X3Int pMatrix = (YoonMatrix3X3Int) pObject;
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = Array[i][j] + pMatrix.Array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix Substract(IYoonMatrix pObject) {
        YoonMatrix3X3Int m = new YoonMatrix3X3Int();
        YoonMatrix3X3Int pMatrix = (YoonMatrix3X3Int) pObject;
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = Array[i][j] - pMatrix.Array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix Multiple(Integer nValue) {
        YoonMatrix3X3Int m = new YoonMatrix3X3Int();
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = nValue * Array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix Multiple(IYoonMatrix pObject) {
        YoonMatrix3X3Int m = new YoonMatrix3X3Int();
        YoonMatrix3X3Int pMatrix = (YoonMatrix3X3Int) pObject;
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = 0;
                for (int kValue = 0; kValue < m.GetLength(); kValue++)
                    m.Array[i][j] += (Array[i][kValue] * pMatrix.Array[kValue][j]);
            }
        }
        return m;
    }

    public IYoonMatrix Divide(Integer nValue) {
        YoonMatrix3X3Int m = new YoonMatrix3X3Int();
        for (int i = 0; i < m.GetLength(); i++) {
            for (int j = 0; j < m.GetLength(); j++) {
                m.Array[i][j] = Array[i][j] / nValue;
            }
        }
        return m;
    }
}