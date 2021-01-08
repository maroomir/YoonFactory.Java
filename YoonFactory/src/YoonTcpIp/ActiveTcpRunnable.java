package YoonTcpIp;

import YoonCommon.eYoonStatus;

import java.io.*;
import java.net.Socket;

import static YoonTcpIp.eStepTcpThread.*;

class ActiveTcpRunnable implements Runnable {
    private Socket m_pSocket = null;
    private eStepTcpThread m_nStepThread = Wait;
    private eStepTcpThread m_nStepThreadReserve = Wait;
    private BufferedReader m_pReader = null;
    private BufferedWriter m_pWriter = null;
    private String m_strSendMessage = "";
    private String m_strReceiveMessage = "";

    public eStepTcpThread getThreadStep() {
        return m_nStepThread;
    }

    public ActiveTcpRunnable(Socket pSocket) throws IOException {
        m_pSocket = pSocket;
        m_pReader = new BufferedReader(new InputStreamReader(m_pSocket.getInputStream()));
        m_pWriter = new BufferedWriter(new OutputStreamWriter(m_pSocket.getOutputStream()));
    }

    public boolean send(String strBuffer) throws InterruptedException {
        return send(strBuffer, true);
    }

    public boolean send(String strBuffer, boolean bIgnoreError) throws InterruptedException {
        m_strSendMessage = strBuffer;
        m_nStepThread = Send;
        while (m_nStepThread == Send) Thread.sleep(10);
        if (m_nStepThread == Wait) {
            ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Send, "Send Message : " + m_strSendMessage);
            m_nStepThreadReserve = Receive;
            return true;
        } else {
            ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Error, "Send Failure : Socket Error");
            if (bIgnoreError) m_nStepThreadReserve = Receive;
            return false;
        }
    }

    public void close() {
        try {
            m_nStepThreadReserve = Exit;
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (m_pSocket == null) return;
        boolean bRun = true;
        m_nStepThread = Receive;
        while (bRun) {
            if (m_nStepThreadReserve != Wait && m_nStepThreadReserve != m_nStepThread)
                m_nStepThread = m_nStepThreadReserve;
            switch (m_nStepThread) {
                case Wait:
                    break;
                case Send:
                    try {
                        m_pWriter.write(m_strSendMessage);
                        m_pWriter.newLine();
                        m_pWriter.flush();
                        m_nStepThread = Wait;
                    } catch (IOException e) {
                        e.printStackTrace();
                        m_nStepThread = Error;
                    }
                    break;
                case Receive:
                    try {
                        String strMessage = m_pReader.readLine();
                        if (m_strReceiveMessage != strMessage) {
                            m_strReceiveMessage = strMessage;
                            ReceiveMessageEventHandler.CallEvent(ActiveTcpRunnable.class, strMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        m_nStepThread = Error;
                    }
                    break;
                case Error:
                    break;
                case Exit:
                    bRun = false;
                    break;
                default:
                    break;
            }
        }
    }
}
