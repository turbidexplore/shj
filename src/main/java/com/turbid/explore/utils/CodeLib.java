package com.turbid.explore.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CodeLib {

    private static String[] code=new String[]{
            "0","1","2","3","4","5","6","7","8","9",
            "A","B","C","D","E","F","G","H","J","K","M",
            "N","P","Q","R","S","T","U","V","W","X","Y"
    };


    //获取随机编码
    public static String randomCode(int length,int type) {
        String base = "";
        if(type==0){
            base="abcdefghijklmnopqrstuvwxyz";
        }else if(type==1){
            base="0123456789";
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
        String thisUrl ="http://ip.taobao.com/service/getIpInfo.php?ip="+ip;
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
            return result;

        } catch (Exception e) {
            System.out.println("获取IP地址失败");
        }
        return null;
    }


    public String getNikeName(StringRedisTemplate stringRedisTemplate){

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
        String count= getCode("",value);
        System.out.println(count.length());
        if(count.length()<4){
            for (int i=0;i<=(5-count.length());i++){
                count="0"+count;
            }
        }
        stringRedisTemplate.opsForValue().set(key,value.toString());
        return "U"+code[year]+code[month]+code[day]+count;
    }

    public String getCode(String str,Integer value){
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

}
