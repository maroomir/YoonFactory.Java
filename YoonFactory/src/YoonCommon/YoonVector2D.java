package YoonCommon;

public class YoonVector2D implements IYoonVector<Double>, IYoonVector2D<Double> {

    public double[] Array = new double[3];

    public int GetCount() {
        return 3;
    }

    public void CopyFrom(IYoonVector pVector) {
        if (pVector instanceof YoonVector2D) {
            YoonVector2D vec = (YoonVector2D) pVector;
            SetX(vec.GetX());
            SetY(vec.GetY());
            SetW(1.0);
        }
    }

    public IYoonVector Clone() {
        YoonVector2D v = new YoonVector2D();
        v.SetX(GetX());
        v.SetY(GetY());
        v.SetW(GetW());
        return v;
    }

    private static double DELTA = 0.0000000000001;

    public Double GetW() {
        return Array[2];
    }

    public void SetW(Double value) {
        Array[2] = value;
    }

    public Double GetX() {
        return Array[0];
    }

    public void SetX(Double value) {
        Array[0] = value;
    }

    public Double GetY() {
        return Array[1];
    }

    public void SetY(Double value) {
        Array[1] = value;
    }

    public YoonVector2D() {
        SetX(0.0);
        SetY(0.0);
        SetW(1.0);
    }

    public YoonVector2D(IYoonVector p) {
        CopyFrom(p);
    }

    public YoonVector2D(double dx, double dy) {
        SetX(dx);
        SetY(dy);
        SetW(1.0);
    }

    public void Zero() {
        SetX(0.0);
        SetY(0.0);
        SetW(1.0);
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
        return new YoonVector2D(-GetX(), -GetY());
    }

    public double Distance(IYoonVector pVector) {
        if (pVector instanceof YoonVector2D) {
            YoonVector2D vec = (YoonVector2D) pVector;
            double dx = GetX() - vec.GetX();
            double dy = GetY() - vec.GetY();
            return Math.sqrt(dx * dx + dy * dy);
        } else
            return 0.0;
    }

    public IYoonVector Scale(Double sx, Double sy) {
        YoonMatrix2D pMatrix = new YoonMatrix2D();
        pMatrix.SetScaleUnit(sx, sy);
        YoonVector2D vec = (YoonVector2D) Multiple(pMatrix);
        SetX(vec.GetX());
        SetY(vec.GetY());
        return this;
    }

    public IYoonVector Move(Double dx, Double dy) {
        YoonMatrix2D pMatrix = new YoonMatrix2D();
        pMatrix.SetMovementUnit(dx, dy);
        YoonVector2D vec = (YoonVector2D) Multiple(pMatrix);
        SetX(vec.GetX());
        SetY(vec.GetY());
        return this;
    }

    public IYoonVector Move(IYoonVector pObject) {
        if (pObject instanceof YoonVector2D) {
            YoonVector2D pVector = (YoonVector2D) pObject;
            YoonMatrix2D pMatrix = new YoonMatrix2D();
            pMatrix.SetMovementUnit(pVector);
            YoonVector2D vec = (YoonVector2D) Multiple(pMatrix);
            SetX(vec.GetX());
            SetY(vec.GetY());
        }
        return this;
    }

    public IYoonVector Rotate(double angle) {
        YoonMatrix2D pMatrix = new YoonMatrix2D();
        pMatrix.SetRotateUnit(angle);
        YoonVector2D vec = (YoonVector2D) Multiple(pMatrix);
        SetX(vec.GetX());
        SetY(vec.GetY());
        return this;
    }

    public IYoonVector Rotate(IYoonVector center, double angle) {
        if (center instanceof YoonVector2D) {
            YoonVector2D pVecCenter = (YoonVector2D) center;
            Move(pVecCenter.Reverse());
            Rotate(angle);
            Move(pVecCenter);
        }
        return this;
    }

    public IYoonVector Multiple(IYoonMatrix pObject) {
        YoonVector2D pVector = new YoonVector2D();
        if (pObject instanceof YoonMatrix3X3Double) {
            YoonMatrix3X3Double pMatrix = (YoonMatrix3X3Double) pObject;
            for (int i = 0; i < GetCount(); i++) {
                pVector.Array[i] = 0;
                for (int k = 0; k < pMatrix.GetLength(); k++)
                    pVector.Array[i] += pMatrix.Array[i][k] * Array[k];
            }
        }
        return pVector;
    }

    public IYoonVector Multiple(Double value) {
        return new YoonVector2D(GetX() * value, GetY() * value);
    }

    public IYoonVector Add(IYoonVector pObject) {
        if (pObject instanceof YoonVector2D) {
            YoonVector2D pVector = (YoonVector2D) pObject;
            return new YoonVector2D(GetX() + pVector.GetX(), GetY() + pVector.GetY());
        } else
            return new YoonVector2D();
    }

    public IYoonVector Substract(IYoonVector pObject) {
        if (pObject instanceof YoonVector2D) {
            YoonVector2D pVector = (YoonVector2D) pObject;
            return new YoonVector2D(GetX() - pVector.GetX(), GetY() - pVector.GetY());
        } else
            return new YoonVector2D();
    }

    public IYoonVector Divide(Double value) {
        return new YoonVector2D(GetX() / value, GetY() / value);
    }

    public Double Multiple(IYoonVector pObject) // dot product
    {
        if (pObject instanceof YoonVector2D) {
            YoonVector2D pVector = (YoonVector2D) pObject;
            return GetX() * pVector.GetX() + GetY() * pVector.GetY();
        } else return 0.0;
    }
}