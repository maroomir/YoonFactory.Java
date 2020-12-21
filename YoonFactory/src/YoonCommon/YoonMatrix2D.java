package YoonCommon;

public class YoonMatrix2D extends YoonMatrix3X3Double implements IYoonMatrix2D<Double>{

    public IYoonMatrix SetScaleUnit(Double sx, Double sy)
    {
        Unit();
        Array[0][0] *= sx;
        Array[1][1] *= sy;
        return this;
    }

    public IYoonMatrix SetMovementUnit(Double dx, Double dy)
    {
        Unit();
        Array[0][2] += dx;
        Array[1][2] += dy;
        return this;
    }

    public IYoonMatrix SetMovementUnit(IYoonVector2D<Double> vec)
    {
        Unit();
        Array[0][2] += vec.GetX();
        Array[1][2] += vec.GetY();
        return this;
    }

    public IYoonMatrix SetRotateUnit(double dAngle)
    {
        Unit();
        double cosT = Math.cos(dAngle);
        double sinT = Math.sin(dAngle);
        Array[0][0] = cosT;
        Array[0][1] = -sinT;
        Array[1][0] = sinT;
        Array[1][1] = cosT;
        return this;
    }
}
