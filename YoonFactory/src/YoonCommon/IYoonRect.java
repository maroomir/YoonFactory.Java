package YoonCommon;

public interface IYoonRect<T> {

    T getLeft();

    T getTop();

    T getRight();

    T getBottom();

    IYoonVector getTopLeft();

    IYoonVector getTopRight();

    IYoonVector getBottomLeft();

    IYoonVector getBottomRight();

    T area();

    void copyFrom(IYoonRect pRect);
}