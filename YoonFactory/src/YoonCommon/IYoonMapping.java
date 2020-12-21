package YoonCommon;
import java.util.ArrayList;

public interface IYoonMapping {

    void CopyFrom(IYoonMapping pMapping);

    IYoonMapping Clone();

    int GetWidth();

    int GetHeight();

    IYoonVector GetOffset();

    ArrayList<IYoonVector> GetRealPoints();

    void SetRealPoints(ArrayList<IYoonVector> pList);

    ArrayList<IYoonVector> GetPixelPoints();

    void SetPixelPoints(ArrayList<IYoonVector> pList);

    void SetReferencePosition(IYoonVector pVecPixelPos, IYoonVector pVecRealPos);

    IYoonVector GetPixelResolution(IYoonVector pVecPixelPos);   // mm/pixel

    IYoonVector ToPixel(IYoonVector pVecRealPos);

    IYoonVector ToReal(IYoonVector pVecPixelPos);
}