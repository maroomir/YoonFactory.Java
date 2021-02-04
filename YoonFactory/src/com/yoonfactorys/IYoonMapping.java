package com.yoonfactory;
import java.util.ArrayList;

public interface IYoonMapping {

    void copyFrom(IYoonMapping pMapping);

    int getWidth();

    int getHeight();

    IYoonVector getOffset();

    ArrayList<IYoonVector> getRealPoints();

    void setRealPoints(ArrayList<IYoonVector> pList);

    ArrayList<IYoonVector> getPixelPoints();

    void setPixelPoints(ArrayList<IYoonVector> pList);

    void setReferencePosition(IYoonVector pVecPixelPos, IYoonVector pVecRealPos);

    IYoonVector getPixelResolution(IYoonVector pVecPixelPos);   // mm/pixel

    IYoonVector toPixel(IYoonVector pVecRealPos);

    IYoonVector toReal(IYoonVector pVecPixelPos);
}