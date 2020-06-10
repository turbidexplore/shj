package com.turbid.explore.tools;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

public class CodeLib {

    public static String md5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static String[] headimg = new String[]{"https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/%E9%BB%98%E8%AE%A4%E5%A4%B4%E5%83%8F.png"};

    private static String[] code=new String[]{
            "0","1","2","3","4","5","6","7","8","9",
            "A","B","C","D","E","F","G","H","J","K","M",
            "N","P","Q","R","S","T","U","V","W","X","Y"
    };

    //获取随机头像
    public static String getHeadimg() {
        Random random = new Random();
        int number = random.nextInt(headimg.length);
        return headimg[number];
    }

    //获取随机编码
    public static String randomCode(int length,int type) {
        String base = "";
        if(type==0){
            base="abcdefghijklmnopqrstuvwxyz";
        }else if(type==1){
            base="123456789";
        }
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    private static final String slat = "&%5123***&&%%$$#@";
    public static String encrypt(String dataStr) {
        try {
            dataStr = dataStr + slat;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes("UTF8"));
            byte s[] = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    public static boolean createFile(File fileName)throws Exception{
        boolean flag=false;
        try{
            if(!fileName.exists()){
                fileName.createNewFile();
                flag=true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }


    public static boolean writeTxtFile(String content,File  fileName)throws Exception{
        RandomAccessFile mm=null;
        boolean flag=false;
        FileOutputStream o=null;
        try {
            o = new FileOutputStream(fileName);
            o.write(content.getBytes("GBK"));
            o.close();
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(mm!=null){
                mm.close();
            }
        }
        return flag;
    }

    public static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }


    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    public static String getLocalIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String forwarded = request.getHeader("X-Forwarded-For");
        String realIp = request.getHeader("X-Real-IP");
        String ip = null;
        if (realIp == null) {
            if (forwarded == null) {
                ip = remoteAddr;
            } else {
                ip = remoteAddr + "/" + forwarded.split(",")[0];
            }
        } else {
            if (realIp.equals(forwarded)) {
                ip = realIp;
            } else {
                if(forwarded != null){
                    forwarded = forwarded.split(",")[0];
                }
                ip = realIp + "/" + forwarded;
            }
        }
        return ip;
    }

    public static String getAddressByIp(String ip) {
        if (ip == null || ip.equals("")) {
            return null;
        }
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        String thisUrl ="http://api.map.baidu.com/location/ip?ip="+ip.trim()+"&ak=fVl33R4iStCXR9xkS9jGc0GiAp5Em0TB";
        try {
            URL url = new URL(thisUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
            return  JSONObject.parseObject(result).getJSONObject("content").getJSONObject("address_detail").getString("city");

        } catch (Exception e) {
            return "未知地区";
        }

    }



    public static String getNikeName(StringRedisTemplate stringRedisTemplate){

        String[] strNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().split("-");
        Integer year = Integer.parseInt(strNow[0].substring(2,4));
        Integer month = Integer.parseInt(strNow[1]);
        Integer day = Integer.parseInt(strNow[2]);
        String key="c"+year+"m"+month+"d"+day;

        if(null==stringRedisTemplate.opsForValue().get(key)) {
            stringRedisTemplate.opsForValue().set(key, "1");
            return "U"+code[year]+code[month]+code[day]+"0001";
        }
        Integer value=Integer.parseInt(stringRedisTemplate.opsForValue().get(key))+1;
        String count= CodeLib.getCode("",value);
        System.out.println(count.length());
        if(count.length()<4){
            for (int i=0;i<=(5-count.length());i++){
                count="0"+count;
            }
        }
        stringRedisTemplate.opsForValue().set(key,value.toString());
        return "U"+code[year]+code[month]+code[day]+count;
    }

    public static String getCode(String str,Integer value){
        Integer number= value/code.length;
        if(number>0){
            str= str+code[value-(number*code.length)];
            return getCode(str,number);
        }else {
            return code[value]+str;
        }


    }

    public static String getSHC() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = sdf.format(date);
        return str;
    }


    /**
     * 从html代码中，获得指定标签的指定属性的取值
     * @param html  HTML代码
     * @param tagName  指定的标签名称
     * @param propertyName 指定的属性名称
     * @return
     */
    public static final List<String> listTagPropertyValue(final String html, final String tagName, final String propertyName) {
        // 结果集合
        ArrayList<String> result = new ArrayList<String>();
        // 找出HTML代码中所有的tagName标签
        List<String> list = RegexUtils.getMatchList(html, "<" + tagName + "[^>]*>", true);
        // 循环遍历每个标签字符串，找出其中的属性字符串,比如 src=....
        for (String tagStr : list) {
            // 去掉标签结尾的/>，方便后面 src 属性的正则表达式。
            // 这样可以适应  <video src=http://www.yourhost.com/xxx>  这样的标签
            if (tagStr.endsWith("/>")) {
                tagStr = tagStr.substring(0, tagStr.length() - 2);
                tagStr = tagStr + " ";
            }
            // 去掉标签结尾的>，方便后面匹配属性的正则表达式。
            // 这样可以适应  <video src=http://www.yourhost.com/xxx>  这样的标签
            else if (tagStr.endsWith(">")) {
                tagStr = tagStr.substring(0, tagStr.length() - 1);
                tagStr = tagStr + " ";
            }
            // 去掉字符串开头的 <video 或 <source
            tagStr = tagStr.substring(1 + tagName.length());
            tagStr = " " + tagStr;

            // 取出属性的值
            String regSingleQuote = "^" + propertyName + "='[^']*'"; // 使用单引号
            String regDoubleQuote = "^" + propertyName + "=\"[^\"]*\""; // 使用双引号
            String reg = "^" + propertyName + "=[^\\s]*\\s"; // 不使用引号
            int index = 0;
            int length = tagStr.length();
            while (index <= length) {
                String subStr = tagStr.substring(index);
                String str = RegexUtils.getFirstMatch(subStr, regSingleQuote, true);
                if (null != str) {
                    // 往后跳过已经匹配的字符串。
                    index += str.length();
                    String srcStr = str;
                    srcStr = srcStr.trim();
                    // 去掉 src=
                    srcStr = srcStr.substring(propertyName.length() + 1);
                    // 去掉单引号
                    srcStr = srcStr.substring(1);
                    srcStr = srcStr.substring(0, srcStr.length() - 1);
                    // 结果中加入图片URL
                    result.add(srcStr);
                } else if ((str = RegexUtils.getFirstMatch(subStr, regDoubleQuote, true)) != null) {
                    // 往后跳过已经匹配的字符串。
                    index += str.length();
                    String srcStr = str;
                    srcStr = srcStr.trim();
                    // 去掉 src=
                    srcStr = srcStr.substring(propertyName.length() + 1);
                    // 去掉双引号
                    srcStr = srcStr.substring(1);
                    srcStr = srcStr.substring(0, srcStr.length() - 1);
                    // 结果中加入图片URL
                    result.add(srcStr);
                } else if ((str = RegexUtils.getFirstMatch(subStr, reg, true)) != null) {
                    // 往后跳过已经匹配的字符串。
                    index += str.length();
                    String srcStr = str;
                    srcStr = srcStr.trim();
                    // 去掉 src=
                    srcStr = srcStr.substring(propertyName.length() + 1);
                    // 结果中加入图片URL
                    result.add(srcStr);
                } else if ((str = RegexUtils.getFirstMatch(subStr, "^[\\w]+='[^']*'", true)) != null) {
                    // 往后跳过已经匹配的字符串。
                    index += str.length();
                } else {
                    index++;
                }
            }
        } // end for (String tagStr : list)
        result.trimToSize();
        return result;
    }

    /**
     * 从html代码中找出img标签的图片路径
     * @param html  HTML代码
     * @return  字符串列表，里面每个字符串都是图片链接地址
     */
    public static List<String> listImgSrc(final String html) {
        return listTagPropertyValue(html, "img", "src");
    }


    public static String getStandardDate(long t) {

        StringBuffer sb = new StringBuffer();


        long time = System.currentTimeMillis() - (t*1000);
        long mill = (long) Math.ceil(time /1000);//秒前

        long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前

        long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时

        long day = (long) Math.ceil(time/24/60/60/1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }


    public static String getFriendlyTime(Date date,boolean isAf) {

        if (date ==null) {

            return "";

        }

        SimpleDateFormat dateFormat;

        int ct = (int) ((System.currentTimeMillis() - date.getTime()) /1000);

        if (isAf) {

            if (ct <0) {

                if (ct < -86400) {//86400 * 30

                    int day = ct / (-86400);

                    if (day ==1) {

                        return "后天 " +new SimpleDateFormat("HH:mm").format(date);

                    }

                    if (date.getYear() ==new Date().getYear()) {

                        dateFormat =new SimpleDateFormat("MM-dd HH:mm");

                    }else {

                        dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");

                    }

                    return dateFormat.format(date);

                }

                if (!isSameDate(new Date(), date)) {//判断跨天

                    return "明天 " +new SimpleDateFormat("HH:mm").format(date);

                }

                if (-86400 < ct && ct < -3600) {

                    return String.format("%d小时后  %s", ct / -3600,new SimpleDateFormat("HH:mm").format(date));

                }

                if (-3600 < ct && ct < -61) {

                    return String.format("%d分钟后  %s", Math.max(ct / -60,3),new SimpleDateFormat("HH:mm").format(date));

                }

                if (-61 < ct && ct <0) {

                    return String.format("即将到点  %s",new SimpleDateFormat("HH:mm").format(date));

                }

            }

        }

        if (ct <61) {//1分钟内

            return "刚刚";

        }

        dateFormat =new SimpleDateFormat("HH:mm");

        if (isSameDate(new Date(), date)) {//

            if (ct <3600) {//1小时内

                return String.format("%d分钟前", Math.max(ct /60,3));

            }

            if (ct >=3600 && ct <86400) {//当天超过一小时显示

                return dateFormat.format(date);

            }

        }else {

//不是当天时进入

            if (ct <86400) {

                return "昨天 " + dateFormat.format(date);

            }

            if (ct >=86400 && ct <259200) {//86400 * 3 (三天)

                int day = ct /86400;

                if (!isSameDate(addDay(new Date(), -day), date)) {//判断时间匹配日期

                    day = day +1;

                }

                if (day ==1) {

                    return "昨天 " + dateFormat.format(date);

                }else {

                    dateFormat =new SimpleDateFormat("MM-dd HH:mm");

                    return dateFormat.format(date);

                }

            }

        }

        if (date.getYear() ==new Date().getYear()) {

            dateFormat =new SimpleDateFormat("MM-dd HH:mm");

        }else {

            dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");

        }

        return dateFormat.format(date);

    }

/**

 * 是否两个时间是否为同一天

 *

     * @param date1,date2 两个对比时间

 */

    private static boolean isSameDate(Date date1, Date date2) {

        Calendar cal1 = Calendar.getInstance();

        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();

        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2

                .get(Calendar.YEAR);

        boolean isSameMonth = isSameYear

                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);

        boolean isSameDate = isSameMonth

                && cal1.get(Calendar.DAY_OF_MONTH) == cal2

                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;

    }

/**

 * 天数增加

 *

     * @param date 时间

     * @param day  增加天数 可为负整数、正整数

 *            例如-1 相当于减少一天

 *            1 相当于增加一天

 */

    public static Date addDay(Date date,int day) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + day);

        return calendar.getTime();

    }

/**

 * 月数增加

 *

     * @param date  时间

     * @param month 增加月数 可为负整数、正整数

 *              例如-1 相当于减少一月

 *              1 相当于增加一月

 */

    public static Date addMonth(Date date,int month) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.MONTH, month);

        return calendar.getTime();

    }

/**

 * 年数增加

 *

     * @param date 时间

     * @param year 增加年数 可为负整数、正整数

 *            例如-1 相当于减少一年

 *            1 相当于增加一年

 */

    public static Date addYear(Date date,int year) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.YEAR, year);

        return calendar.getTime();

    }

    public static List removeDuplicate(List list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;

    }





}
