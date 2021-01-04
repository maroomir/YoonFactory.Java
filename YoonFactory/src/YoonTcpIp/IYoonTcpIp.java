package YoonTcpIp;

public interface IYoonTcpIp {
    String GetAddress();

    void SetAddress(String strAddress);

    int GetPort();

    void SetPort(int nPort);

    boolean GetIsRetryOpen();

    boolean GetIsSend();

    boolean GetIsConnected();

    StringBuilder GetReceiveMessage();

    void CopyFrom(IYoonTcpIp pTcpIp);

    IYoonTcpIp Clone();

    boolean Send(String strBuffer);

    boolean Send(byte[] pBuffer);

    boolean Open();

    void Close();
}
