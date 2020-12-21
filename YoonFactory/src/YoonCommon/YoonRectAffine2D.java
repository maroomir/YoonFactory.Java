package YoonCommon;

import org.jetbrains.annotations.NotNull;

public class YoonRectAffine2D implements IYoonRect<Double> {

    private YoonVector2D m_vecCornerOrigin_TopLeft = new YoonVector2D();
    private YoonVector2D m_vecCornerOrigin_BottomLeft = new YoonVector2D();
    private YoonVector2D m_vecCornerOrigin_TopRight = new YoonVector2D();
    private YoonVector2D m_vecCornerOrigin_BottomRight = new YoonVector2D();
    private YoonVector2D m_vecCornerRotate_TopLeft = null;
    private YoonVector2D m_vecCornerRotate_BottomLeft = null;
    private YoonVector2D m_vecCornerRotate_TopRight = null;
    private YoonVector2D m_vecCornerRotate_BottomRight = null;
    private YoonVector2D m_vecCenter = new YoonVector2D(); // Set 내 InitOrigin과 충돌에 따른 StackOverflow 방지용
    private double m_dWidth = 0.0; // Set 내 InitOrigin과 충돌에 따른 StackOverflow 방지용
    private double m_dHeight = 0.0; // Set 내 InitOrigin과 충돌에 따른 StackOverflow 방지용
    private double m_dRotation = 0.0; // Set 내 InitOrigin과 충돌에 따른 StackOverflow 방지용

    public IYoonRect Clone() {
        YoonRectAffine2D r = new YoonRectAffine2D(0.0, 0.0, 0.0);
        r.SetCenterPos((YoonVector2D) m_vecCenter.Clone());
        r.SetWidth(m_dWidth);
        r.SetHeight(m_dHeight);
        r.SetRotation(m_dRotation);
        return r;
    }

    public void CopyFrom(IYoonRect pRect) {
        if (pRect instanceof YoonRectAffine2D) {
            YoonRectAffine2D rect = (YoonRectAffine2D) pRect;
            m_vecCenter = (YoonVector2D) rect.GetCenterPos().Clone();
            m_dWidth = rect.GetWidth();
            m_dHeight = rect.GetHeight();
            m_dRotation = rect.GetRotation();
        }
    }

    public YoonVector2D GetCenterPos() {
        return m_vecCenter;
    }

    public void SetCenterPos(YoonVector2D value) {
        m_vecCenter = value;
        InitRectOrigin(m_vecCenter, m_dWidth, m_dHeight);
    }

    public double GetWidth() {
        return m_dWidth;
    }

    public void SetWidth(double value) {
        m_dWidth = value;
        InitRectOrigin(m_vecCenter, m_dWidth, m_dHeight);
    }

    public double GetHeight() {
        return m_dHeight;
    }

    public void SetHeight(double value) {
        m_dHeight = value;
        InitRectOrigin(m_vecCenter, m_dWidth, m_dHeight);
    }

    public double GetRotation() {
        return m_dRotation;
    }

    public void SetRotation(double value) {
        m_dRotation = value;
        InitRectOrigin(m_vecCenter, m_dWidth, m_dHeight);

        m_vecCornerRotate_TopLeft = (YoonVector2D) m_vecCornerOrigin_TopLeft.Rotate(m_vecCenter, m_dRotation);
        m_vecCornerRotate_BottomLeft = (YoonVector2D) m_vecCornerOrigin_BottomLeft.Rotate(m_vecCenter, m_dRotation);
        m_vecCornerRotate_TopRight = (YoonVector2D) m_vecCornerOrigin_TopRight.Rotate(m_vecCenter, m_dRotation);
        m_vecCornerRotate_BottomRight = (YoonVector2D) m_vecCornerOrigin_BottomRight.Rotate(m_vecCenter, m_dRotation);
    }

    private void InitRectOrigin(YoonVector2D vecCenter, double dWidth, double dHeight) {
        m_vecCornerOrigin_TopLeft.SetX(vecCenter.GetX() - dWidth / 2);
        m_vecCornerOrigin_TopLeft.SetY(vecCenter.GetY() - dHeight / 2);
        m_vecCornerOrigin_TopRight.SetX(vecCenter.GetX() + dWidth / 2);
        m_vecCornerOrigin_TopRight.SetY(vecCenter.GetY() - dHeight / 2);
        m_vecCornerOrigin_BottomLeft.SetX(vecCenter.GetX() - dWidth / 2);
        m_vecCornerOrigin_BottomLeft.SetY(vecCenter.GetY() + dHeight / 2);
        m_vecCornerOrigin_BottomRight.SetX(vecCenter.GetX() + dWidth / 2);
        m_vecCornerOrigin_BottomRight.SetY(vecCenter.GetY() + dHeight / 2);
    }

    public Double GetLeft() {
        return m_vecCenter.GetX() - m_dWidth / 2;
    }

    public Double GetTop() {
        return m_vecCenter.GetY() - m_dHeight / 2;
    }

    public Double GetRight() {
        return m_vecCenter.GetX() + m_dWidth / 2;
    }

    public Double GetBottom() {
        return m_vecCenter.GetY() + m_dHeight / 2;
    }

    public IYoonVector GetTopLeft() {
        return m_vecCornerRotate_TopLeft;

    }

    public IYoonVector GetTopRight() {
        return m_vecCornerRotate_TopRight;
    }

    public IYoonVector GetBottomLeft() {
        return m_vecCornerRotate_BottomLeft;
    }

    public IYoonVector GetBottomRight() {
        return m_vecCornerRotate_BottomRight;
    }

    public YoonRectAffine2D(double dWidth, double dHeight, double dTheta) {
        m_vecCenter = new YoonVector2D();
        m_vecCenter.SetX(0.0);
        m_vecCenter.SetY(0.0);
        SetWidth(dWidth);
        SetHeight(dHeight);
        SetRotation(dTheta);
    }

    public YoonRectAffine2D(double dX, double dY, double dWidth, double dHeight, double dTheta) {
        m_vecCenter = new YoonVector2D();
        m_vecCenter.SetX(dX);
        m_vecCenter.SetY(dY);
        SetWidth(dWidth);
        SetHeight(dHeight);
        SetRotation(dTheta);
    }

    public YoonRectAffine2D(@NotNull YoonVector2D vecPos, double dWidth, double dHeight, double dTheta) {
        m_vecCenter = (YoonVector2D) vecPos.Clone();
        SetWidth(dWidth);
        SetHeight(dHeight);
        SetRotation(dTheta);
    }

    public Double Area() {
        return m_dWidth * m_dHeight;
    }
}