package YoonCommon;


public interface IYoonVector2D<T> extends IYoonVector<T>{
    T GetW();

    void SetW(T w);

    T GetX();

    void SetX(T x);

    T GetY();

    void SetY(T y);

    IYoonVector Scale(T sx, T sy);

    IYoonVector Move(T dx, T dy);

    IYoonVector Move(IYoonVector pVector);

    IYoonVector Rotate(double dAngle);

    IYoonVector Rotate(IYoonVector pVecCenter, double dAngle);
}
