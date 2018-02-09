package utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author yuh
 * @Date Created in 上午11:12 2018/2/9
 * @Description
 */
public class ParseRequest {
    /**
     * @param request
     * @param paramName
     * @param defaultStr
     * @return String
     * @Author: lulei
     * @Description:  获取参数值，返回字符串，去除前后多余的空格
     */
    public static String getString(HttpServletRequest request, String paramName, String defaultStr){
        String param = request.getParameter(paramName);
        if (param == null){
            return defaultStr;
        }
        try {
            return new String(param.getBytes("iso-8859-1"), "utf-8").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return defaultStr;
        }
    }
    /**
     * @param request
     * @param paramName
     * @param defaultStr
     * @return
     * @Author: lulei
     * @Description:   获取参数值，返回字符串，参数采用gbk编码，去除前后多余的空格
     */
    public static String getStringGbk(HttpServletRequest request, String paramName, String defaultStr){
        String param = request.getParameter(paramName);
        if (param == null){
            return defaultStr;
        }
        try {
            return new String(param.getBytes("iso-8859-1"), "gbk").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return defaultStr;
        }
    }

    /**
     * @param request
     * @param paramName
     * @param defaultInt
     * @return int
     * @Author: lulei
     * @Description:  获取参数值，返回int形整数
     */
    public static int getInt(HttpServletRequest request, String paramName, int defaultInt){
        String param = request.getParameter(paramName);
        if (param == null){
            return defaultInt;
        }
        try {
            int re = Integer.parseInt(param);
            return re;
        } catch (Exception e) {
            return defaultInt;
        }
    }

    /**
     * @param request
     * @param paramName
     * @param defaultlong
     * @return long
     * @Author: lulei
     * @Description:  获取参数值，返回long形数字
     */
    public static long getLong(HttpServletRequest request, String paramName, long defaultlong){
        String param = request.getParameter(paramName);
        if (param == null){
            return defaultlong;
        }
        try {
            long re = Long.parseLong(param);
            return re;
        } catch (Exception e) {
            return defaultlong;
        }
    }
}
