package com.yoonfactory.comm;

public interface IYoonSerial extends IYoonComm {
    String getPort();

    void setPort(int nPort);

    void setPort(String strPort);

    int getTimeout();

    void setTimeout(int nTimeout);

    int getBoardRate();

    void setBoardRate(int nRate);
}
