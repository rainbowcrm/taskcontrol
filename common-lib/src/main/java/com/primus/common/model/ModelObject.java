package com.primus.common.model;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import java.util.Set;

import com.primus.common.utils.Utils;
import com.primus.common.utils.XMLDocument;
import com.primus.common.utils.XMLElement;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.primus.common.annotations.RadsPropertySet;
import com.primus.common.context.IRadsContext;
import sun.awt.image.IntegerComponentRaster;


public abstract class ModelObject {
	
	
	public static ModelObject instantiateObjectfromJSON(String json,String className,IRadsContext context){
		try {
			JSONTokener  tokener = new JSONTokener(json);
			JSONObject root = new JSONObject(tokener);
			ModelObject object = (ModelObject)Class.forName(className).newInstance();
			Iterator itkeys = root.keys() ;
			while (itkeys.hasNext()) {
				String key  = (String) itkeys.next() ;
				Object childElement = root.opt(key);
				if (childElement == null)
					 continue;
				Method curMethod = getMethodfromJSONTag(object, Utils.initupper(key));
				if (curMethod == null ) 
					continue;
				if (ModelObject.class.isAssignableFrom(curMethod.getParameters()[0].getType())){
					ModelObject subObject = instantiateObjectfromJSON(childElement.toString(), curMethod.getParameters()[0].getType().getName(), context);
					curMethod.invoke(object, subObject);
				}else if ( (java.util.Set.class.isAssignableFrom(curMethod.getParameters()[0].getType()) ||
						java.util.List.class.isAssignableFrom(curMethod.getParameters()[0].getType()) ||
						java.util.Collection.class.isAssignableFrom(curMethod.getParameters()[0].getType()))&&
						childElement instanceof JSONArray ) {
					JSONArray array = (JSONArray) childElement;
					Collection subObjects ;
					if (java.util.Set.class.isAssignableFrom(curMethod.getParameters()[0].getType()))
						subObjects = new LinkedHashSet();
					else
						subObjects = new ArrayList();
					if (array != null && array.length() > 0 ) { 
						for  (int index = 0 ; index < array.length() ; index ++ ) {
							JSONObject childJSON = (JSONObject)array.get(index);
							String paramType = curMethod.getParameters()[0].getParameterizedType().getTypeName();
							if (!Utils.isNullString(paramType) && paramType.contains("<")) { 
								String paramClass = paramType.substring(paramType.indexOf("<")+1,paramType.length()-1);
								Object subObj = Class.forName(paramClass).newInstance() ;
								if (ModelObject.class.isAssignableFrom(subObj.getClass())){
									subObj = (Object ) instantiateObjectfromJSON(childJSON.toString(), paramClass, context) ;
								}
								subObjects.add(subObj);
							}
						}
						curMethod.invoke(object, subObjects);
					}
				}else{
					String valueStr = String.valueOf(childElement);
					if( !Utils.isNullString(valueStr) &&	!Utils.isNullString(valueStr)) {
						Object paramValue   = Utils.stringToType( valueStr, curMethod.getParameters()[0].getType());
						curMethod.invoke(object, paramValue);
					}
				}
			}
			return object;
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			return null;
	}
	
	private static Method getMethodforKey(ModelObject object, String propertyKey) {
		Map methods = object.getAllSetterMethods("MTH");
		Iterator keys = methods.keySet().iterator() ;
		while( keys.hasNext()) {
		Method method = (Method)methods.get(keys.next());
		 if (method.getName().equalsIgnoreCase("set" + Utils.initupper(propertyKey))) {
					return method; 
			}
		}
		return null;
	}
	
	private static Method getMethodfromXMLTag(ModelObject object, String xmlTag) {
		Map methods = object.getAllSetterMethods("XML");
		Iterator keys = methods.keySet().iterator() ;
		while( keys.hasNext()) {
			Method method = (Method)methods.get(keys.next());
			RadsPropertySet[] radsPropSet =(RadsPropertySet[] )method.getAnnotationsByType(RadsPropertySet.class);
			if (radsPropSet != null && radsPropSet.length > 0  &&  !Utils.isNullString(radsPropSet[0].xmlTag())
					&& xmlTag.equals(radsPropSet[0].xmlTag())) {
				return method;
			}else if (method.getName().equalsIgnoreCase("set" + Utils.initupper(xmlTag) )) {
					return method; 
			}
		}
		return null;
	}
	
	private static Method getMethodfromJSONTag(ModelObject object, String jsonTag) {
		Map methods = object.getAllSetterMethods("JSON");
		Iterator keys = methods.keySet().iterator() ;
		while( keys.hasNext()) {
			Method method = (Method)methods.get(keys.next());
			RadsPropertySet[] radsPropSet =(RadsPropertySet[] )method.getAnnotationsByType(RadsPropertySet.class);
			if (radsPropSet != null && radsPropSet.length > 0  &&  !Utils.isNullString(radsPropSet[0].jsonTag())
					&& jsonTag.equals(radsPropSet[0].jsonTag())) {
				return method;
			}else if (method.getName().equalsIgnoreCase("set" + Utils.initupper(jsonTag) )) {
					return method; 
			}
		}
		return null;
	}
	
	public static ModelObject instantiateObjectfromMap(Map<String,Object> map,String className,IRadsContext context){
		try {
			ModelObject object = (ModelObject)Class.forName(className).newInstance();
			Iterator it = map.keySet().iterator() ;
			while (it.hasNext()) {
				String key = String.valueOf (it.next()) ;
				Object value = map.get(key);
				Method curMethod  = getMethodforKey(object,key) ;
				if(ModelObject.class.isAssignableFrom(curMethod.getParameters()[0].getType())) {
					if (value instanceof  Map) {
						ModelObject subObject =instantiateObjectfromMap((Map)value,curMethod.getParameters()[0].getType().getName(),context);
						curMethod.invoke(object, subObject);
					}else
						curMethod.invoke(object, null);
				}else {
					Object paramValue   = Utils.stringToType( String.valueOf(value).trim(), curMethod.getParameters()[0].getType());
					if (paramValue != null && !paramValue.equals("null"))
						curMethod.invoke(object, paramValue);
				}
			}
			return object ;
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	public static ModelObject instantiateObjectfromXML(String xml,String className,IRadsContext context){
		try {
		ModelObject object = (ModelObject)Class.forName(className).newInstance();
		XMLDocument document = XMLDocument.parseXMLString(xml);
		XMLElement rootElement = document.getRootElement();
		for (XMLElement childElement : rootElement.getAllChildElements()) {
			Method curMethod = getMethodfromXMLTag(object,Utils.initupper(childElement.getTag()));
			if (curMethod == null ) 
				continue;
			if (ModelObject.class.isAssignableFrom(curMethod.getParameters()[0].getType())){
				ModelObject subObject = instantiateObjectfromXML(childElement.toString(), curMethod.getParameters()[0].getType().getName(), context);
				curMethod.invoke(object, subObject);
			}else if ( (java.util.Set.class.isAssignableFrom(curMethod.getParameters()[0].getType()) ||
					java.util.List.class.isAssignableFrom(curMethod.getParameters()[0].getType()) ||
					java.util.Collection.class.isAssignableFrom(curMethod.getParameters()[0].getType()))) {
				Collection subObjects ;
				if (java.util.Set.class.isAssignableFrom(curMethod.getParameters()[0].getType()))
					subObjects = new LinkedHashSet();
				else
					subObjects = new ArrayList();
				if (childElement != null && !Utils.isNullList(childElement.getAllChildElements()) ) { 
					for  (XMLElement grandChild : childElement.getAllChildElements() ) {
						String paramType = curMethod.getParameters()[0].getParameterizedType().getTypeName();
						if (!Utils.isNullString(paramType) && paramType.contains("<")) { 
							String paramClass = paramType.substring(paramType.indexOf("<")+1,paramType.length()-1);
							Object subObj = Class.forName(paramClass).newInstance() ;
							if (ModelObject.class.isAssignableFrom(subObj.getClass())){
								subObj = (Object ) instantiateObjectfromXML(grandChild.toString(), paramClass, context) ;
							}
							subObjects.add(subObj);
						}
					}
					curMethod.invoke(object, subObjects);
				}
			}else{
				if(!Utils.isNullString(childElement.getValue()) && !Utils.isNullString(childElement.getValue().trim())) {
					Object paramValue   = Utils.stringToType( childElement.getValue().trim(), curMethod.getParameters()[0].getType());
					curMethod.invoke(object, paramValue);
				}
			}
		}
		return object;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	
	public String toBusinessKeyXML(IRadsContext context)  {
		return getXML("BK",context);
	}
	
	public String toPrimaryKeyXML(IRadsContext context)  {
		return getXML("PK",context);
	}
	
	/**
	 * 
	 * @param mode - full ,  PK , BK
	 * @return
	 */
	private String getXML(String mode,IRadsContext context) {
		StringBuffer xmlString = new StringBuffer();
		String classTag= this.getClass().getSimpleName() ;
		try  {
		RadsPropertySet[] classAnnotationSet =(RadsPropertySet[] )this.getClass().getAnnotationsByType(RadsPropertySet.class);
		if(classAnnotationSet !=null  && classAnnotationSet.length >=1 ) {
			RadsPropertySet classAnnotation = classAnnotationSet[0];
			if (classAnnotation.excludeFromXML()) return "";
			if (!Utils.isNullString(classAnnotation.xmlTag())) classTag =classAnnotation.xmlTag(); 
		}
		xmlString.append("<" + classTag + ">\n" );
		Method allMethods[]= this.getClass().getMethods();
		for (int i = 0; i <allMethods.length ; i ++ ) {
			Method currMethod = allMethods[i];
			if(! (currMethod.getName().startsWith("get") || currMethod.getName().startsWith("is")) ){
				continue;
			}
			if(currMethod.getName().equals("getClass")) continue ;
			RadsPropertySet[] radsPropSet =(RadsPropertySet[] )currMethod.getAnnotationsByType(RadsPropertySet.class);
			RadsPropertySet radsProp = null;
			int index = currMethod.getName().startsWith("get")?3:2;
			String xmlTag= currMethod.getName().substring(index, currMethod.getName().length());
			if (radsPropSet != null &&  radsPropSet.length >=1) {
				radsProp= radsPropSet[0];
				if (!Utils.isNullString(mode) && "PK".equals(mode) && radsProp.isPK() == false ) {
					continue ;	
				}
				if (!Utils.isNullString(mode) && "BK".equals(mode) && radsProp.isBK() == false ) {
					  continue ;	
				}
				if(!radsProp.excludeFromXML()) {
					if (!Utils.isNullString(radsProp.xmlTag()))
						xmlTag = radsProp.xmlTag();
				}else 
					continue;
			}else if (!Utils.isNullString(mode) && ("PK".equals(mode) ||  "BK".equals(mode) )) {
				continue;
			}
			
				 Object ret = currMethod.invoke(this, new Object [] {});
				 if (ret == null) 
					 xmlString.append("<" + xmlTag + "/>\n");
				else if (ret instanceof ModelObject) {
					 if (radsProp!=null && radsProp.usePKForXML() ==true )
						 xmlString.append(((ModelObject)ret).toPrimaryKeyXML(context));
					 else if (radsProp!=null && radsProp.useBKForXML() ==true )
						 xmlString.append(((ModelObject)ret).toBusinessKeyXML(context));
					 else
						 xmlString.append(((ModelObject)ret).toXML(context));
				}else if (ret instanceof java.util.Collection) {
					 Collection subObjects = (java.util.Collection)ret ;
					 Iterator it = subObjects.iterator() ;
					 while (it.hasNext()) {
						 Object itObject = it.next() ;
						 if (itObject instanceof ModelObject) {
							 xmlString.append("<" + xmlTag + ">\n");
							 if (radsProp!=null && radsProp.usePKForXML() ==true )
								 xmlString.append(((ModelObject)itObject).toPrimaryKeyXML(context));
							 else if (radsProp!=null && radsProp.useBKForXML() ==true )
								 xmlString.append(((ModelObject)itObject).toBusinessKeyXML(context));
							 else
								 xmlString.append(((ModelObject)itObject).toXML(context));
							 xmlString.append("</" + xmlTag + ">\n");
						 } else {
							 xmlString.append("<" + xmlTag + ">" +  ret.toString()+ "</" + xmlTag + ">\n" );
						 }
					 }
				}else
					xmlString.append("<" + xmlTag + ">" +  ret.toString()+ "</" + xmlTag + ">\n" );	
			}
		}catch(Exception invEx) {
			invEx.printStackTrace();
		}
		xmlString.append("</" + classTag + ">\n" );
		return xmlString.toString();
	}
	
	
	
	public String toXML(IRadsContext context){
		return getXML("full",context);
	}
	
	
	
	private String getJSON(String mode,IRadsContext context) {
		StringBuffer jsonString = new StringBuffer();
		String classTag= this.getClass().getSimpleName() ;
		try  {
		RadsPropertySet[] classAnnotationSet =(RadsPropertySet[] )this.getClass().getAnnotationsByType(RadsPropertySet.class);
		if(classAnnotationSet !=null  && classAnnotationSet.length >=1 ) {
			RadsPropertySet classAnnotation = classAnnotationSet[0];
			if (classAnnotation.excludeFromXML()) return "";
			if (!Utils.isNullString(classAnnotation.xmlTag())) classTag =classAnnotation.jsonTag() ;
		}
		jsonString.append("{\n" );
		Method allMethods[]= this.getClass().getMethods();
		for (int i = 0; i <allMethods.length ; i ++ ) {
			Method currMethod = allMethods[i];
			if(! (currMethod.getName().startsWith("get") || currMethod.getName().startsWith("is")) ){
				continue;
			}
			if(currMethod.getName().equals("getClass")) continue ;
			RadsPropertySet[] radsPropSet =(RadsPropertySet[] )currMethod.getAnnotationsByType(RadsPropertySet.class);
			RadsPropertySet radsProp = null;
			int index = currMethod.getName().startsWith("get")?3:2;
			String jsonTag= currMethod.getName().substring(index, currMethod.getName().length());
			if (radsPropSet != null &&  radsPropSet.length >=1) {
				radsProp= radsPropSet[0];
				if (!Utils.isNullString(mode) && "PK".equals(mode) && radsProp.isPK() == false ) {
					continue ;	
				}
				if (!Utils.isNullString(mode) && "BK".equals(mode) && radsProp.isBK() == false ) {
					  continue ;	
				}
				if(!radsProp.excludeFromJSON()) {
					if (!Utils.isNullString(radsProp.jsonTag()))
						jsonTag = radsProp.jsonTag();
				}else 
					continue;
			}else if (!Utils.isNullString(mode) && ("PK".equals(mode) ||  "BK".equals(mode) )) {
				continue;
			}
			
				 Object ret = currMethod.invoke(this, new Object [] {});
				 if (ret == null  ) 
					 jsonString.append("\"" + jsonTag + "\":\"\",\n");
				else if (ret instanceof ModelObject) {
					jsonString.append("\"" + jsonTag + "\":\n");
					 if (radsProp!=null && radsProp.usePKForJSON() ==true )
						 jsonString.append(((ModelObject)ret).toPrimaryKeyJSON(context));
					 else if (radsProp!=null && radsProp.useBKForJSON() ==true )
						 jsonString.append(((ModelObject)ret).toBusinessKeyJSON(context));
					 else
						 jsonString.append(((ModelObject)ret).toJSON(context));
					 jsonString.append(",\n");
				}else if (ret instanceof java.util.Collection) {
					 Collection subObjects = (java.util.Collection)ret ;
					 jsonString.append("\"" + jsonTag + "\":[\n");
					 Iterator it = subObjects.iterator() ;
					 while (it.hasNext()) {
						 Object itObject = it.next() ;
						 if (itObject instanceof ModelObject) {
							 if (radsProp!=null && radsProp.usePKForJSON() ==true )
								 jsonString.append(((ModelObject)itObject).toPrimaryKeyJSON(context));
							 else if (radsProp!=null && radsProp.useBKForJSON() ==true )
								 jsonString.append(((ModelObject)itObject).toBusinessKeyJSON(context));
							 else
								 jsonString.append(((ModelObject)itObject).toJSON(context) );
							    
							 jsonString.append(",");
						 } else {
							 if (itObject != null ) { 
								 jsonString.append("{\n" +  itObject.toString()+ "}, " );
							 }
						 }
					 }
					 jsonString.setCharAt(jsonString.length()-1, ' ');
					 jsonString.append("],\n");
				}else if(java.util.Date.class.equals(currMethod.getReturnType())) {
					 String dateFormatted =  Utils.dateToString((java.util.Date)ret,context.getDateFormat());
					 jsonString.append("\"" + jsonTag + "\":\"" + dateFormatted+ "\",\n" );
				 }else  if (!isNumericClass(currMethod.getReturnType()))
					jsonString.append("\"" + jsonTag + "\":\"" +  ret.toString()+ "\",\n" );
				else
					jsonString.append("\"" + jsonTag + "\":" +  ret.toString()+ ",\n" );
			}
		jsonString.setCharAt(jsonString.lastIndexOf(","),' ');
		}catch(Exception invEx) {
			invEx.printStackTrace();
		}
		jsonString.append("\n}\n" );
		return jsonString.toString();
	}
	
	private boolean isNumericClass (Class cls) {
		if (cls.equals(Integer.class) || cls.equals(Double.class) || cls.equals(Float.class) || cls.equals(Long.class))
			return true;
		else 
			return false ;
			
	}
	public String toBusinessKeyJSON(IRadsContext context)  {
		return getJSON("BK",context);
	}
	
	public String toPrimaryKeyJSON(IRadsContext context)  {
		return getJSON("PK",context);
	}
	public String toJSON(IRadsContext context){
		return getJSON("full",context);
	}

	private Map<String,Method> getAllProperties() {
		Map<String,Method>  allFields = new HashMap();
		Method allMethods[]= this.getClass().getMethods();
			for (int i = 0; i <allMethods.length ; i ++ ) {
				Method currMethod = allMethods[i];
				if(! (currMethod.getName().startsWith("set")) && ! (currMethod.getName().startsWith("get")) &&
						!(currMethod.getName().startsWith("is")) ){
					continue;
				}
				String prop= currMethod.getName().substring(3, currMethod.getName().length());
				allFields.put(prop, currMethod);
			}
			return allFields;
	}
	
	private Map<String,Method> getAllSetterMethods(String jsonorXML) {
		Map<String,Method>  allFields = new HashMap();
		Method allMethods[]= this.getClass().getMethods();
			for (int i = 0; i <allMethods.length ; i ++ ) {
				Method currMethod = allMethods[i];
				if(! (currMethod.getName().startsWith("set"))){
					continue;
				}
				RadsPropertySet[] radsPropSet =(RadsPropertySet[] )currMethod.getAnnotationsByType(RadsPropertySet.class);
				RadsPropertySet radsProp = null;
				String prop= currMethod.getName().substring(3, currMethod.getName().length());
				if (radsPropSet != null &&  radsPropSet.length >=1) {
					radsProp= radsPropSet[0];
					if (jsonorXML.equalsIgnoreCase("JSON") && !Utils.isNullString(radsProp.jsonTag()) )
						prop = radsProp.jsonTag();
					if (jsonorXML.equalsIgnoreCase("XML") && !Utils.isNullString(radsProp.xmlTag()) )
						prop = radsProp.xmlTag();
				}
					allFields.put(prop, currMethod);
			}
		return allFields;
	}
	
	
	private Map<String,Method> getPKMethods() {
		Map<String,Method>  pkFields = new HashMap();
		Method allMethods[]= this.getClass().getMethods();
		for (int i = 0; i <allMethods.length ; i ++ ) {
			Method currMethod = allMethods[i];
			if(! (currMethod.getName().startsWith("get") || currMethod.getName().startsWith("is")) ){
				continue;
			}
			if(currMethod.getName().equals("getClass")) continue ;
			RadsPropertySet[] radsPropSet =(RadsPropertySet[] )currMethod.getAnnotationsByType(RadsPropertySet.class);
			RadsPropertySet radsProp = null;
			int index = currMethod.getName().startsWith("get")?3:2;
			String prop= currMethod.getName().substring(index, currMethod.getName().length());
			if (radsPropSet != null &&  radsPropSet.length >=1) {
				radsProp= radsPropSet[0];
				if (radsProp.isPK())
					pkFields.put(prop,currMethod);
			}
		}
		return pkFields;
	}
	private Map<String,Method>  getBKMethods() {
		Map<String,Method>  pkFields = new HashMap();
		Method allMethods[]= this.getClass().getMethods();
		for (int i = 0; i <allMethods.length ; i ++ ) {
			Method currMethod = allMethods[i];
			if(! (currMethod.getName().startsWith("get") || currMethod.getName().startsWith("is")) ){
				continue;
			}
			if(currMethod.getName().equals("getClass")) continue ;
			RadsPropertySet[] radsPropSet =(RadsPropertySet[] )currMethod.getAnnotationsByType(RadsPropertySet.class);
			RadsPropertySet radsProp = null;
			int index = currMethod.getName().startsWith("get")?3:2;
			String prop= currMethod.getName().substring(index, currMethod.getName().length());
			if (radsPropSet != null &&  radsPropSet.length >=1) {
				radsProp= radsPropSet[0];
				if (radsProp.isBK())
					pkFields.put(prop,currMethod);
			}
		}
		return pkFields;
	}
	
	
	private Map<String,Method>  getAllgetterMethods() {
		Map<String,Method>  pkFields = new HashMap();
		Method allMethods[]= this.getClass().getMethods();
		for (int i = 0; i <allMethods.length ; i ++ ) {
			Method currMethod = allMethods[i];
			if(! (currMethod.getName().startsWith("get") || currMethod.getName().startsWith("is")) ){
				continue;
			}
			if(currMethod.getName().equals("getClass")) continue ;
			if(currMethod.getName().equals("getPK") || currMethod.getName().equals("getBK")) continue ;
			
			int index = currMethod.getName().startsWith("get")?3:2;
			String prop= currMethod.getName().substring(index, currMethod.getName().length());
			pkFields.put(prop,currMethod);
			
		}
		return pkFields;
	}


	@RadsPropertySet(excludeFromJSON=true,excludeFromXML=true)
	public  Object getPK(){
		Map pkMethods =getPKMethods() ;
		try {
		if (pkMethods != null ) {
			Iterator it =	pkMethods.keySet().iterator() ;
			while ((it.hasNext())) {
				String key = (String)it.next() ;
				Method method = (Method)pkMethods.get(key);
				Object retVal = method.invoke(this, new Object [] {});
				if (pkMethods.size() == 1)
					return retVal;
				pkMethods.put(key, retVal);
			}
		}
		}catch(Exception ex) {
			ex.printStackTrace(); 
		}
		return pkMethods;
	}
	
	@RadsPropertySet(excludeFromJSON=true,excludeFromXML=true)
	public  Map<String,Object> getBK(){
		Map pkMethods =getBKMethods() ;
		try {
		if (pkMethods != null ) {
			Iterator it =	pkMethods.keySet().iterator() ;
			while ((it.hasNext())) {
				String key = (String)it.next() ;
				Method method = (Method)pkMethods.get(key);
				Object retVal = method.invoke(this, new Object [] {});
				pkMethods.put(key, retVal);
			}
		}
		}catch(Exception ex) {
			ex.printStackTrace(); 
		}
		return pkMethods;
	}
		
	private Map<String,Object> getMap(String mode, IRadsContext context){
		Map pkMethods =getAllgetterMethods() ;
		Map<String,Object> returnMap = new HashMap();
		try {
		if (pkMethods != null ) {
			Iterator it =	pkMethods.keySet().iterator() ;
			while ((it.hasNext())) {
				String key = (String)it.next() ;
				Method method = (Method)pkMethods.get(key);
				RadsPropertySet[] radsPropSet =(RadsPropertySet[] )method.getAnnotationsByType(RadsPropertySet.class);
				RadsPropertySet radsProp = null;
				if (radsPropSet != null &&  radsPropSet.length >=1) {
					radsProp= radsPropSet[0];
					if (!Utils.isNullString(mode) && "PK".equals(mode) && radsProp.isPK() == false ) {
						continue ;	
					}
					if (!Utils.isNullString(mode) && "BK".equals(mode) && radsProp.isBK() == false ) {
						  continue ;	
					}
					if(radsProp.excludeFromMap()) continue ;
				}else if (!Utils.isNullString(mode) && ("PK".equals(mode) ||  "BK".equals(mode) )) {
					continue;
				}
				Object retVal = method.invoke(this, new Object [] {}); 
				if (ModelObject.class.isAssignableFrom(method.getReturnType()) ) {
					if (retVal == null) {
						retVal= Class.forName(method.getReturnType().getName()).newInstance() ;
					}
					 if (radsProp!=null && radsProp.usePKForMap() ==true ) {
						 	Map innerMap = ((ModelObject)retVal).getMap("PK",context);
						 	returnMap.put(key,innerMap);
					 }else if (radsProp!=null && radsProp.useBKForMap() ==true ){
						 	Map innerMap = ((ModelObject)retVal).getMap("BK",context);
						 	returnMap.put(key,innerMap);
					 }else {
						 	Map innerMap = ((ModelObject)retVal).getMap("full",context);
						 	returnMap.put(key,innerMap);
					 }
				}else
					returnMap.put(key, retVal);
			}
		}
		}catch(Exception ex) {
			ex.printStackTrace(); 
		}
		return returnMap;
	}
	
	public  Map<String,Object> toMap(IRadsContext context){
		return getMap("full",context);
	}

}
