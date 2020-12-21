package YoonCommon;

public interface IYoonTriangle<T> {
    T GetX();

    T SetX(T x);

    T GetY();

    T SetY(T y);

    T GetHeight();

    T SetHeight(T height);

    T Area();

    IYoonTriangle Clone();

    void CopyFrom(IYoonTriangle pTriangle);
}
