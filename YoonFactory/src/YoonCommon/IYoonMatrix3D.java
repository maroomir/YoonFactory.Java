package YoonCommon;

public  interface IYoonMatrix3D<T> extends IYoonMatrix<T> {

    IYoonMatrix SetScaleUnit(T sx, T sy, T sz);

    IYoonMatrix SetMovementUnit(T dx, T dy, T dz);

    IYoonMatrix SetMovementUnit(IYoonVector3D<T> pVector);

    IYoonMatrix SetRotateXUnit(double dAngle);

    IYoonMatrix SetRotateYUnit(double dAngle);

    IYoonMatrix SetRotateZUnit(double dAngle);
}