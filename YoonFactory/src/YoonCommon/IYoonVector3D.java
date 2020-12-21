package YoonCommon;

public interface IYoonVector3D<T> extends IYoonVector<T>{
    T GetW();

    void SetW();

    T GetX();

    void SetX();

    T GetY();

    void SetY();

    T GetZ();

    void SetZ();

    IYoonVector Scale(T sx, T sy, T sz);

    IYoonVector Move(T dx, T dy, T dz);

    IYoonVector Move(IYoonVector pVector);

    IYoonVector RotateX(double dAngle);

    IYoonVector RotateY(double dAngle);

    IYoonVector RotateZ(double dAngle);
}