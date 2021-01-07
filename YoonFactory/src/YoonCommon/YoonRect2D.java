package YoonCommon;

public class YoonRect2D implements IYoonRect<Double> {

    public YoonVector2D CenterPos;
    public double Width;
    public double Height;

    @Override
    public IYoonRect clone() {
        YoonRect2D r = new YoonRect2D();
        r.CenterPos = (YoonVector2D) this.CenterPos.clone();
        r.Width = this.Width;
        r.Height = this.Height;
        return r;
    }

    public void copyFrom(IYoonRect pRect) {
        if (pRect instanceof YoonRect2D) {
            YoonRect2D rect = (YoonRect2D) pRect;
            CenterPos = (YoonVector2D) rect.CenterPos.clone();
            Width = rect.Width;
            Height = rect.Height;
        }
    }

    public Double getLeft() {
        return CenterPos.getX() - Width / 2;
    }

    public Double getTop() {
        return CenterPos.getY() - Height / 2;
    }

    public Double getRight() {
        return CenterPos.getX() + Width / 2;
    }

    public Double getBottom() {
        return CenterPos.getY() + Height / 2;
    }

    public IYoonVector getTopLeft() {
        return new YoonVector2D(CenterPos.getX() - Width / 2, CenterPos.getY() - Height / 2);
    }

    public IYoonVector getTopRight() {
        return new YoonVector2D(CenterPos.getX() + Width / 2, CenterPos.getY() - Height / 2);
    }

    public IYoonVector getBottomLeft() {
        return new YoonVector2D(CenterPos.getX() - Width / 2, CenterPos.getY() + Height / 2);
    }

    public IYoonVector getBottomRight() {
        return new YoonVector2D(CenterPos.getX() + Width / 2, CenterPos.getY() + Height / 2);
    }

    public YoonRect2D() {
        CenterPos = new YoonVector2D();
        CenterPos.setX(0.0);
        CenterPos.setY(0.0);
        Width = 0;
        Height = 0;
    }

    public YoonRect2D(double dx, double dy, double dw, double dh) {
        CenterPos = new YoonVector2D();
        CenterPos.setX(dx);
        CenterPos.setY(dy);
        Width = dw;
        Height = dh;
    }

    public Double area() {
        return Width * Height;
    }
}