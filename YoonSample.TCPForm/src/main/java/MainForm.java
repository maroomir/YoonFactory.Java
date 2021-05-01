import javax.swing.*;

import com.yoonfactory.eYoonStatus;
import com.yoonfactory.file.FileFactory;
import com.yoonfactory.log.*;
import com.yoonfactory.comm.*;
import com.yoonfactory.param.YoonParameter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm implements IProcessLogEventListener, IReceiveMessageEventListener, IShowMessageEventListener {
    private JButton buttonDisconnect;
    private JButton buttonConnect;
    private JButton buttonClose;
    private JButton buttonOpen;
    private JLabel labelTitle;
    private JButton buttonApplyIP;
    private JButton buttonApplyPort;
    private JTextField textFieldSendMessage;
    private JButton buttonSendMessage;
    private JTextArea textAreaMessageBox;
    private JPanel panelForm;
    private JTextField textFieldIPAddress;
    private JTextField textFieldPort;

    private final int FORM_WIDTH = 800;
    private final int FORM_HEIGHT = 480;
    private static JFrame m_pFrame;
    private StringBuilder m_sbReceiveMessage;

    public static void main(String args[]) {
        //// Init YoonFactory
        CommonClass.pCLM = new YoonConsoler();
        CommonClass.pDLM = new YoonDisplayer();
        CommonClass.pCLM.setRootDirectory("log");
        CommonClass.pDLM.setRootDirectory("log");
        CommonClass.pParamTcp = new YoonParameter(new ParameterTcp(), ParameterTcp.class);
        CommonClass.pParamTcp.RootDirectory = "param";
        CommonClass.pServer = new YoonServer();
        CommonClass.pClient = new YoonClient();
        //// Create and Init Form
        MainForm pForm = new MainForm();
        pForm.createFrame();
        pForm.initFrame();
    }

    @Override
    public void onProcessLogEvent(String s, Color color) {
        textAreaMessageBox.setBackground(color);
        textAreaMessageBox.append(s);
    }

    @Override
    public void onReceiveMessageEvent(String s) {
        m_sbReceiveMessage.append(s + FileFactory.getLineSeparator());
    }

    @Override
    public void onShowMessageEvent(eYoonStatus eYoonStatus, String s) {
        printMessage(eYoonStatus, s);
    }

    public void createFrame() {
        m_pFrame = new JFrame("MainForm");
        m_pFrame.setContentPane(panelForm);
        m_pFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m_pFrame.pack();
        m_pFrame.setSize(FORM_WIDTH, FORM_HEIGHT);
        m_pFrame.setVisible(true);
    }

    public void initFrame() {
        //// Event Subscription
        LogEventHandler.addProcessLogListener(this);
        CommEventHandler.addReceiveMessageListener(this);
        CommEventHandler.addShowMessageListener(this);
        printMessage(eYoonStatus.Info, "Subscription Completed");
        //// Load Parameter
        CommonClass.pParamTcp.loadParameter(true);
        ParameterTcp pParam = (ParameterTcp) CommonClass.pParamTcp.Parameter;
        textFieldIPAddress.setText(pParam.ipAddress);
        textFieldPort.setText(Integer.toString(pParam.port));
        printMessage(eYoonStatus.Info, "Parameter Load");
        //// Create Communication Action Event
        buttonCommClickListener pCommListener = new buttonCommClickListener();
        buttonConnect.addActionListener(pCommListener);
        buttonDisconnect.addActionListener(pCommListener);
        buttonOpen.addActionListener(pCommListener);
        buttonClose.addActionListener(pCommListener);
        buttonSendMessage.addActionListener(pCommListener);
        printMessage(eYoonStatus.Info, "Create Comm Action Event");
        //// Create Param Apply Action Event
        buttonApplyParamClickListener pParamListener = new buttonApplyParamClickListener();
        buttonApplyIP.addActionListener(pParamListener);
        buttonApplyPort.addActionListener(pParamListener);
        printMessage(eYoonStatus.Info, "Create Param Apply Action Event");
    }

    public void printMessage(eYoonStatus nStatus, String strMessage) {
        CommonClass.pCLM.write(strMessage);
        CommonClass.pDLM.write(nStatus, strMessage + FileFactory.getLineSeparator(), false);
    }

    class buttonCommClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ParameterTcp pParam = (ParameterTcp)CommonClass.pParamTcp.Parameter;
            switch (e.getActionCommand()){
                case "Connect":
                    printMessage(eYoonStatus.Conform, "Connect Event Start");
                    pParam.isServer = false;
                    CommonClass.pClient.connect(pParam.ipAddress, pParam.port);
                    break;
                case "Disconnect":
                    printMessage(eYoonStatus.Conform, "Disconnect Event Start");
                    CommonClass.pClient.close();
                    break;
                case "Open":
                    printMessage(eYoonStatus.Conform, "Open Event Start");
                    pParam.isServer = true;
                    CommonClass.pServer.listen(pParam.port);
                    break;
                case "Close":
                    printMessage(eYoonStatus.Conform, "Close Event Start");
                    CommonClass.pServer.close();
                    break;
                case "Send":
                    printMessage(eYoonStatus.Conform, "Send Event Start");
                    if(pParam.isServer)
                        CommonClass.pServer.send(textFieldSendMessage.getText());
                    else
                        CommonClass.pClient.send(textFieldSendMessage.getText());
                    break;
            }
        }
    }

    class buttonApplyParamClickListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            ParameterTcp pParam = (ParameterTcp) CommonClass.pParamTcp.Parameter;
            pParam.ipAddress = textFieldIPAddress.getText();
            printMessage(eYoonStatus.Conform, "Apply IP : " + pParam.ipAddress);
            pParam.port = Integer.parseInt(textFieldPort.getText());
            printMessage(eYoonStatus.Conform, "Apply Port : " + Integer.toString(pParam.port));
            CommonClass.pParamTcp.Parameter = pParam;
            CommonClass.pParamTcp.saveParameter();
        }
    }
}