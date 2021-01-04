package YoonTcpIp;

import java.sql.Struct;

public class YoonTcpParam extends Struct {

    public final String Port = "1234";
    public final String Backlog = "5";
    public final String RetryCount = "10";
    public final String RetryListen = "true";
    public final String Timeout = "10000";
}
