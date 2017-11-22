package com.eastpharm.ylc;



import java.util.List;

import org.dom4j.*;

import com.alibaba.fastjson.*;


public class ResultDataModel {
//	int code;
//	String message;
//	JSONArray array;
//	
//	public void setCode(int msgcode) {
//		code = msgcode;
//	}
//	public void setMessage(String msg) {
//		message = msg;
//	}
//	public void setArray(JSONArray jsonArray) {
//		array = jsonArray;
//	}
	
	public JSONObject setData( int intcode,String msg, JSONArray jsonArray) {
		JSONObject resObj = new JSONObject();
		resObj.put("code", intcode);
		resObj.put("message", msg);
		resObj.put("data", jsonArray);
		return resObj;
	}

	public JSONObject setErrorData(int intcode, Object printStackTrace, JSONArray array) {
		// TODO Auto-generated method stub
		JSONObject resObj = new JSONObject();
		resObj.put("code", intcode);
		resObj.put("message", printStackTrace);
		resObj.put("data", array);
		return resObj;
	}
	
	public JSONObject getParameterErrorMsg() {
		// TODO Auto-generated method stub
		JSONObject resObj = new JSONObject();
		resObj.put("code", 11000);
		resObj.put("message", "参数不正确");
		resObj.put("data", new JSONArray());
		return resObj;
	}

	 /**
     * xml转json
     * @param xmlStr
     * @return
     * @throws DocumentException
     */
	public static JSONObject xml2Json(String xmlStr) throws DocumentException {
		Document doc= DocumentHelper.parseText(xmlStr);
        JSONObject json=new JSONObject();
        dom4j2Json(doc.getRootElement(), json);
        return json;
	}
	
	 /**
     * xml转json
     * @param element
     * @param json
     */
    public static void dom4j2Json(Element element,JSONObject json){
        //如果是属性
        for(Object o:element.attributes()){
            Attribute attr=(Attribute)o;
            if(!isEmpty(attr.getValue())){
                json.put("@"+attr.getName(), attr.getValue());
            }
        }
        List<Element> chdEl=element.elements();
        if(chdEl.isEmpty()&&!isEmpty(element.getText())){//如果没有子元素,只有一个值
            json.put(element.getName(), element.getText());
        }

        for(Element e:chdEl){//有子元素
            if(!e.elements().isEmpty()){//子元素也有子元素
                JSONObject chdjson=new JSONObject();
                dom4j2Json(e,chdjson);
                Object o=json.get(e.getName());
                if(o!=null){
                    JSONArray jsona=null;
                    if(o instanceof JSONObject){//如果此元素已存在,则转为jsonArray
                        JSONObject jsono=(JSONObject)o;
                        json.remove(e.getName());
                        jsona=new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    if(o instanceof JSONArray){
                        jsona=(JSONArray)o;
                        jsona.add(chdjson);
                    }
                    json.put(e.getName(), jsona);
                }else{
                    if(!chdjson.isEmpty()){
                        json.put(e.getName(), chdjson);
                    }
                }


            }else{//子元素没有子元素
                for(Object o:element.attributes()){
                    Attribute attr=(Attribute)o;
                    if(!isEmpty(attr.getValue())){
                        json.put("@"+attr.getName(), attr.getValue());
                    }
                }
                if(!e.getText().isEmpty()){
                    json.put(e.getName(), e.getText());
                }
            }
        }
    }
    
    public static boolean isEmpty(String str) {

        if (str == null || str.trim().isEmpty() || "null".equals(str)) {
            return true;
        }
        return false;
    }
    
    public static boolean stringIsNullOrEmpty(String value) {
        if (value == null ) { //正确的写法
             return false;
         } else if ("".equals(value)) { //正确的写法
            return false;
         } else {
             return true;
         }
    }
    
    
    
    
}
