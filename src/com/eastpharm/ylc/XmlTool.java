package com.eastpharm.ylc;

import java.util.List;

import org.dom4j.*;
import com.alibaba.fastjson.*;


public class XmlTool {
	/**
	     * String ת org.dom4j.Document
	      * @param xml
	      * @return
	      * @throws DocumentException
	      */
	     public static Document strToDocument(String xml) throws DocumentException {
	         return DocumentHelper.parseText(xml);
	     }
	 
	     /**
	      * org.dom4j.Document ת  com.alibaba.fastjson.JSONObject
	      * @param xml
	      * @return
	      * @throws DocumentException
	      */
	     public static JSONObject documentToJSONObject(String xml) throws DocumentException {
	         return elementToJSONObject(strToDocument(xml).getRootElement());
	     }
	 
	     /**
	      * org.dom4j.Element ת  com.alibaba.fastjson.JSONObject
	      * @param node
	      * @return
	      */
	     public static JSONObject elementToJSONObject(Element node) {
	         JSONObject result = new JSONObject();
	         // ��ǰ�ڵ�����ơ��ı����ݺ�����
	         List<Attribute> listAttr = node.attributes();// ��ǰ�ڵ���������Ե�list
	         for (Attribute attr : listAttr) {// ������ǰ�ڵ����������
	             result.put(attr.getName(), attr.getValue());
	         }
	         // �ݹ������ǰ�ڵ����е��ӽڵ�
	         List<Element> listElement = node.elements();// ����һ���ӽڵ��list
	         if (!listElement.isEmpty()) {
	             for (Element e : listElement) {// ��������һ���ӽڵ�
	                 if (e.attributes().isEmpty() && e.elements().isEmpty()) // �ж�һ���ڵ��Ƿ������Ժ��ӽڵ�
	                     result.put(e.getName(), e.getTextTrim());// �]���򽫵�ǰ�ڵ���Ϊ�ϼ��ڵ�����ԶԴ�
	                 else {
	                     if (!result.containsKey(e.getName())) // �жϸ��ڵ��Ƿ���ڸ�һ���ڵ����Ƶ�����
	                         result.put(e.getName(), new JSONArray());// û���򴴽�
	                     ((JSONArray) result.get(e.getName())).add(elementToJSONObject(e));// ����һ���ڵ����ýڵ����Ƶ����Զ�Ӧ��ֵ��
	                 }
	             }
	         }
	         return result;
	     }
}