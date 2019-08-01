package com.zzrenfeng.jenkin.networktest;


import android.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 使用SAX方式解析XML控制器类
 */
public class ContentHandler extends DefaultHandler {

    private static final String TAG = "ContentHandler";

    private String nodeName;
    private StringBuilder id;
    private StringBuilder name;
    private StringBuilder version;

    /**
     * 开始XML解析时调用
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        id = new StringBuilder();
        name = new StringBuilder();
        version = new StringBuilder();
    }

    /**
     * 完成整个XML解析时调用
     * @throws SAXException
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    /**
     * 开始解析某个节点时调用
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //记录当前节点名
        nodeName = localName;
    }

    /**
     * 完成解析某个节点时调用
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if("app".equals(localName)) {
            Log.d(TAG, "========[XmlSAXParser]>>>> id is: " + id.toString().trim());
            Log.d(TAG, "========[XmlSAXParser]>>>> name is: " + name.toString().trim());
            Log.d(TAG, "========[XmlSAXParser]>>>> version is: " + version.toString().trim());
            //最后要将StringBuilder清空
            id.setLength(0);
            name.setLength(0);
            version.setLength(0);
        }
    }

    /**
     * 在获取节点中内容时调用（会被调用多次，一些换行符也被当做内容解析出来，需要在代码中做好控制）
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        //根据当前的节点名判断将内容添加到那个StringBuilder对象中
        if("id".equals(nodeName)) {
            id.append(ch, start, length);
        } else if("name".equals(nodeName)) {
            name.append(ch, start, length);
        } else if("version".equals(nodeName)) {
            version.append(ch, start, length);
        }
    }
}
