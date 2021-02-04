package com.yoonfactory.file;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

public class YoonXml implements IYoonFile {
    private String m_strFilePath;

    @Override
    public String getFilePath() {
        return m_strFilePath;
    }

    @Override
    public void copyFrom(IYoonFile pFile) {
        if (pFile instanceof YoonXml) {
            YoonXml pXml = (YoonXml) pFile;
            m_strFilePath = pXml.getFilePath();
        }
    }

    public YoonXml(String strFilePath) {
        m_strFilePath = strFilePath;
    }

    @Override
    public IYoonFile clone() {
        return new YoonXml(m_strFilePath);
    }

    @Override
    public boolean isFileExist() {
        AtomicReference<String> refStrPath = new AtomicReference<>(m_strFilePath);
        return FileFactory.verifyFileExtension(refStrPath, "xml", false, false);
    }

    public Document loadFile() throws IOException {
        if (!isFileExist()) return null;
        FileInputStream pStream = null;
        try {
            //// Parsing XML to Document
            pStream = new FileInputStream(new File(m_strFilePath));
            DocumentBuilderFactory pFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder pBuilder = pFactory.newDocumentBuilder();
            Document pDocXml = pBuilder.parse(pStream);
            pStream.close();
            return pDocXml;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pStream != null) pStream.close();
        }
        return null;
    }

    public boolean saveFile(Document pDocXml) throws IOException {
        if (!isFileExist()) return false;
        FileOutputStream pStream = null;
        try {
            pStream = new FileOutputStream(new File(m_strFilePath), false);
            TransformerFactory pFactory = TransformerFactory.newInstance();
            Transformer pTransform = pFactory.newTransformer();
            DOMSource pSource = new DOMSource(pDocXml);
            pTransform.transform(pSource, new StreamResult(pStream));
            pStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } finally {
            if (pStream != null) pStream.close();
        }
        return false;
    }
}