package YoonCommon;

public interface IYoonRect<T> {

    T GetLeft();

    T GetTop();

    T GetRight();

    T GetBottom();

    IYoonVector GetTopLeft();

    IYoonVector GetTopRight();

    IYoonVector GetBottomLeft();

    IYoonVector GetBottomRight();

    T Area();

    IYoonRect Clone();

    void CopyFrom(IYoonRect pRect);
}