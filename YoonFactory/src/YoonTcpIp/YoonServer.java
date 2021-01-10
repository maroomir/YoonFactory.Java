package YoonTcpIp;

import YoonCommon.eYoonStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class YoonServer implements IYoonTcpIp, IReceiveMessageEventListener, IRetryOpenEventListener {

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

    public YoonServer() {
        // Initialize Manageable Variable
        m_sbReceiveMessage = new StringBuilder("");
        // Event Subscription
        YoonTcpEventHandler.addRetryOpenListener(this);
        YoonTcpEventHandler.addReceiveMessageListener(this);
    }

    @Override
    public void onRetryOpenEvent() {
        listenAndConnect();
    }

    @Override
    public void onReceiveMessageEvent(String strReceiveMessage) {
        m_sbReceiveMessage = new StringBuilder(strReceiveMessage);
    }

    @Override
    public boolean isRetryOpen() {
        return m_bRetryOpen;
    }

    @Override
    public boolean isSend() {
        return m_bSend;
    }

    @Override
    public String getAddress() {
        return m_strAddress;
    }

    @Override
    public void setAddress(String m_strAddress) {
        this.m_strAddress = m_strAddress;
    }

    @Override
    public int getPort() {
        return m_nPort;
    }

    @Override
    public void setPort(int nPort) {
        this.m_nPort = nPort;
    }

    @Override
    public int getRetryCount() {
        return m_nCountRetry;
    }

    @Override
    public int getTimeout() {
        return m_nTimeout;
    }

    public boolean isBound() {
        if (m_serverSocket == null) return false;
        return m_serverSocket.isBound();
    }

    @Override
    public boolean isConnected() {
        if (m_connectedClientSocket == null) return false;
        return m_connectedClientSocket.isConnected();
    }

    @Override
    public StringBuilder getReceiveMessage() {
        return m_sbReceiveMessage;
    }

    @Override
    public void copyFrom(IYoonTcpIp pTcpIp) {
        if (pTcpIp instanceof YoonServer) {
            YoonServer pServer = (YoonServer) pTcpIp;
            close();
            if (pServer.isConnected())
                pServer.close();
            m_nPort = pServer.getPort();
        }
    }

    @Override
    public boolean open() {
        return listenAndConnect();
    }

    public boolean listenAndConnect(int nPort) {
        m_nPort = nPort;
        return listenAndConnect();
    }

    private Thread m_threadSocket = null;
    private ActiveTcpRunnable m_pRunnableSocket = null;
    public boolean listenAndConnect() {
        try {
            if (m_serverSocket != null && m_serverSocket.isBound() == true)
                m_serverSocket.close();
            m_serverSocket = new ServerSocket();
            if (!isRetryOpen())
                YoonTcpEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Info, String.format("Listen Port : %d", m_nPort), false);
            //// Binding port and Listening per backlogging
            m_serverSocket.bind(new InetSocketAddress(m_nPort), m_nBacklog);
            m_serverSocket.setReceiveBufferSize(BUFFER_SIZE);
            //// Accept the connection socket
            m_connectedClientSocket = m_serverSocket.accept();
            m_pRunnableSocket = new ActiveTcpRunnable(m_connectedClientSocket);
            m_threadSocket = new Thread(m_pRunnableSocket);
            m_threadSocket.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            YoonTcpEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Error, ex.toString());
            m_bRetryOpen = false;
            if (m_serverSocket != null)
                m_serverSocket = null;
            return false;
        }
        if (m_serverSocket.isBound() && m_connectedClientSocket != null) {
            YoonTcpEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Info, "Listen Success");
            m_bRetryOpen = false;
        } else {
            if (!m_bRetryOpen)
                YoonTcpEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Info, "Bound Failure");
            m_bRetryOpen = true;
        }
        return m_serverSocket.isBound();
    }

    @Override
    public void close() {
        if (m_bRetryOpen)
            m_bRetryOpen = false;
        YoonTcpEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Info, "Close Listen");
        if (m_serverSocket == null)
            return;
        try {
            OnStopRetryThread();
            m_pRunnableSocket.close();
            if (m_threadSocket.isAlive())
                m_threadSocket.interrupt();
            m_serverSocket.close();
            YoonTcpEventHandler.removeReceiveMessageListener(this);
            YoonTcpEventHandler.removeRetryOpenListener(this);
        } catch (IOException e) {
            e.printStackTrace();
            YoonTcpEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Error, e.getMessage());
        }
        m_serverSocket = null;
    }

    private Thread m_threadRetryListen = null;
    public void OnStartRetryThread() throws Exception {
        if (!m_bRetryOpen || m_threadRetryListen.isAlive()) return;
        m_threadRetryListen = new Thread(new RetryServerRunnable(m_serverSocket, m_nCountRetry, m_nTimeout));
        m_threadRetryListen.setName("Retry Listen");
        m_threadRetryListen.start();
    }

    public void OnStopRetryThread() {
        if (m_threadRetryListen == null) return;
        if (m_threadRetryListen.isAlive())
            m_threadRetryListen.interrupt();
        m_threadRetryListen = null;
    }

    @Override
    public boolean send(String strBuffer) {
        if (m_serverSocket == null || m_connectedClientSocket == null)
            return false;
        if (m_connectedClientSocket.isConnected() == false) {
            YoonTcpEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Error, "Send Failure : Connection Fail");
            return false;
        }
        try {
            m_bSend = m_pRunnableSocket.send(strBuffer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return m_bSend;
    }

    @Override
    public boolean send(byte[] pBuffer) {
        if (m_serverSocket == null || m_connectedClientSocket == null)
            return false;
        if (m_connectedClientSocket.isConnected() == false) {
            YoonTcpEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Error, "Send Failure : Connection Fail");
            return false;
        }
        try {
            String strBuffer = String.valueOf(pBuffer);
            m_bSend = m_pRunnableSocket.send(strBuffer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return m_bSend;
    }
}
