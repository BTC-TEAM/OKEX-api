package com.cdc.okex.Utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ConvertUtils {

    public static long getTimestamp() {
        // 时间转换为时间戳
        // getTime返回自1970年01月01日00时00分00秒(北京时间1970年01月01日08时00分00秒)起至现在的总毫秒数.
        // 时间戳是指自1970年01月01日00时00分00秒(北京时间1970年01月01日08时00分00秒)起至现在的总秒数
        // 单位换算：1秒=1000毫秒
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date time = new Date(); // 获取当前时间
        String t = sdf.format(time);
        long timestamp = 0;
        try {
            timestamp = sdf.parse(t).getTime()/1000L;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    public static String getTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date time = new Date(); // 获取当前时间
        String format = sdf.format(time);// 格式化时间
        //System.out.println(format);
        return format;
    }

    public static String getAccessSign(long timestamp, String method, String url, String body, String secretKey) {
        /*
        OK-ACCESS-SIGN的请求头是对timestamp + method + requestPath + body字符串（+表示字符串连接），以及SecretKey，使用HMAC SHA256方法加密，通过Base-64编码输出而得到的。
        如：sign=CryptoJS.enc.Base64.stringify(CryptoJS.HmacSHA256(timestamp + 'GET' + '/api/v5/account/balance?ccy=BTC', SecretKey))
        其中，timestamp的值与OK-ACCESS-TIMESTAMP请求头相同，为ISO格式，如2020-12-08T09:08:57.715Z。
        method是请求方法，字母全部大写：GET/POST。
        requestPath是请求接口路径。如：/api/v5/account/balance
        body是指请求主体的字符串，如果请求没有主体（通常为GET请求）则body可省略。如：{"instId":"BTC-USDT","lever":"5","mgnMode":"isolated"}
         */
        StringBuilder input = new StringBuilder();
        input.append(timestamp);
        input.append(method);
        input.append(url);
        //input.append(body);   //太坑了StringBuilder直接把null变成字符串了
        String data = input.toString();
        //System.out.println(data);

        String result =null;
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes("utf-8"), "HmacSHA256");
            sha256_HMAC.init(secret);
            byte[] hash = sha256_HMAC.doFinal(data.getBytes("utf-8"));
            String encodeStr =  Base64.encodeBase64String(hash);
            String encodeStr16 = byte2Hex(hash);
            result = encodeStr;
            //result = Base64Utils.encodeToString(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    public static JSONObject getWebsocketLoginAccessJSON(String apiKey, String passphrase, long timestamp, String sign) {
        JSONObject js = new JSONObject();
        JSONArray args = new JSONArray();
        JSONObject arg = new JSONObject();
        js.put("op", "login");
        arg.put("apiKey", apiKey);
        arg.put("passphrase", passphrase);
        arg.put("timestamp", timestamp);
        arg.put("sign", sign);
        args.add(arg);

        js.put("args", args);

        return js;
    }

    public static JSONObject getHttpLoginAccessJSON(String apiKey, String passphrase, String timestamp, String sign) {
        /*
        OK-ACCESS-KEY The API Key as a String.
        OK-ACCESS-SIGN The Base64-encoded signature (see Signing Messages subsection for details).
        OK-ACCESS-TIMESTAMP The UTC timestamp of your request .e.g : 2020-12-08T09:08:57.715Z
        OK-ACCESS-PASSPHRASE The passphrase you specified when creating the APIKey.

        Request bodies should have content type application/json and be in valid JSON format.
         */

        JSONObject arg = new JSONObject();
        arg.put("OK-ACCESS-KEY", apiKey);
        arg.put("OK-ACCESS-SIGN", passphrase);
        arg.put("OK-ACCESS-TIMESTAMP", timestamp);
        arg.put("OK-ACCESS-PASSPHRASE", sign);
        arg.put("Content-Type", "application/json");

        return arg;
    }

    public static JSONObject getWebsocketSubscribeAccessJSON(String channel, String instId) {
        JSONObject js = new JSONObject();
        JSONArray args = new JSONArray();
        JSONObject arg = new JSONObject();
        js.put("op", "subscribe");
        arg.put("channel", channel);
        arg.put("instId", instId);
        args.add(arg);

        js.put("args", args);

        return js;
    }
}
