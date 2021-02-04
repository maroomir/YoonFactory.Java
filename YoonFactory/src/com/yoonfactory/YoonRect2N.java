package com.yoonfactory;

public class YoonRect2N implements IYoonRect<Integer> {

    public YoonVector2N CenterPos;
    public int Width;
    public int Height;

    @Override
    public IYoonRect clone() {
        YoonRect2N r = new YoonRect2N();
        r.CenterPos = (YoonVector2N) this.CenterPos.clone();
        r.Width = this.Width;
        r.Height = this.Height;
        return r;
    }

    public void copyFrom(IYoonRect pRect) {
        if (pRect instanceof YoonRect2N) {
            YoonRect2N rect = (YoonRect2N) pRect;
            CenterPos = (YoonVector2N) rect.CenterPos.clone();
            Width = rect.Width;
            Height = rect.Height;
        }
    }

    public Integer getLeft() {
        return CenterPos.getX() - Width / 2;
    }

    public Integer getTop() {
        return CenterPos.getY() - Height / 2;
    }

    public Integer getRight() {
        return CenterPos.getX() + Width / 2;
    }

    public Integer getBottom() {
        return CenterPos.getY() + Height / 2;
    }

    public IYoonVector getTopLeft() {
        return new YoonVector2N(CenterPos.getX() - Width / 2, CenterPos.getY() - Height / 2);
    }

    public IYoonVector getTopRight() {
        return new YoonVector2N(CenterPos.getX() + Width / 2, CenterPos.getY() - Height / 2);
    }

    public IYoonVector getBottomLeft() {
        return new YoonVector2N(CenterPos.getX() - Width / 2, CenterPos.getY() + Height / 2);
    }

    public IYoonVector getBottomRight() {
        return new YoonVector2N(CenterPos.getX() + Width / 2, CenterPos.getY() + Height / 2);
    }

    public YoonRect2N() {
        CenterPos = new YoonVector2N();
        CenterPos.setX(0);
        CenterPos.setY(0);
        Width = 0;
        Height = 0;
    }

    public YoonRect2N(int dx, int dy, int dw, int dh) {
        CenterPos = new YoonVector2N();
        CenterPos.setX(dx);
        CenterPos.setY(dy);
        Width = dw;
        Height = dh;
    }

    public Integer area() {
        return Width * Height;
    }
}