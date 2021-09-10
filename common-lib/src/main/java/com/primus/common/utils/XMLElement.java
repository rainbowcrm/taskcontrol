package com.primus.common.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XMLElement {

	private String tag ;
	private String value ;
	private List<XMLElement> childElements ;  
	private XMLElement parent;
	private Map<String, String> attributeValues;
	
	
	public boolean isEmpty() {
		if (Utils.isNullString(tag) && Utils.isNullList(childElements) 
				&& Utils.isNullMap(attributeValues) )
			return true ;
		else
			return false;
		
			
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<XMLElement> getChildElements() {
		return childElements;
	}
	public void setChildElements(List<XMLElement> childElements) {
		this.childElements = childElements;
	}
	public XMLElement getParent() {
		return parent;
	}
	public void setParent(XMLElement parent) {
		this.parent = parent;
	}
	public Map<String, String> getAttributeValues() {
		return attributeValues;
	}
	public void setAttributeValues(Map<String, String> attributeValues) {
		this.attributeValues = attributeValues;
	}
	
	public String getAttributeValue(String attributeName) {
		if (!Utils.isNullMap(attributeValues) && !Utils.isNullString(attributeName) && 
				attributeValues.containsKey(attributeName)) {
			return attributeValues.get(attributeName) ;
		}
		return null;
	}
	
	public String getChildAttributeValue(String tag) {
		XMLElement  elem = getFirstChildElement(tag) ;
		if (elem != null)
			return elem.getValue() ;
		else
			return null;
	}
	
	public void addAttribute(String key, String value) {
		if (attributeValues == null)
			attributeValues = new HashMap<String, String>();
		attributeValues.put(key, value);
	}
	
	public XMLElement getFirstChildElement(String tag) {
		if(!Utils.isNullList(childElements) && !Utils.isNullString(tag) )
			for (XMLElement element : childElements) {
				if(tag.equalsIgnoreCase(element.getTag()))
					return element;
			}
		return null;
	}
	
	public List<XMLElement> getChildElements(String tag) {
		List<XMLElement> ans = new ArrayList<XMLElement>(); 
		if(!Utils.isNullList(childElements) && !Utils.isNullString(tag) ){
			for (XMLElement element : childElements) {
				if(tag.equalsIgnoreCase(element.getTag()))
					ans.add(element);
			}
		}
		return ans;
	}
	
	public List<XMLElement> getAllChildElements() {
		List<XMLElement> ans = new ArrayList<XMLElement>(); 
		if(!Utils.isNullList(childElements) && !Utils.isNullString(tag) ){
			for (XMLElement element : childElements) {
					ans.add(element);
			}
		}
		return ans;
	}
	
	public void addChildElement(XMLElement element)  {
		if(childElements == null) {
			childElements = new ArrayList<XMLElement>();
		}
		childElements.add(element);
	}
	
	
	public String toString() {
		return convertString(this).toString();
	}
	
	private static StringBuffer convertString(XMLElement element){
		StringBuffer ans = new StringBuffer();
		ans.append("\n<" + element.getTag());
		if (!Utils.isNullMap(element.getAttributeValues())) {
			Iterator it = element.getAttributeValues().keySet().iterator();
			 while (it.hasNext()) {
				 String key =String.valueOf( it.next());
				 String value = element.getAttributeValues().get(key);
				  ans.append( " " + key +  "=\"" + value +"\"");
			 }
			
		}
		 ans.append(">\n");
		if(!Utils.isNullList(element.getAllChildElements())) {
			for (XMLElement childElement : element.getAllChildElements()) {
				StringBuffer childAns = convertString(childElement);
				ans.append(childAns);
			}
		}
		if(!Utils.isNullString(element.getValue()))
		ans.append(element.getValue());
		ans.append("\n</" + element.getTag() + ">\n") ;
		return ans;
	}
}
