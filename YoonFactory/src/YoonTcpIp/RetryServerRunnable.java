package YoonTcpIp;

import YoonCommon.eYoonStatus;
import org.springframework.util.StopWatch;

import java.net.ServerSocket;

class RetryServerRunnable implements Runnable {

    private StopWatch m_pStopWatch = new StopWatch();
    private ServerSocket m_serverSocket = null;
    private int m_nRetryCount = 100;
    private int m_nTimeout = 1000;
    private boolean m_bRetryOpen = false;

    public RetryServerRunnable(ServerSocket pServerSocket, int nRetryCount, int nTimeout) {
        m_serverSocket = pServerSocket;
        m_nRetryCount = nRetryCount;
        m_nTimeout = nTimeout;
    }

    public void setRetryOpen(boolean bRetryOpen) {
        this.m_bRetryOpen = bRetryOpen;
    }

    @Override
    public void run() {
        if (m_serverSocket == null) return;
        m_pStopWatch.stop();
        m_pStopWatch.start();
        YoonTcpEventHandler.callShowMessageEvent(RetryServerRunnable.class, eYoonStatus.Info, "Retry Server Start");
        try {
            for (int iRetry = 0; iRetry < m_nRetryCount; iRetry++) {
                //// Error : Timeout
                if (m_pStopWatch.getTotalTimeMillis() >= m_nTimeout)
                    break;
                //// Error : Retry Listen is false suddenly
                if (!m_bRetryOpen)
                    break;
                ////  Success to connect
                if (m_serverSocket != null) {
                    if (m_serverSocket.isBound() == true) {
                        YoonTcpEventHandler.callShowMessageEvent(RetryServerRunnable.class, eYoonStatus.Info, "Listen Retry Success");
                        m_bRetryOpen = false;
                        break;
                    }
                }
                YoonTcpEventHandler.callRetryOpenEvent(RetryServerRunnable.class);
            }
            m_pStopWatch.stop();

            if (m_serverSocket == null) {
                YoonTcpEventHandler.callShowMessageEvent(RetryServerRunnable.class, eYoonStatus.Error, "Listen Retry Failure : Listen Socket Empty");
                return;
            }
            if (m_serverSocket.isBound() == false) {
                YoonTcpEventHandler.callShowMessageEvent(RetryServerRunnable.class, eYoonStatus.Error, "Listen Retry Failure : Connection Fail");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            YoonTcpEventHandler.callShowMessageEvent(RetryServerRunnable.class, eYoonStatus.Info, "Retry Thread Exit");
        }
    }
}