package YoonTcpIp;

import YoonCommon.eYoonStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class YoonServer implements IYoonTcpIp {

    private final int BUFFER_SIZE = 4096;
    public IShowMessageEventListener OnShowMessageEvent;
    public IShowMessageEventListener OnShowReceiveDataEvent;
    private boolean m_bRetryOpen = false;
    private boolean m_bSend = false;
    private StringBuilder m_sbReceiveMessage = null;
    private String m_strAddress = "";
    private int m_nPort = 0;
    private int m_nBacklog = 5;
    private int m_nCountRetry = 10;
    private int m_nTimeout = 10000;
    private ServerSocket m_serverSocket = null;
    private Socket m_connectedClientSocket = null;

    public YoonServer()
    {
        // Initialize Manageable Variable
        m_sbReceiveMessage = new StringBuilder("");
    }

    @Override
    public void CopyFrom(IYoonTcpIp pTcpIp) {
        if (pTcpIp instanceof YoonServer) {
            YoonServer pServer = (YoonServer) pTcpIp;
            Close();
            if (pServer.GetIsConnected())
                pServer.Close();
            m_nPort = pServer.GetPort();
        }
    }

    @Override
    public IYoonTcpIp Clone()
    {
        Close();
        YoonServer pServer = new YoonServer();
        pServer.SetPort(m_nPort);
        return pServer;
    }

    public boolean GetIsBound() {
        if (m_serverSocket == null) return false;
        return m_serverSocket.isBound();
    }

    @Override
    public boolean GetIsConnected() {
        if (m_connectedClientSocket == null) return false;
        return m_connectedClientSocket.isConnected();
    }

    @Override
    public boolean Open() {
        return ListenAndConnect();
    }

    public boolean ListenAndConnect(int nPort) {
        m_nPort = nPort;
        return ListenAndConnect();
    }

    private Thread m_threadSocket = null;
    public boolean ListenAndConnect() {
        try
        {
            if (m_serverSocket != null && m_serverSocket.isBound() == true)
                m_serverSocket.close();
            m_serverSocket = new ServerSocket();
            if(!GetIsRetryOpen())
                ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Info, String.format("Listen Port : %d", m_nPort), false);
            //// Binding port and Listening per backlogging
            m_serverSocket.bind(new InetSocketAddress(m_nPort), m_nBacklog);
            m_serverSocket.setReceiveBufferSize(BUFFER_SIZE);
            //// Accept the connection socket
            m_connectedClientSocket = m_serverSocket.accept();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Error, ex.toString());
            m_bRetryOpen = false;
            if (m_serverSocket != null)
                m_serverSocket = null;
            return false;
        }
        if (m_serverSocket.isBound() && m_connectedClientSocket != null) {
            ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Info, "Listen Success");
            m_bRetryOpen = false;
        }
        else {
            if (!m_bRetryOpen)
                ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Info, "Bound Failure");
            m_bRetryOpen = true;
        }
        return m_serverSocket.isBound();
    }

    @Override
    public void Close() {
        if (m_bRetryOpen)
            m_bRetryOpen = false;
        ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Info, "Close Listen");
        if (m_serverSocket == null)
            return;
        try {
            m_serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Error, e.getMessage());
        }
        m_serverSocket = null;
    }

    private Thread m_threadRetryListen = null;
    public void OnStartRetryThread() {
        if (!m_bRetryOpen || m_threadRetryListen.isAlive()) return;
        RetryServerRunnable pRunnable = new RetryServerRunnable();
        pRunnable.setParents(this);
        pRunnable.setServerSocket(m_serverSocket);
        pRunnable.setRetryCount(m_nCountRetry);
        pRunnable.setTimeout(m_nTimeout);
        m_threadRetryListen = new Thread(pRunnable);
        m_threadRetryListen.setName("Retry Listen");
        m_threadRetryListen.start();
    }

    public void OnStopRetryThread()
    {
        if (m_threadRetryListen == null) return;
        if (m_threadRetryListen.isAlive())
            m_threadRetryListen.interrupt();
    }

    @Override
    public boolean Send(String strBuffer) {
        if (m_serverSocket == null || m_connectedClientSocket == null)
            return false;
        if (m_connectedClientSocket.isConnected() == false) {
            ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Error, "Send Failure : Connection Fail");
            return false;
        }
        m_bSend = false;
        OutputStream pStream = null;
        try {
            pStream = m_connectedClientSocket.getOutputStream();
            PrintWriter pWriter = new PrintWriter(pStream, true);
            pWriter.println(strBuffer);
            ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Send, "Send Message : " + strBuffer);
            m_bSend = true;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Error, "Send Failure : Socket Error");
        } finally {
            try {
                if (pStream != null)
                    pStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public boolean Send(byte[] pBuffer) {
        if (m_serverSocket == null || m_connectedClientSocket == null)
            return false;
        if (m_connectedClientSocket.isConnected() == false) {
            ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Error, "Send Failure : Connection Fail");
            return false;
        }
        m_bSend = false;
        OutputStream pStream = null;
        try {
            pStream = m_connectedClientSocket.getOutputStream();
            PrintWriter pWriter = new PrintWriter(pStream, true);
            String strSendMessage = String.valueOf(pBuffer);
            pWriter.println(strSendMessage);
            ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Send, "Send Message : " + strSendMessage);
            m_bSend = true;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Error, "Send Failure : Socket Error");
        } finally {
            try {
                if (pStream != null)
                    pStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
/*
    private void OnReceiveEvent(IAsyncResult ar)
    {
        if (m_serverSocket == null || m_connectedClientSocket == null) return;

        try
        {
            //// 동기화된 State Object에서 Socket과 State Object를 검색한다.
            AsyncObject ao = (AsyncObject)ar.AsyncState;
            if (!ao.WorkingSocket.Connected)
            {
                //// 예외가 발생하면 예외 정보 출력 후 함수를 종료한다.
                OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Error, string.Format("Receive Failure : Socket Disconnect")));
                return;
            }

            //// Remote Device에서 Data를 읽어온다.
            int bytesRead = ao.WorkingSocket.EndReceive(ar);
            if (bytesRead > 0)
            {
                ////// 더 많은 Data가 있을 수 있으므로 현재까지의 Data를 저장한다.
                sbReceiveMessage.Append(Encoding.ASCII.GetString(ao.Buffer, 0, bytesRead));
                //// 자료 처리가 끝났으면 이제 다시 데이터를 수신받기 위해서 수신 대기를 해야 합니다.
                //// BeginReceive 메서드를 이용해 비동기적으로 작업을 대기했다면,
                //// 반드시 대리자 함수에서 EndReceive 메서드를 이용해 비동기 작업이 끝났다고 알려줘야 합니다!
                ao.WorkingSocket.BeginReceive(ao.Buffer, 0, ao.Buffer.Length, 0, m_receiveHandler, ao);

                byte[] buffer = new byte[bytesRead];
                System.Buffer.BlockCopy(ao.Buffer, 0, buffer, 0, buffer.Length);
                OnShowReceiveDataEvent(this, new BufferArgs(buffer));
                OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Info, string.Format("Receive Sucess : {0}", Encoding.ASCII.GetString(buffer))));
                //strRecv = Encoding.ASCII.GetString(state.buffer, 0, bytesRead);
            }
            else // 서버와 연결이 끊겼을 경우
            {
                //// 모든 Data가 도착했으므로, 응답(CallBack) 한다.
                //if (state.sb.Length > 1)
                //{
                //        strRecv = state.sb.ToString();
                //        ReceiveBufferEvent(strRecv);
                //}
                OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Error, "Receive Failure : Connection Fail"));
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex.ToString());

            OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Error, "Receive Failure: Socket Error"));
        }
    }
 */
}
