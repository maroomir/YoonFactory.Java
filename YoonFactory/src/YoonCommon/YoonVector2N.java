package YoonCommon;

public class YoonVector2N implements IYoonVector<Integer>, IYoonVector2D<Integer> {

    public int[] Array = new int[3];

    public int getCount() {
        return 3;
    }

    public void copyFrom(IYoonVector pVector) {
        if (pVector instanceof YoonVector2N) {
            YoonVector2N vec = (YoonVector2N) pVector;
            setX(vec.getX());
            setY(vec.getY());
            setW(1);
        }
    }

    public IYoonVector clone() {
        YoonVector2N v = new YoonVector2N();
        v.setX(getX());
        v.setY(getY());
        v.setW(getW());
        return v;
    }

    private static double DELTA = 0.0000000000001;

    public Integer getW() {
        return Array[2];
    }

    public void setW(Integer value) {
        Array[2] = value;
    }

    public Integer getX() {
        return Array[0];
    }

    public void setX(Integer value) {
        Array[0] = value;
    }

    public Integer getY() {
        return Array[1];
    }

    public void setY(Integer value) {
        Array[1] = value;
    }

    public YoonVector2N() {
        setX(0);
        setY(0);
        setW(1);
    }

    public YoonVector2N(IYoonVector p) {
        copyFrom(p);
    }

    public YoonVector2N(int dx, int dy) {
        setX(dx);
        setY(dy);
        setW(1);
    }

    public void zero() {
        setX(0);
        setY(0);
        setW(1);
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
        return new YoonVector2N(-getX(), -getY());
    }

    public double distance(IYoonVector pVector) {
        if (pVector instanceof YoonVector2N) {
            YoonVector2N vec = (YoonVector2N) pVector;
            double dx = getX() - vec.getX();
            double dy = getY() - vec.getY();
            return Math.sqrt(dx * dx + dy * dy);
        } else
            return 0.0;
    }

    public IYoonVector scale(Integer sx, Integer sy) {
        YoonMatrix2N pMatrix = new YoonMatrix2N();
        pMatrix.setScaleUnit(sx, sy);
        YoonVector2N vec = (YoonVector2N) multiple(pMatrix);
        setX(vec.getX());
        setY(vec.getY());
        return this;
    }

    public IYoonVector move(Integer dx, Integer dy) {
        YoonMatrix2N pMatrix = new YoonMatrix2N();
        pMatrix.setMovementUnit(dx, dy);
        YoonVector2N vec = (YoonVector2N) multiple(pMatrix);
        setX(vec.getX());
        setY(vec.getY());
        return this;
    }

    public IYoonVector move(IYoonVector pObject) {
        if (pObject instanceof YoonVector2N) {
            YoonVector2N pVector = (YoonVector2N) pObject;
            YoonMatrix2N pMatrix = new YoonMatrix2N();
            pMatrix.setMovementUnit(pVector);
            YoonVector2N vec = (YoonVector2N) multiple(pMatrix);
            setX(vec.getX());
            setY(vec.getY());
        }
        return this;
    }

    public IYoonVector rotate(double angle) {
        YoonMatrix2N pMatrix = new YoonMatrix2N();
        pMatrix.setRotateUnit(angle);
        YoonVector2N vec = (YoonVector2N) multiple(pMatrix);
        setX(vec.getX());
        setY(vec.getY());
        return this;
    }

    public IYoonVector rotate(IYoonVector center, double angle) {
        if (center instanceof YoonVector2N) {
            YoonVector2N pVecCenter = (YoonVector2N) center;
            move(pVecCenter.reverse());
            rotate(angle);
            move(pVecCenter);
        }
        return this;
    }

    public IYoonVector multiple(IYoonMatrix pObject) {
        YoonVector2N pVector = new YoonVector2N();
        if (pObject instanceof YoonMatrix3X3Int) {
            YoonMatrix3X3Int pMatrix = (YoonMatrix3X3Int) pObject;
            for (int i = 0; i < getCount(); i++) {
                pVector.Array[i] = 0;
                for (int k = 0; k < pMatrix.getLength(); k++)
                    pVector.Array[i] += pMatrix.Array[i][k] * Array[k];
            }
        }
        return pVector;
    }

    public IYoonVector multiple(Integer value) {
        return new YoonVector2N(getX() * value, getY() * value);
    }

    public IYoonVector add(IYoonVector pObject) {
        if (pObject instanceof YoonVector2N) {
            YoonVector2N pVector = (YoonVector2N) pObject;
            return new YoonVector2N(getX() + pVector.getX(), getY() + pVector.getY());
        } else
            return new YoonVector2N();
    }

    public IYoonVector substract(IYoonVector pObject) {
        if (pObject instanceof YoonVector2N) {
            YoonVector2N pVector = (YoonVector2N) pObject;
            return new YoonVector2N(getX() - pVector.getX(), getY() - pVector.getY());
        } else
            return new YoonVector2N();
    }

    public IYoonVector divide(Integer value) {
        return new YoonVector2N(getX() / value, getY() / value);
    }

    public Integer multiple(IYoonVector pObject) // dot product
    {
        if (pObject instanceof YoonVector2N) {
            YoonVector2N pVector = (YoonVector2N) pObject;
            return getX() * pVector.getX() + getY() * pVector.getY();
        } else return 0;
    }
}