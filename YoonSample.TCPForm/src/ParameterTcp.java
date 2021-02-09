import com.yoonfactory.param.IYoonParameter;

public class ParameterTcp implements IYoonParameter {
    public String ipAddress;
    public String port;

    @Override
    public boolean isEqual(IYoonParameter iYoonParameter) {
        if (iYoonParameter instanceof ParameterTcp) {
            ParameterTcp pParam = (ParameterTcp) iYoonParameter;
            if (pParam.ipAddress == ipAddress &&
                    pParam.port == port)
                return true;
        }
        return false;
    }

    @Override
    public void copyFrom(IYoonParameter iYoonParameter) {
        if (iYoonParameter instanceof ParameterTcp) {
            ParameterTcp pParam = (ParameterTcp) iYoonParameter;
            ipAddress = pParam.ipAddress;
            port = pParam.port;
        }
    }
}
