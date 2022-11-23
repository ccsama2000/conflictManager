package com.example.filemanager.utils;

import java.util.HashMap;
import java.util.Map;

public class LayuiUtil {

    /**
     * 设置返回的结果集
     *
     * @param code  0：处理成功 1：处理失败
     * @param data
     * @param count
     * @return
     */
    public static Map<String, Object> setResultMap(Integer code, String msg, Object data, Integer count) {

        if (!checkParam(String.valueOf(code), String.valueOf(count))) {
            code = 0;
            count = 0;
        }

        Integer finalCode = code;
        Integer finalCount = count;
        return new HashMap<String, Object>() {{
            put("code", finalCode);
            put("msg", msg);
            put("data", data);
            put("count", finalCount);
        }};
    }

    /**
     * 检查参数  不能为空或者null
     *
     * @param args
     * @return
     */
    public static boolean checkParam(String... args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i] == null || args[i].equals("")) {
                return false;
            }
        }

        return true;
    }

}
