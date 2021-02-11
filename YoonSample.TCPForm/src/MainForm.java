import javax.swing.*;

import com.yoonfactory.eYoonStatus;
import com.yoonfactory.file.FileFactory;
import com.yoonfactory.log.*;
import com.yoonfactory.comm.*;
import com.yoonfactory.param.YoonParameter;

import java.awt.*;
import java.lang.reflect.Parameter;

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
        //// Create and Init Form
        MainForm pForm = new MainForm();
        pForm.CreateFrame();
        pForm.InitFrame();
    }

    @Override
    public void OnProcessLogEvent(String s, Color color) {
        textAreaMessageBox.setCaretColor(color);
        textAreaMessageBox.append(s);
    }

    @Override
    public void onReceiveMessageEvent(String s) {
        m_sbReceiveMessage.append(s + FileFactory.getLineSeparator());
    }

    @Override
    public void onShowMessageEvent(eYoonStatus eYoonStatus, String s) {
        PrintMessage(eYoonStatus, s);
    }

    public void CreateFrame() {
        m_pFrame = new JFrame("MainForm");
        m_pFrame.setContentPane(panelForm);
        m_pFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m_pFrame.pack();
        m_pFrame.setSize(FORM_WIDTH, FORM_HEIGHT);
        m_pFrame.setVisible(true);
    }

    public void InitFrame() {
        //// Event Subscription
        LogEventHandler.addProcessLogListener(this);
        CommEventHandler.addReceiveMessageListener(this);
        CommEventHandler.addShowMessageListener(this);
        PrintMessage(eYoonStatus.Info, "Subscription Completed");
        //// Load Parameter
        CommonClass.pParamTcp.loadParameter(true);
        ParameterTcp pParam = (ParameterTcp) CommonClass.pParamTcp.Parameter;
        textFieldIPAddress.setText(pParam.ipAddress);
        textFieldPort.setText(pParam.port);
    }

    public void PrintMessage(eYoonStatus nStatus, String strMessage) {
        CommonClass.pCLM.write(strMessage);
        CommonClass.pDLM.write(nStatus, strMessage + FileFactory.getLineSeparator(), false);
    }
}