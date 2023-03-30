package com.cdc.okex;

import com.alibaba.fastjson.JSONObject;
import com.cdc.okex.Utils.ConvertUtils;
import com.cdc.okex.Utils.WebSocketUtils;
import org.junit.Test;

public class RequestTest {
    @Test
    public void TimestampTest() {
        long timestamp = ConvertUtils.getTimestamp();
        String timeString = ConvertUtils.getTimeString();
        System.out.println(timestamp);
        System.out.println(timeString);
    }

    @Test
    public void AccessSignTest() {
        // 正确为：+LdIr8lkkvhr5hoA3g9TMC0+uQJ849ftAcocA/ouu4M=
        //String result = ConvertUtils.getAccessSign("1538054050", "GET", "/users/self/verify",null,"22582BD0CFF14C41EDBF1AB98506286D");
        //System.out.println(result);
    }

    @Test
    public void JSONTest() {
        JSONObject jsonObject = ConvertUtils.getWebsocketLoginAccessJSON("8486bcdd-f2fb-4645-9245-a771513572d8", "Cdc19980828%",1538054050,"XAhon9iP3NZ1GA51USnuP44v/y4W2kDMleNUn3J50aY=");
        System.out.println(jsonObject.toJSONString());
        //JSONObject subscribeAccessJSON = ConvertUtils.getSubscribeAccessJSON("LTC-USD-200327");
        //System.out.println(subscribeAccessJSON);
    }

    @Test
    public void ConnectionTest() {
        String session = WebSocketUtils.getSession();
        System.out.println(session);
    }

}
