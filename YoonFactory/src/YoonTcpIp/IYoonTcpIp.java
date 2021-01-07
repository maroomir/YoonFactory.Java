package YoonTcpIp;

public interface IYoonTcpIp {
    String getAddress();

    void setAddress(String strAddress);

    int getPort();

    void setPort(int nPort);

    int getRetryCount();

    int getTimeout();

    boolean isRetryOpen();

    boolean isSend();

    boolean isConnected();

    StringBuilder getReceiveMessage();

    void copyFrom(IYoonTcpIp pTcpIp);

    boolean send(String strBuffer);

    boolean send(byte[] pBuffer);

    boolean open();

    void close();
}
