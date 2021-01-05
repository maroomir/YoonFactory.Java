package YoonTcpIp;

import YoonCommon.eYoonStatus;

public interface IShowMessageEventListener {
    public void OnEvent(eYoonStatus nStatus, String strMessage);
}
