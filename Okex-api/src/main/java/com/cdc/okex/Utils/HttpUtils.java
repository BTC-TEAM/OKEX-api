package com.cdc.okex.Utils;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtils {

    private static String apiKey = "8486bcdd-f2fb-4645-9245-a771513572d8";
    private static String secretKey = "CDCE90D1077AC0C4F5B48D64E9E5DE6A";
    private static String passphrase = "Cdc19980828%";

    private static String baseUrl = "https://www.okx.com/";

    /*
        OK-ACCESS-KEY The API Key as a String.
        OK-ACCESS-SIGN The Base64-encoded signature (see Signing Messages subsection for details).
        OK-ACCESS-TIMESTAMP The UTC timestamp of your request .e.g : 2020-12-08T09:08:57.715Z
        OK-ACCESS-PASSPHRASE The passphrase you specified when creating the APIKey.

        Request bodies should have content type application/json and be in valid JSON format.
         */
    public static String _requestData(String uri) throws IOException, InterruptedException {
        String timeString = ConvertUtils.getTimeString();
        String accessSign = null;//ConvertUtils.getAccessSign(timeString, "GET", uri, null, secretKey);
        JSONObject header = ConvertUtils.getHttpLoginAccessJSON(apiKey, passphrase, timeString, accessSign);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        System.out.println(request.headers());
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    public static void main(String[] args) {
        //GET /api/v5/market/tickers?instType=SWAP
        try {
            System.out.println(_requestData(baseUrl + "/api/v5/market/tickers?instType=SWAP"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
