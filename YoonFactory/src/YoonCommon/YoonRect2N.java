package YoonCommon;

public class YoonRect2N implements IYoonRect<Integer> {

    public YoonVector2N CenterPos;
    public int Width;
    public int Height;

    public IYoonRect Clone() {
        YoonRect2N r = new YoonRect2N();
        r.CenterPos = (YoonVector2N) this.CenterPos.Clone();
        r.Width = this.Width;
        r.Height = this.Height;
        return r;
    }

    public void CopyFrom(IYoonRect pRect) {
        if (pRect instanceof YoonRect2N) {
            YoonRect2N rect = (YoonRect2N) pRect;
            CenterPos = (YoonVector2N) rect.CenterPos.Clone();
            Width = rect.Width;
            Height = rect.Height;
        }
    }

    public Integer GetLeft() {
        return CenterPos.GetX() - Width / 2;
    }

    public Integer GetTop() {
        return CenterPos.GetY() - Height / 2;
    }

    public Integer GetRight() {
        return CenterPos.GetX() + Width / 2;
    }

    public Integer GetBottom() {
        return CenterPos.GetY() + Height / 2;
    }

    public IYoonVector GetTopLeft() {
        return new YoonVector2N(CenterPos.GetX() - Width / 2, CenterPos.GetY() - Height / 2);
    }

    public IYoonVector GetTopRight() {
        return new YoonVector2N(CenterPos.GetX() + Width / 2, CenterPos.GetY() - Height / 2);
    }

    public IYoonVector GetBottomLeft() {
        return new YoonVector2N(CenterPos.GetX() - Width / 2, CenterPos.GetY() + Height / 2);
    }

    public IYoonVector GetBottomRight() {
        return new YoonVector2N(CenterPos.GetX() + Width / 2, CenterPos.GetY() + Height / 2);
    }

    public YoonRect2N() {
        CenterPos = new YoonVector2N();
        CenterPos.SetX(0);
        CenterPos.SetY(0);
        Width = 0;
        Height = 0;
    }

    public YoonRect2N(int dx, int dy, int dw, int dh) {
        CenterPos = new YoonVector2N();
        CenterPos.SetX(dx);
        CenterPos.SetY(dy);
        Width = dw;
        Height = dh;
    }

    public Integer Area() {
        return Width * Height;
    }
}