package com.primus.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.primus.common.exceptions.RadsException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class XMLDocument {

	XMLElement rootElement;

	public XMLElement getRootElement() {
		return rootElement;
	}

	public void setRootElement(XMLElement rootElement) {
		this.rootElement = rootElement;
	}
	
	
	private static void addAttributes (XMLElement element, Node nodeElement) {
		NamedNodeMap  attributeMap =   nodeElement.getAttributes() ;
		if (attributeMap == null )
			return;
		for (int i = 0 ; i < attributeMap.getLength() ; i ++  ) {
			Attr attribute = (Attr)attributeMap.item(i);
			element.addAttribute(attribute.getNodeName(), attribute.getValue());
		}
	}
	
	private static XMLElement converttoXMLElement(Node n) {
		XMLElement element = new XMLElement();
		if (n instanceof Element ) {
			Element nodeElement  = ((Element)n);
			element.setTag(nodeElement.getTagName());
			String nodeVal = nodeElement.getFirstChild() != null?nodeElement.getFirstChild().getNodeValue():nodeElement.getNodeValue();
			element.setValue(nodeVal);
		}   //   instanceof Element
		NodeList childNodes = n.getChildNodes();
		for (int i = 0 ; i < childNodes.getLength() ; i ++ ) {
			Node childNode =  (Node)childNodes.item(i);
			XMLElement childElement = converttoXMLElement(childNode);
			if (!childElement.isEmpty())
				element.addChildElement(childElement);
		}
			
		addAttributes(element, n);
		return element;
		
	}
	
	public static XMLDocument parseXMLString(String xmlData ) throws RadsException {
		XMLDocument document  = new XMLDocument() ;
		try {
			XMLElement element = null ;
			InputStream stream = new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stream);
			NodeList ls = doc.getDocumentElement().getChildNodes();
			for (int  i = 0 ;  i < ls.getLength() ; i  ++ ) 
			{
				Node node = ls.item(i);
				element =  converttoXMLElement(node.getOwnerDocument().getFirstChild());
				document.setRootElement(element);
				break ;
			}
		}catch (Exception ex) {
			throw new RadsException (ex);
		}
		return document ;
	}
	
	public static XMLDocument parse(String xmlFile ) throws  RadsException {
		XMLDocument document  = new XMLDocument() ;
		try {
			XMLElement element = null ;
			File fXmlFile = new File(xmlFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile); 
			NodeList ls = doc.getDocumentElement().getChildNodes();
			for (int  i = 0 ;  i < ls.getLength() ; i  ++ ) 
			{
				Node node = ls.item(i);
				element =  converttoXMLElement(node.getOwnerDocument().getFirstChild());
				document.setRootElement(element);
				break ;
			}
		}catch (Exception ex) {
			throw new RadsException (ex);
		}
		return document ;
	}
	
	public static XMLDocument parse(Document doc )  throws  RadsException {
		XMLDocument document  = new XMLDocument() ;
		try {
			XMLElement element = null ;
			NodeList ls = doc.getDocumentElement().getChildNodes();
			for (int  i = 0 ;  i < ls.getLength() ; i  ++ ) 
			{
				Node node = ls.item(i);
				element =  converttoXMLElement(node.getOwnerDocument().getFirstChild());
				document.setRootElement(element);
				break;
			}
		}catch (Exception ex) {
			throw new RadsException (ex);
		}
		return document ;
	}
	
}
