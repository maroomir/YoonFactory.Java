package com.yoonfactory.comm;

import com.yoonfactory.eYoonStatus;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.*;
import java.util.TooManyListenersException;

class ActiveSerialRunnable implements Runnable, SerialPortEventListener {
    private SerialPort m_pSerial = null;
    private eStepCommThread m_nStepThread = eStepCommThread.Wait;
    private eStepCommThread m_nStepThreadReserve = eStepCommThread.Wait;
    private BufferedReader m_pReader = null;
    private BufferedWriter m_pWriter = null;
    private String m_strSendMessage = "";
    private String m_strReceiveMessage = "";

    public eStepCommThread getThreadStep() {
        return m_nStepThread;
    }

    public ActiveSerialRunnable(SerialPort pSerial) throws IOException, TooManyListenersException {
        m_pSerial = pSerial;
        m_pReader = new BufferedReader(new InputStreamReader(pSerial.getInputStream()));
        m_pWriter = new BufferedWriter(new OutputStreamWriter(pSerial.getOutputStream()));
        m_pSerial.addEventListener(this);
    }

    public boolean send(String strBuffer) throws InterruptedException {
        return send(strBuffer, true);
    }

    public boolean send(String strBuffer, boolean bIgnoreError) {
        m_strSendMessage = strBuffer;
        m_nStepThread = eStepCommThread.Send;
        while (m_nStepThread == eStepCommThread.Send) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        if (m_nStepThread == eStepCommThread.Wait) {
            CommEventHandler.callShowMessageEvent(ActiveSerialRunnable.class, eYoonStatus.Send, "Send Message : " + m_strSendMessage);
            return true;
        } else {
            CommEventHandler.callShowMessageEvent(ActiveSerialRunnable.class, eYoonStatus.Error, "Send Failure : Socket Error");
            if (bIgnoreError) m_nStepThreadReserve = eStepCommThread.Wait;
            return false;
        }
    }

    @Override
    public void serialEvent(SerialPortEvent pEvent) {
        if (pEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            m_nStepThread = eStepCommThread.Receive;
            while (m_nStepThread == eStepCommThread.Receive) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
            if (m_nStepThread == eStepCommThread.Wait)
                CommEventHandler.callShowMessageEvent(ActiveSerialRunnable.class, eYoonStatus.Receive, "Receive Message : " + m_strReceiveMessage);
            else
                CommEventHandler.callShowMessageEvent(ActiveSerialRunnable.class, eYoonStatus.Error, "Receive Failed : Serial Port Event Error");
        }
    }

    public void close() {
        try {
            m_nStepThreadReserve = eStepCommThread.Exit;
            Thread.sleep(100);
            m_pSerial.removeEventListener();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (m_pSerial == null) return;
        boolean bRun = true;
        m_nStepThread = eStepCommThread.Receive;
        while (bRun) {
            if (m_nStepThreadReserve != eStepCommThread.Wait && m_nStepThreadReserve != m_nStepThread)
                m_nStepThread = m_nStepThreadReserve;
            switch (m_nStepThread) {
                case Wait:
                    break;
                case Send:
                    try {
                        m_pWriter.write(m_strSendMessage);
                        m_pWriter.newLine();
                        m_pWriter.flush();
                        m_nStepThread = eStepCommThread.Wait;
                    } catch (IOException e) {
                        e.printStackTrace();
                        m_nStepThread = eStepCommThread.Error;
                    }
                    break;
                case Receive:
                    try {
                        String strMessage = m_pReader.readLine();
                        if (m_strReceiveMessage != strMessage) {
                            m_strReceiveMessage = strMessage;
                            CommEventHandler.callReceiveMessageEvent(ActiveSerialRunnable.class, strMessage);
                            m_nStepThread = eStepCommThread.Wait;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        m_nStepThread = eStepCommThread.Error;
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
