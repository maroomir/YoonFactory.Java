package YoonLog;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import YoonFile.FileFactory;

public class YoonScribe implements IYoonLog{
    private String m_strRootDirectory = "";
    private int m_nFileExistDays;

    @Override
    public String getRootDirectory() {
        return m_strRootDirectory;
    }

    @Override
    public void setRootDirectory(String strRootDirectory) {
        this.m_strRootDirectory = strRootDirectory;
    }

    public YoonScribe()
    {
        m_nFileExistDays = 1;
    }

    public YoonScribe(int nDays)
    {
        m_nFileExistDays = nDays;
    }

    public YoonScribe(String strDirectory, int nDays)
    {
        m_strRootDirectory = strDirectory;
        m_nFileExistDays = nDays;
    }

    @Override
    public void write(String strMessage)
    {
        write(strMessage, true);
    }

    public void write(String strMessage, boolean bSave) {
        SimpleDateFormat pDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar pCalNow = Calendar.getInstance();
        String strMessageLine = "[" + pDateFormat.format(pCalNow.getTime()) + "]" + strMessage;
        System.out.println(strMessageLine);
        if (FileFactory.verifyDirectory(m_strRootDirectory) && bSave) {
            //// Erase the old files
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    FileFactory.deleteOldFilesInDirectory(m_strRootDirectory, m_nFileExistDays);
                }
            });
            executorService.shutdown();
            //// Create the new log files
            String strFilePath = Paths.get(m_strRootDirectory, Integer.toString(pCalNow.get(Calendar.YEAR)),
                    Integer.toString(pCalNow.get(Calendar.MONTH)), Integer.toString(pCalNow.get(Calendar.DAY_OF_MONTH)),
                    String.format("%d.txt", pCalNow.get(Calendar.HOUR_OF_DAY))).toString();
            if (FileFactory.verifyFilePath(strFilePath, true))
                FileFactory.appendTextToFile(strFilePath, strMessageLine);
        }
    }
}
