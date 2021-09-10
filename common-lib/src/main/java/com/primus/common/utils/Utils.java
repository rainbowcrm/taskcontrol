package com.primus.common.utils;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class Utils {

	public static Object[] MapToJSONArray(Map<? extends Object, ? extends Object> map) {
		if (Utils.isNullMap(map)) return null;
		int i = 0 ;
		Object [] obArray = new  Object [map.size()];
		for ( Object key : map.keySet()) {
			Object val = map.get(key);
		
			/*String 
			obArray[i ++ ] = ob;*/
			
		}
		return obArray;
	}
	
	public static Object[] listToArray(List<? extends Object> list) {
		if (Utils.isNullList(list)) return null;
		int i = 0 ;
		Object [] obArray = new  Object [list.size()];
		for ( Object ob : list) {
			
			obArray[i ++ ] = ob;
			
		}
		return obArray;
	}
	
	public static boolean isNullString(String str) {
		if (str ==null || "null".equalsIgnoreCase(str) || "".equals(str) ) {
			return true;
		}else
			return false;
	}
	
	public static boolean isNull(Object obj) {
		if (obj ==null || "null".equalsIgnoreCase(String.valueOf(obj)) || "".equals(String.valueOf(obj)) ) {
			return true;
		}else
			return false;
	}

	public static boolean getBooleanValue(String str) {
		if (isNull(str) ) return false ; 
		return Boolean.parseBoolean(str);
	}
	
	public static boolean isPositiveInt(String str) {
		if (isNullString(str)) return false ;
		try  {
			int val = Integer.parseInt(str) ;
			if  (val >=  0 )
				return true ;
			else
				return false;
		}catch (NumberFormatException ex) {
			return false ; 
		}
	}
	
	public static String getFormattedValue(Object str) {
		if  (str == null) return "" ;
 		 switch (str.getClass().getName()) {
		 case "String" :
			 return getFormattedValue((String) str);
		 }
		 return String.valueOf(str);
	}
	public static String getFormattedValue(String str) {
		 return isNullString(str)?"":str ; 
	}
	
	public static boolean isNullList(List lst) {
		if( lst == null || lst.size() ==0)
			return true;
		else
			return false;
		
	}

	public static boolean isNullCollection(Collection lst) {
		if( lst == null || lst.size() ==0)
			return true;
		else
			return false;
		
	}
	
	public static boolean isNullSet(Set set) {
		if( set == null || set.size() ==0)
			return true;
		else
			return false;
		
	}
	public static boolean isNullMap(Map map) {
		if( map == null || map.size() ==0)
			return true;
		else
			return false;
		
	}
	
	public static String dateToString(Date dt , String format )throws ParseException {
		if (dt == null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(dt);
	}
	
	public static Date stringToDate(String str , String format) throws ParseException {
			SimpleDateFormat sdf = new SimpleDateFormat(format); 
			return sdf.parse(str) ;
	}
	
	
	public static Object stringToType(String str , Class retType) throws Exception {
		if(isNullString(str) &&
			(!"double".equals(retType.getName()) && !"int".equals(retType.getName()) && !"long".equals(retType.getName()) && !"float".equals(retType.getName()) ) )
				return null ;
		else if (isNullString(str))
			 return 0;
		if("int".equals(retType.getName()) || java.lang.Integer.class.equals(retType) )  {
			if(str.contains(".")) {
				int intVal = (int) Double.parseDouble(str);
				return intVal;
			}else 
			return Integer.parseInt(str); 
		}else if("long".equals(retType.getName()) || java.lang.Long.class.equals(retType)){
			return Long.parseLong(str);
		}else if("java.util.Date".equals(retType.getName())){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(str) ;
		}else if("double".equals(retType.getName()) || java.lang.Double.class.equals(retType)){
			return Double.parseDouble(str);
		}else if("boolean".equals(retType.getName()) || java.lang.Boolean.class.equals(retType)){
			return Boolean.parseBoolean(str);
		}else if("float".equals(retType.getName()) || java.lang.Float.class.equals(retType)){
			return Float.parseFloat(str);
		}
		return str ;
	}
	
	public  static String getNodeValuefromXML(Element  section, String tag) {
		NodeList xmlSection = section.getElementsByTagName(tag);
		if (xmlSection != null && xmlSection.getLength() > 0 ) {
			Element element = (Element)xmlSection.item(0);
			if (element != null ) {
				String  nodeValue = element.getFirstChild().getNodeValue(); 
				return nodeValue ;
			}
		}
		
		return "";
	}
	
	public  static String getNodeValuefromXML(Element  section, String tag,int  index) {
		NodeList xmlSection = section.getElementsByTagName(tag);
		if (xmlSection != null && xmlSection.getLength() > index ) {
			Element element = (Element)xmlSection.item(index);
			if (element != null ) {
				String  nodeValue = element.getFirstChild().getNodeValue(); 
				return nodeValue ;
			}
		}
		
		return "";
	}
	
	public  static String getNodeValuefromXML(XMLElement  section, String tag) {
		XMLElement node = section.getFirstChildElement(tag);
		if (node != null) {
			return node.getValue() ;
		}
		return "";
	}
	
	public static String  initlower(String s) {
		if (s.contains(".")) {
			String firstPart = s.substring(0, s.indexOf("."));
			String laterPart = s.substring(s.indexOf(".")+1,s.length());
			return firstPart.substring(0, 1).toLowerCase() + firstPart.substring(1,firstPart.length()) + "." + initlower(laterPart);
		}else 
			return  s.substring(0, 1).toLowerCase() + s.substring(1,s.length());
	}
	
	public static String  initupper(String s) {
		if (s.contains(".")) {
			String firstPart = s.substring(0, s.indexOf("."));
			String laterPart = s.substring(s.indexOf(".")+1,s.length());
			return firstPart.substring(0, 1).toUpperCase() + firstPart.substring(1,firstPart.length()) + "." + initupper(laterPart);
		}else 
			return  s.substring(0, 1).toUpperCase() + s.substring(1,s.length());
	}
	
	public static Method getterMethod(Class className , String property) throws java.lang.NoSuchMethodException  {
		boolean noSuchMethod = false; 
		try {
			Method methodRead = className.getMethod("get" + Utils.initupper(property));
			noSuchMethod = true ;
			return methodRead;
		}catch (java.lang.NoSuchMethodException nsmEX) {
			noSuchMethod = false ;
		}
		Method methodRead = className.getMethod("is" + Utils.initupper(property));
		return methodRead ;
	}
	
	public static String getCommaSeperatedArray(List list, String quote) {
		StringBuffer objects = new StringBuffer () ;
		if (list == null) return "";
		for( int i = 0 ; i < list.size() ; i ++ ) {
			objects.append(quote + list.get(i) + quote);
			if( i < list.size() -1 )
				 objects.append( ",");
		}
		return objects.toString();
	}
	
	public static List<String> getListfromStringtokens(String string,String token )
	{
		List<String> ans = new ArrayList<String> ();
		if(isNullString(string)) return ans;
		StringTokenizer tokenizer  = new StringTokenizer(string,token);
		while(tokenizer.hasMoreTokens()) {
			ans.add(tokenizer.nextToken());
		}
		
		return ans;
	}
	
	public static java.sql.Date getSQLDate(Date date) 
	{
		return new java.sql.Date(date.getTime()) ;
	}
	
	public static Date getUtilDate(java.sql.Date date) 
	{
		return new Date(date.getTime()) ;
	}
}
