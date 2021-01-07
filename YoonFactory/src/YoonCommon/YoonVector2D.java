package YoonCommon;

public class YoonVector2D implements IYoonVector<Double>, IYoonVector2D<Double> {

    public double[] Array = new double[3];

    public int getCount() {
        return 3;
    }

    public void copyFrom(IYoonVector pVector) {
        if (pVector instanceof YoonVector2D) {
            YoonVector2D vec = (YoonVector2D) pVector;
            setX(vec.getX());
            setY(vec.getY());
            setW(1.0);
        }
    }

    @Override
    public IYoonVector clone() {
        YoonVector2D v = new YoonVector2D();
        v.setX(getX());
        v.setY(getY());
        v.setW(getW());
        return v;
    }

    private static double DELTA = 0.0000000000001;

    public Double getW() {
        return Array[2];
    }

    public void setW(Double value) {
        Array[2] = value;
    }

    public Double getX() {
        return Array[0];
    }

    public void setX(Double value) {
        Array[0] = value;
    }

    public Double getY() {
        return Array[1];
    }

    public void setY(Double value) {
        Array[1] = value;
    }

    public YoonVector2D() {
        setX(0.0);
        setY(0.0);
        setW(1.0);
    }

    public YoonVector2D(IYoonVector p) {
        copyFrom(p);
    }

    public YoonVector2D(double dx, double dy) {
        setX(dx);
        setY(dy);
        setW(1.0);
    }

    public void zero() {
        setX(0.0);
        setY(0.0);
        setW(1.0);
    }

    public double length() {
        return Math.sqrt(getX() * getX() + getY() * getY());
    }

    public IYoonVector unit() {
        double len = this.length();
        if (len > DELTA) {
            len = 1.0 / len;
            Array[0] *= (int) len;
            Array[1] *= (int) len;
        }
        return this;
    }

    public IYoonVector reverse() {
        return new YoonVector2D(-getX(), -getY());
    }

    public double distance(IYoonVector pVector) {
        if (pVector instanceof YoonVector2D) {
            YoonVector2D vec = (YoonVector2D) pVector;
            double dx = getX() - vec.getX();
            double dy = getY() - vec.getY();
            return Math.sqrt(dx * dx + dy * dy);
        } else
            return 0.0;
    }

    public IYoonVector scale(Double sx, Double sy) {
        YoonMatrix2D pMatrix = new YoonMatrix2D();
        pMatrix.setScaleUnit(sx, sy);
        YoonVector2D vec = (YoonVector2D) multiple(pMatrix);
        setX(vec.getX());
        setY(vec.getY());
        return this;
    }

    public IYoonVector move(Double dx, Double dy) {
        YoonMatrix2D pMatrix = new YoonMatrix2D();
        pMatrix.setMovementUnit(dx, dy);
        YoonVector2D vec = (YoonVector2D) multiple(pMatrix);
        setX(vec.getX());
        setY(vec.getY());
        return this;
    }

    public IYoonVector move(IYoonVector pObject) {
        if (pObject instanceof YoonVector2D) {
            YoonVector2D pVector = (YoonVector2D) pObject;
            YoonMatrix2D pMatrix = new YoonMatrix2D();
            pMatrix.setMovementUnit(pVector);
            YoonVector2D vec = (YoonVector2D) multiple(pMatrix);
            setX(vec.getX());
            setY(vec.getY());
        }
        return this;
    }

    public IYoonVector rotate(double angle) {
        YoonMatrix2D pMatrix = new YoonMatrix2D();
        pMatrix.setRotateUnit(angle);
        YoonVector2D vec = (YoonVector2D) multiple(pMatrix);
        setX(vec.getX());
        setY(vec.getY());
        return this;
    }

    public IYoonVector rotate(IYoonVector center, double angle) {
        if (center instanceof YoonVector2D) {
            YoonVector2D pVecCenter = (YoonVector2D) center;
            move(pVecCenter.reverse());
            rotate(angle);
            move(pVecCenter);
        }
        return this;
    }

    public IYoonVector multiple(IYoonMatrix pObject) {
        YoonVector2D pVector = new YoonVector2D();
        if (pObject instanceof YoonMatrix3X3Double) {
            YoonMatrix3X3Double pMatrix = (YoonMatrix3X3Double) pObject;
            for (int i = 0; i < getCount(); i++) {
                pVector.Array[i] = 0;
                for (int k = 0; k < pMatrix.getLength(); k++)
                    pVector.Array[i] += pMatrix.array[i][k] * Array[k];
            }
        }
        return pVector;
    }

    public IYoonVector multiple(Double value) {
        return new YoonVector2D(getX() * value, getY() * value);
    }

    public IYoonVector add(IYoonVector pObject) {
        if (pObject instanceof YoonVector2D) {
            YoonVector2D pVector = (YoonVector2D) pObject;
            return new YoonVector2D(getX() + pVector.getX(), getY() + pVector.getY());
        } else
            return new YoonVector2D();
    }

    public IYoonVector substract(IYoonVector pObject) {
        if (pObject instanceof YoonVector2D) {
            YoonVector2D pVector = (YoonVector2D) pObject;
            return new YoonVector2D(getX() - pVector.getX(), getY() - pVector.getY());
        } else
            return new YoonVector2D();
    }

    public IYoonVector divide(Double value) {
        return new YoonVector2D(getX() / value, getY() / value);
    }

    public Double multiple(IYoonVector pObject) // dot product
    {
        if (pObject instanceof YoonVector2D) {
            YoonVector2D pVector = (YoonVector2D) pObject;
            return getX() * pVector.getX() + getY() * pVector.getY();
        } else return 0.0;
    }
}