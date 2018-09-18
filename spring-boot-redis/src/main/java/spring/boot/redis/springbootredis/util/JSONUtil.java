package spring.boot.redis.springbootredis.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * JSON公共帮助类
 * @author bill.li
 * 
 */
public class JSONUtil {
	
	/**
	 * object转成byte
	 */
	public static byte[] getBytes(Object object) {
		return getString(object).getBytes();
	}

	/**
	 * byte转成object
	 */
	public static Object getObject(byte[] buf) {
		return JSON.parse(buf);
	}
	
	/**
	 * Object转成String
	 */
	public static String getString(Object object) {
		return JSON.toJSONString(object);
	}

	/**
	 * String转成Object
	 */
	public static <T> T strToObj(String str, Class<? extends T> clazz) {
		return JSON.parseObject(str, clazz);
	}
	
	public static <T> T byteToObj(byte[] bs, Class<? extends T> clazz) {
		return JSON.parseObject(bs, clazz);
	}
	
	/**
	 * String转成Object
	 */
	public static <T> T strToObj(String str, Type type) {
		return JSON.parseObject(str, type);
	}
	
	public static <T> T byteToObject(byte[] bs, Type type) {
		return JSON.parseObject(bs, type);
	}
	
	/**
	 * 将JSON列表转换为字符串
	 * @param jsonList
	 * @return
	 */
	public static String JSONListToString(List<JSONObject> jsonList) {
		if (jsonList == null) {
			return "[]";
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("[");
		for (JSONObject json : jsonList) {
			stringBuffer.append(json.toString());
			stringBuffer.append(",");
		}
		if (stringBuffer.length() != 1) {
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);
		}
		stringBuffer.append("]");
		return stringBuffer.toString();
	}

	/**
	 * 将字符串转化为JSON列表
	 * @param jsonString 格式：[{"id":1,num:3},{"id":2,num:4}]
	 * @return
	 */
	public static List<JSONObject> stringToJSONList(String jsonListString) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			if (jsonListString.length() > 2) {
				String jsonListStringTmp = jsonListString.substring(0,
						jsonListString.length() - 1);
				String[] jsonStrings = jsonListStringTmp.split("}");
				for (String jsonString : jsonStrings) {
					JSONObject jsonObject = JSONObject.parseObject(jsonString
							.substring(1) + "}");
					jsonList.add(jsonObject);
				}
			}
		} catch (JSONException e) {
			return new ArrayList<JSONObject>();
		}
		return jsonList;
	}

}
