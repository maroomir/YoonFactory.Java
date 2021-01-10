package YoonFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

class FileFactory {

    public static boolean verifyDirectory(String path) {
        File pDir = new File(path);
        if (pDir.isDirectory()) {
            if (!pDir.exists()) {
                pDir.mkdirs();
                if (pDir.exists()) return true;
            } else return true;
        }
        return false;
    }

    public static boolean verifyFilePath(String path, Boolean bCreateFile) {
        File pFile = new File(path);
        FileOutputStream pStream = null;
        if (pFile.isFile()) {
            if (!verifyDirectory(pFile.getParent())) return false;
            try {
                if (!pFile.exists()) {
                    if (!bCreateFile) return false;
                    pStream = new FileOutputStream(pFile);
                    pStream.close();
                    return true;
                } else return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (pStream != null) pStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean verifyFileExtension(AtomicReference<String> refPath, String strExt, boolean bChangeExtension, boolean bCreateFile) {
        if (verifyFilePath(refPath.get(), bCreateFile)) {
            int nExtPos = refPath.get().lastIndexOf(".");
            String strExtPath = refPath.get().substring(nExtPos + 1);
            String strFilePathWithoutExt = refPath.get().substring(0, nExtPos);
            if (strExtPath != strExt) {
                if (!bChangeExtension) return false;
                File pFile = new File(refPath.get());
                String strFilePath = strFilePathWithoutExt + strExtPath;
                if (!verifyFilePath(strFilePath, bCreateFile)) return false;
                refPath.set(strFilePath);
                return true;
            } else
                return true;
        }
        return false;
    }

    public static boolean isFileExist(String path) {
        File pFile = new File(path);
        return pFile.exists();
    }

    public static ArrayList<String> getFileNamesInDirectory(String rootPath) {
        File pRootPath = new File(rootPath);
        ArrayList<String> pListFile = new ArrayList<>();
        if (pRootPath.isDirectory()) {
            if (!verifyDirectory(rootPath)) return pListFile;
            String[] pArrayFiles = pRootPath.list();
            if (pArrayFiles.length > 0) {
                for (int iFile = 0; iFile < pArrayFiles.length; iFile++) {
                    pListFile.add(pArrayFiles[iFile]);
                }
            }
        } else if (pRootPath.isFile()) {
            pListFile.add(rootPath);
        }
        return pListFile;
    }

    public static boolean setTextToFile(String path, String data) {
        FileOutputStream pStream = null;
        try {
            pStream = new FileOutputStream(path, false);
            pStream.write(data.getBytes(StandardCharsets.UTF_8));
            pStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pStream != null) pStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean appendTextToFile(String path, String data) {
        FileOutputStream pStream = null;
        try {
            pStream = new FileOutputStream(path, true);
            pStream.write(data.getBytes(StandardCharsets.UTF_8));
            pStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pStream != null) pStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean deleteFilePath(String filePath) {
        File pFile = new File(filePath);
        if (pFile.isFile()) {
            try {

                if (!verifyDirectory(pFile.getParent()))
                    return false;
                if (pFile.exists()) {
                    pFile.delete();
                    return true;
                } else
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void deleteExtensionFilesInDirectory(String dirPath, String strExt) {
        if (!verifyDirectory(dirPath)) {
            File pDir = new File(dirPath);
            FilenameFilter pFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase(Locale.ROOT).endsWith("." + strExt);
                }
            };
            File[] pArrayFiles = pDir.listFiles(pFilter);
            for (int iFile = 0; iFile < pArrayFiles.length; iFile++) {
                pArrayFiles[iFile].delete();
            }
        }
    }

    public static void deleteAllFilesInDirectory(String dirPath) {
        if (!verifyDirectory(dirPath)) {
            File pDir = new File(dirPath);
            try {
                while (pDir.exists()) {
                    File[] pArrayFiles = pDir.listFiles();
                    for (int iFile = 0; iFile < pArrayFiles.length; iFile++) {
                        pArrayFiles[iFile].delete();
                    }
                    if (pArrayFiles.length == 0 && pDir.isDirectory()) {
                        pDir.delete();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
