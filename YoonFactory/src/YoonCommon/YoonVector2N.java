package YoonCommon;

public class YoonVector2N implements IYoonVector<Integer>, IYoonVector2D<Integer> {

    public int[] Array = new int[3];

    public int GetCount() {
        return 3;
    }

    public void CopyFrom(IYoonVector pVector) {
        if (pVector instanceof YoonVector2N) {
            YoonVector2N vec = (YoonVector2N) pVector;
            SetX(vec.GetX());
            SetY(vec.GetY());
            SetW(1);
        }
    }

    public IYoonVector Clone() {
        YoonVector2N v = new YoonVector2N();
        v.SetX(GetX());
        v.SetY(GetY());
        v.SetW(GetW());
        return v;
    }

    private static double DELTA = 0.0000000000001;

    public Integer GetW() {
        return Array[2];
    }

    public void SetW(Integer value) {
        Array[2] = value;
    }

    public Integer GetX() {
        return Array[0];
    }

    public void SetX(Integer value) {
        Array[0] = value;
    }

    public Integer GetY() {
        return Array[1];
    }

    public void SetY(Integer value) {
        Array[1] = value;
    }

    public YoonVector2N() {
        SetX(0);
        SetY(0);
        SetW(1);
    }

    public YoonVector2N(IYoonVector p) {
        CopyFrom(p);
    }

    public YoonVector2N(int dx, int dy) {
        SetX(dx);
        SetY(dy);
        SetW(1);
    }

    public void Zero() {
        SetX(0);
        SetY(0);
        SetW(1);
    }

    public double Length() {
        return Math.sqrt(GetX() * GetX() + GetY() * GetY());
    }

    public IYoonVector Unit() {
        double len = this.Length();
        if (len > DELTA) {
            len = 1.0 / len;
            Array[0] *= (int) len;
            Array[1] *= (int) len;
        }
        return this;
    }

    public IYoonVector Reverse() {
        return new YoonVector2N(-GetX(), -GetY());
    }

    public double Distance(IYoonVector pVector) {
        if (pVector instanceof YoonVector2N) {
            YoonVector2N vec = (YoonVector2N) pVector;
            double dx = GetX() - vec.GetX();
            double dy = GetY() - vec.GetY();
            return Math.sqrt(dx * dx + dy * dy);
        } else
            return 0.0;
    }

    public IYoonVector Scale(Integer sx, Integer sy) {
        YoonMatrix2N pMatrix = new YoonMatrix2N();
        pMatrix.SetScaleUnit(sx, sy);
        YoonVector2N vec = (YoonVector2N) Multiple(pMatrix);
        SetX(vec.GetX());
        SetY(vec.GetY());
        return this;
    }

    public IYoonVector Move(Integer dx, Integer dy) {
        YoonMatrix2N pMatrix = new YoonMatrix2N();
        pMatrix.SetMovementUnit(dx, dy);
        YoonVector2N vec = (YoonVector2N) Multiple(pMatrix);
        SetX(vec.GetX());
        SetY(vec.GetY());
        return this;
    }

    public IYoonVector Move(IYoonVector pObject) {
        if (pObject instanceof YoonVector2N) {
            YoonVector2N pVector = (YoonVector2N) pObject;
            YoonMatrix2N pMatrix = new YoonMatrix2N();
            pMatrix.SetMovementUnit(pVector);
            YoonVector2N vec = (YoonVector2N) Multiple(pMatrix);
            SetX(vec.GetX());
            SetY(vec.GetY());
        }
        return this;
    }

    public IYoonVector Rotate(double angle) {
        YoonMatrix2N pMatrix = new YoonMatrix2N();
        pMatrix.SetRotateUnit(angle);
        YoonVector2N vec = (YoonVector2N) Multiple(pMatrix);
        SetX(vec.GetX());
        SetY(vec.GetY());
        return this;
    }

    public IYoonVector Rotate(IYoonVector center, double angle) {
        if (center instanceof YoonVector2N) {
            YoonVector2N pVecCenter = (YoonVector2N) center;
            Move(pVecCenter.Reverse());
            Rotate(angle);
            Move(pVecCenter);
        }
        return this;
    }

    public IYoonVector Multiple(IYoonMatrix pObject) {
        YoonVector2N pVector = new YoonVector2N();
        if (pObject instanceof YoonMatrix3X3Int) {
            YoonMatrix3X3Int pMatrix = (YoonMatrix3X3Int) pObject;
            for (int i = 0; i < GetCount(); i++) {
                pVector.Array[i] = 0;
                for (int k = 0; k < pMatrix.GetLength(); k++)
                    pVector.Array[i] += pMatrix.Array[i][k] * Array[k];
            }
        }
        return pVector;
    }

    public IYoonVector Multiple(Integer value) {
        return new YoonVector2N(GetX() * value, GetY() * value);
    }

    public IYoonVector Add(IYoonVector pObject) {
        if (pObject instanceof YoonVector2N) {
            YoonVector2N pVector = (YoonVector2N) pObject;
            return new YoonVector2N(GetX() + pVector.GetX(), GetY() + pVector.GetY());
        } else
            return new YoonVector2N();
    }

    public IYoonVector Substract(IYoonVector pObject) {
        if (pObject instanceof YoonVector2N) {
            YoonVector2N pVector = (YoonVector2N) pObject;
            return new YoonVector2N(GetX() - pVector.GetX(), GetY() - pVector.GetY());
        } else
            return new YoonVector2N();
    }

    public IYoonVector Divide(Integer value) {
        return new YoonVector2N(GetX() / value, GetY() / value);
    }

    public Integer Multiple(IYoonVector pObject) // dot product
    {
        if (pObject instanceof YoonVector2N) {
            YoonVector2N pVector = (YoonVector2N) pObject;
            return GetX() * pVector.GetX() + GetY() * pVector.GetY();
        } else return 0;
    }
}