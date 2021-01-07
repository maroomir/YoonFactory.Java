package YoonTcpIp;

import YoonCommon.eYoonStatus;

public interface IShowMessageEventListener {
    public void onEvent(eYoonStatus nStatus, String strMessage);
}
