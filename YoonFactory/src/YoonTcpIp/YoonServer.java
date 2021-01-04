package YoonTcpIp;

import YoonCommon.eYoonStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class YoonServer implements IYoonTcpIp {

    public IShowMessageEventListener OnShowMessageEvent;
    public IShowMessageEventListener OnShowReceiveDataEvent;

    private final int BUFFER_SIZE = 4096;
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

    public void CopyFrom(IYoonTcpIp pTcpIp) {
        if (pTcpIp instanceof YoonServer) {
            YoonServer pServer = (YoonServer) pTcpIp;
            Close();
            if (pServer.GetIsConnected())
                pServer.Close();
            m_nPort = pServer.GetPort();
        }
    }

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

    public boolean ListenAndConnect() {
        try
        {
            if (m_serverSocket != null && m_serverSocket.isBound() == true)
                m_serverSocket.close();
            m_serverSocket = new ServerSocket();
            if(!GetIsRetryOpen())
                ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Info, String.format("Listen Port : %d", m_nPort), false);
            //// Binding port and Listening per backlogging
            m_serverSocket.bind(new InetSocketAddress(m_nPort), m_nBacklog;
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

    Thread m_threadRetryListen = null;
    Stopwatch m_StopWatch = new Stopwatch();
    /// <summary>
    /// 재연결을 시도하기 위한 Thread를 작성 및 구동한다.
    /// </summary>
    public void OnRetryThreadStart()
    {
        if (Param.fRetryListen == Boolean.FalseString)
            return;
        m_threadRetryListen = new Thread(new ThreadStart(ProcessRetry));
        m_threadRetryListen.Name = "Retry Listen";
        m_threadRetryListen.Start();
    }

    public void OnRetryThreadStop()
    {
        if (m_threadRetryListen == null) return;

        if (m_threadRetryListen.IsAlive)
            m_threadRetryListen.Abort();
    }

    /// <summary>
    /// 재연결 시도 Thread에 들어가는 Core 함수다.
    /// </summary>
    private void ProcessRetry()
    {
        //
    }

    /// <summary>
    ///  Buffer 상의 내용을 보낸다.
    /// </summary>
    /// <param name="strBuffer">전송할 내용</param>
    public bool Send(string strBuffer)
    {
        if (m_serverSocket == null || m_connectedClientSocket == null)
            return false;
        if (m_connectedClientSocket.Connected == false)
        {
            OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Error, "Send Failure : Connection Fail"));
            return false;
        }
        IsSend = false;
        // 추가 정보를 넘기기 위한 변수 선언
        // 크기를 설정하는게 의미가 없습니다.
        // 왜냐하면 바로 밑의 코드에서 문자열을 유니코드 형으로 변환한 바이트 배열을 반환하기 때문에
        // 최소한의 크기르 배열을 초기화합니다.
        AsyncObject ao = new AsyncObject(1);

        //// 문자열을 Byte 배열(ASCII)로 변환해서 Buffer에 삽입한다.
        ao.Buffer = Encoding.ASCII.GetBytes(strBuffer);
        ao.WorkingSocket = m_connectedClientSocket;

        try
        {
            m_connectedClientSocket.BeginSend(ao.Buffer, 0, ao.Buffer.Length, SocketFlags.None, m_sendHandler, ao);
            OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Info, string.Format("Send Message To String : " + strBuffer)));
            return true;
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex.ToString());
            OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Error, "Send Failure : Client Socket Error"));
        }
        return false;
    }

    /// <summary>
    /// Data의 내용을 보낸다.
    /// </summary>
    /// <param name="pBuffer">전송할 내용</param>
    public bool Send(byte[] pBuffer)
    {
        if (m_serverSocket == null || m_connectedClientSocket == null)
            return false;
        if (m_connectedClientSocket.Connected == false)
        {
            OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Error, "Send Failure : Connection Fail"));
            return false;
        }
        IsSend = false;

        AsyncObject ao = new AsyncObject(1);

        string strBuff = Encoding.ASCII.GetString(pBuffer);
        //// 문자열을 Byte 배열(ASCII)로 변환해서 Buffer에 삽입한다.
        ao.Buffer = Encoding.ASCII.GetBytes(strBuff);
        ao.WorkingSocket = m_connectedClientSocket;

        try
        {
            m_connectedClientSocket.BeginSend(ao.Buffer, 0, ao.Buffer.Length, SocketFlags.None, m_sendHandler, ao);
            //strBuff.Replace("\0", "");
            //strBuff = "[S] " + strBuff;
            OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Info, string.Format("Send Message To String : " + strBuff)));
            return true;
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex.ToString());
            OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Error, "Send Failure : Client Socket Error"));
        }
        return false;
    }

    private void OnSendEvent(IAsyncResult ar)
    {
        if (m_serverSocket == null || m_connectedClientSocket == null) return;

        //// 넘겨진 추가 정보를 가져옵니다.
        AsyncObject ao = (AsyncObject)ar.AsyncState;
        if(!ao.WorkingSocket.Connected)
        {
            //// 예외가 발생하면 예외 정보 출력 후 함수를 종료한다.
            OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Error, string.Format("Send Failure : Socket Disconnect")));
            return;
        }

        //// 보낸 바이트 수를 저장할 변수 선언
        Int32 sentBytes;

        try
        {
            //// 자료를 전송하고, 전송한 바이트를 가져옵니다.
            sentBytes = ao.WorkingSocket.EndSend(ar);
            IsSend = true;
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex.ToString());

            //// 예외가 발생하면 예외 정보 출력 후 함수를 종료한다.
            OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Error, string.Format("Send Failure : Socket Error")));
            return;
        }

        if (sentBytes > 0)
        {
            //// 여기도 마찬가지로 보낸 바이트 수 만큼 배열 선언 후 복사한다.
            Byte[] msgByte = new Byte[sentBytes];
            Array.Copy(ao.Buffer, msgByte, sentBytes);

            OnShowMessageEvent(this, new MessageArgs(eYoonStatus.Info, string.Format("Send Success : {0}", Encoding.ASCII.GetString(msgByte))));
        }
    }

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
}
}
