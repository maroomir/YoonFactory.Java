package YoonTcpIp;

import YoonCommon.eYoonStatus;
import org.springframework.util.StopWatch;

public class RetryServerThread extends Thread {

    private StopWatch m_pStopWatch = new StopWatch();
    private int m_nRetryCount = 100;
    private int m_nTimeout = 1000;

    public RetryServerThread(int nCount, int nTimeout)
    {
        m_nRetryCount = nCount;
        m_nTimeout = nTimeout;
    }

    @Override
    public void run() {
        m_pStopWatch.stop();
        m_pStopWatch.start();
        ShowMessageEventHandler.CallEvent(RetryServerThread.class, eYoonStatus.Info, "Retry Server Start");

        for (int iRetry = 0; iRetry < m_nRetryCount; iRetry++)
        {
            //// Error : Timeout
            if (m_pStopWatch.ElapsedMilliseconds >= nTimeOut)
                break;

            //// Error : Retry Listen is false suddenly
            if (!IsRetryOpen)
                break;

            ////  Success to connect
            if (m_serverSocket != null)
            {
                if (m_serverSocket.IsBound == true)
                {
                    OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Info, string.Format("Listen Retry Success")));
                    IsRetryOpen = false;
                    break;
                }
            }
            ListenAndConnect();
        }
        m_StopWatch.Stop();
        m_StopWatch.Reset();

        if (m_serverSocket == null)
        {
            OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Error, "Listen Retry Failure : Listen Socket Empty"));
            return;
        }
        if (m_serverSocket.IsBound == false)
        {
            OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Error, string.Format("Listen Retry Failure : Connection Fail")));
        }
    }
}