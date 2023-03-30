package com.cdc.okex.Utils;
import com.alibaba.fastjson.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

public class WebSocketUtils {

    private static String apiKey = "8486bcdd-f2fb-4645-9245-a771513572d8";
    private static String secretKey = "CDCE90D1077AC0C4F5B48D64E9E5DE6A";
    private static String passphrase = "Cdc19980828%";

    private static String url = "wss://ws.okx.com:8443/ws/v5/public";
    //private static String url = "https://www.okx.com/";

    public static String getSession() {
        return "";

    }

    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(15);

        WebSocket ws = HttpClient
                .newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create(url), new WebSocketClient(latch))
                .join();
        String timeString = ConvertUtils.getTimeString();
        long timestamp = ConvertUtils.getTimestamp();
        String accessSign = ConvertUtils.getAccessSign(timestamp, "GET", "/users/self/verify", null, secretKey);
        JSONObject accessLoginJSON = ConvertUtils.getWebsocketLoginAccessJSON(apiKey, passphrase, timestamp, accessSign);
        JSONObject subscribeAccessJSON = ConvertUtils.getWebsocketSubscribeAccessJSON("tickers", "LTC-USDT");

        ws.sendText(accessLoginJSON.toString(), true);
        //latch.await();

        ws.sendText(subscribeAccessJSON.toString(), true);
        latch.await();
    }

    private static class WebSocketClient implements WebSocket.Listener {
        private final CountDownLatch latch;

        public WebSocketClient(CountDownLatch latch) { this.latch = latch; }

        @Override
        public void onOpen(WebSocket webSocket) {
            System.out.println("onOpen using subprotocol " + webSocket.getSubprotocol());
            WebSocket.Listener.super.onOpen(webSocket);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            System.out.println("onText received " + data);
            latch.countDown();
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            System.out.println("Bad day! " + webSocket.toString());
            WebSocket.Listener.super.onError(webSocket, error);
        }
    }

}
