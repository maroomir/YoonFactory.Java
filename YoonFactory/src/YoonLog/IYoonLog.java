package YoonLog;

import java.util.ArrayList;

public interface IYoonLog {
    String getRootDirectory();

    void setRootDirectory(String strRootDirectory);

    void write(String strMessage);
}
