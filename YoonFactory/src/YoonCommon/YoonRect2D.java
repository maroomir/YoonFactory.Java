package YoonCommon;

public class YoonRect2D implements IYoonRect<Double> {

    public YoonVector2D CenterPos;
    public double Width;
    public double Height;

    public IYoonRect Clone() {
        YoonRect2D r = new YoonRect2D();
        r.CenterPos = (YoonVector2D) this.CenterPos.Clone();
        r.Width = this.Width;
        r.Height = this.Height;
        return r;
    }

    public void CopyFrom(IYoonRect pRect) {
        if (pRect instanceof YoonRect2D) {
            YoonRect2D rect = (YoonRect2D) pRect;
            CenterPos = (YoonVector2D) rect.CenterPos.Clone();
            Width = rect.Width;
            Height = rect.Height;
        }
    }

    public Double GetLeft() {
        return CenterPos.GetX() - Width / 2;
    }

    public Double GetTop() {
        return CenterPos.GetY() - Height / 2;
    }

    public Double GetRight() {
        return CenterPos.GetX() + Width / 2;
    }

    public Double GetBottom() {
        return CenterPos.GetY() + Height / 2;
    }

    public IYoonVector GetTopLeft() {
        return new YoonVector2D(CenterPos.GetX() - Width / 2, CenterPos.GetY() - Height / 2);
    }

    public IYoonVector GetTopRight() {
        return new YoonVector2D(CenterPos.GetX() + Width / 2, CenterPos.GetY() - Height / 2);
    }

    public IYoonVector GetBottomLeft() {
        return new YoonVector2D(CenterPos.GetX() - Width / 2, CenterPos.GetY() + Height / 2);
    }

    public IYoonVector GetBottomRight() {
        return new YoonVector2D(CenterPos.GetX() + Width / 2, CenterPos.GetY() + Height / 2);
    }

    public YoonRect2D() {
        CenterPos = new YoonVector2D();
        CenterPos.SetX(0.0);
        CenterPos.SetY(0.0);
        Width = 0;
        Height = 0;
    }

    public YoonRect2D(double dx, double dy, double dw, double dh) {
        CenterPos = new YoonVector2D();
        CenterPos.SetX(dx);
        CenterPos.SetY(dy);
        Width = dw;
        Height = dh;
    }

    public Double Area() {
        return Width * Height;
    }
}