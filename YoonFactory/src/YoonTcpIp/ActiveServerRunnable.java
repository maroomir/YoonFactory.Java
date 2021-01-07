package YoonTcpIp;

import YoonCommon.eYoonStatus;

import java.io.*;
import java.net.Socket;

import static YoonTcpIp.eStepTcpThread.*;

class ActiveServerRunnable implements Runnable {
    private YoonServer Parants = null;
    private Socket m_pSocket = null;
    private eStepTcpThread m_nStepThread = Wait;
    private BufferedReader m_pReader = null;
    private BufferedWriter m_pWriter = null;
    private String m_strSendMessage = "";

    public eStepTcpThread getThreadStep() {
        return m_nStepThread;
    }

    public ActiveServerRunnable(YoonServer pParants, Socket pSocket) throws IOException {
        Parants = pParants;
        m_pSocket = pSocket;
        try {
            m_pReader = new BufferedReader(new InputStreamReader(m_pSocket.getInputStream()));
            m_pWriter = new BufferedWriter(new OutputStreamWriter(m_pSocket.getOutputStream()));
        } finally {
            //
        }
    }

    public boolean Send(String strBuffer) {
        m_strSendMessage = strBuffer;
        m_nStepThread = Send;
    }

    @Override
    public void run() {
        if (Parants == null || m_pSocket == null) return;
        boolean bRun = true;
        while (bRun) {
            switch (m_nStepThread) {
                case Wait:
                    break;
                case Send:
                    OutputStream pStream = null;
                    try {
                        pStream = m_pSocket.getOutputStream();
                        PrintWriter pWriter = new PrintWriter(pStream, true);
                        pWriter.println(m_strSendMessage);
                        Parants.setSend(true);
                        ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Send, "Send Message : " + m_strSendMessage);
                        m_nStepThread = Wait;
                    } catch (IOException e) {
                        e.printStackTrace();
                        Parants.setSend(false);
                        ShowMessageEventHandler.CallEvent(YoonServer.class, eYoonStatus.Error, "Send Failure : Socket Error");
                        m_nStepThread = Error;
                    } finally {
                        try {
                            if (pStream != null)
                                pStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Receive:
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
