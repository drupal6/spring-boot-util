package spring.boot.redis.springbootredis.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ParamUtil {

    public static Map<String, Object> megerNameAndArgs(String[] names, Object[] args) throws Throwable {
        if(names == null || args == null ) {
            return null;
        }
        if(names.length != args.length) {
            throw new Throwable("names not match args");
        }
        Map<String, Object> paramM = new HashMap<>();
        for(int i = 0; i < names.length; i++) {
            paramM.put(names[i], args[i]);
        }
        return paramM;
    }


    public static String getParam(Map<String, Object> param, String name) throws Throwable{
        if(param == null || param.isEmpty()) {
            return null;
        }
        if(name == null || name.isEmpty()){
            return null;
        }
        if(false == name.startsWith("{#") || false == name.endsWith("}")) {
            throw new Throwable("param name not suport. expect {#xxx.xxx} but is " + name);
        }
        String[] names = name.substring(2, name.length() - 1).split("\\.");
        Object obj = null;
        for(int i = 0; i < names.length; i ++) {
            if(i == 0) {
                obj = param.get(names[i]);
            }
            if(obj == null) {
                return null;
            }
            if(i == names.length - 1) {
                return String.valueOf(obj);
            }
            obj = getProperty(obj, names[i + 1]);
        }
        return null;
    }

    public static Object getProperty(Object obj, String fieldName) throws Exception {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        Method method = obj.getClass().getMethod(getter, new Class[] {});
        Object value = method.invoke(obj, new Object[] {});
        return value;
    }
}
