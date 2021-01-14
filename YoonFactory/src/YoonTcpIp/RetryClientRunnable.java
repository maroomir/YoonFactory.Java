package YoonTcpIp;

import YoonCommon.eYoonStatus;
import org.springframework.util.StopWatch;

import java.net.Socket;

class RetryClientRunnable implements Runnable {

    private StopWatch m_pStopWatch = new StopWatch();
    private Socket m_clientSocket = null;
    private int m_nRetryCount = 100;
    private int m_nTimeout = 1000;
    private boolean m_bRetryOpen = false;

    public RetryClientRunnable(Socket pSocket, int nRetryCount, int nTimeout) {
        m_clientSocket = pSocket;
        m_nRetryCount = nRetryCount;
        m_nTimeout = nTimeout;
    }

    public void setRetryOpen(boolean bRetryOpen) {
        this.m_bRetryOpen = bRetryOpen;
    }

    @Override
    public void run() {
        if (m_clientSocket == null) return;
        m_pStopWatch.stop();
        m_pStopWatch.start();
        YoonTcpEventHandler.callShowMessageEvent(RetryClientRunnable.class, eYoonStatus.Info, "Retry Client Start");
        try {
            for (int iRetry = 0; iRetry < m_nRetryCount; iRetry++) {
                //// Error : Timeout
                if (m_pStopWatch.getTotalTimeMillis() >= m_nTimeout)
                    break;
                //// Error : Retry Listen is false suddenly
                if (!m_bRetryOpen)
                    break;
                ////  Success to connect
                if (m_clientSocket != null) {
                    if (m_clientSocket.isConnected() == true) {
                        YoonTcpEventHandler.callShowMessageEvent(RetryClientRunnable.class, eYoonStatus.Info, "Connection Retry Success");
                        m_bRetryOpen = false;
                        break;
                    }
                }
                YoonTcpEventHandler.callRetryOpenEvent(RetryClientRunnable.class);
            }
            m_pStopWatch.stop();

            if (m_clientSocket == null) {
                YoonTcpEventHandler.callShowMessageEvent(RetryClientRunnable.class, eYoonStatus.Error, "Connection Retry Failure : Connection Socket Empty");
                return;
            }
            if (m_clientSocket.isConnected() == false) {
                YoonTcpEventHandler.callShowMessageEvent(RetryClientRunnable.class, eYoonStatus.Error, "Connection Retry Failure : Connection Fail");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            YoonTcpEventHandler.callShowMessageEvent(RetryClientRunnable.class, eYoonStatus.Info, "Retry Thread Exit");
        }
    }
}