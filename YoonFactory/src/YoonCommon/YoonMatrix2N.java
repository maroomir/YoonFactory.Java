package YoonCommon;

public class YoonMatrix2N extends YoonMatrix3X3Int implements IYoonMatrix2D<Integer>{

    public IYoonMatrix SetScaleUnit(Integer sx, Integer sy)
    {
        Unit();
        Array[0][0] *= sx;
        Array[1][1] *= sy;
        return this;
    }

    public IYoonMatrix SetMovementUnit(Integer dx, Integer dy)
    {
        Unit();
        Array[0][2] += dx;
        Array[1][2] += dy;
        return this;
    }

    public IYoonMatrix SetMovementUnit(IYoonVector2D<Integer> vec)
    {
        Unit();
        Array[0][2] += vec.GetX();
        Array[1][2] += vec.GetY();
        return this;
    }

    public IYoonMatrix SetRotateUnit(double dAngle)
    {
        Unit();
        int cosT = (int)Math.cos(dAngle);
        int sinT = (int)Math.sin(dAngle);
        Array[0][0] = cosT;
        Array[0][1] = -sinT;
        Array[1][0] = sinT;
        Array[1][1] = cosT;
        return this;
    }
}
