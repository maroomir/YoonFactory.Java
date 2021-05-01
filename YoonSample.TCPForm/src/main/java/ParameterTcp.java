import com.yoonfactory.param.IYoonParameter;

public class ParameterTcp implements IYoonParameter {
    public String ipAddress;
    public int port;
    public boolean isServer;

    public ParameterTcp() {
        ipAddress = "192.168.100.100";
        port = 5000;
        isServer = false;
    }

    @Override
    public boolean eqauls(IYoonParameter iYoonParameter) {
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
