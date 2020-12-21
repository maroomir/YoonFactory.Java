package YoonCommon;

public interface  IYoonMatrix2D<T> extends IYoonMatrix<T> {
    IYoonMatrix SetScaleUnit(T sx, T sy);

    IYoonMatrix SetMovementUnit(T dx, T dy);

    IYoonMatrix SetMovementUnit(IYoonVector2D<T> pVector);

    IYoonMatrix SetRotateUnit(double dAngle);
}