//package com.sipc.hospitalalarmsystem.sparkdesk;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.google.gson.Gson;
//import okhttp3.*;
//import okhttp3.sse.EventSource;
//import okhttp3.sse.EventSourceListener;
//import okhttp3.sse.EventSources;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.io.IOException;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.function.Consumer;
//
//@Slf4j
//@RequiredArgsConstructor
//public class SparkBigModel extends EventSourceListener {
//    public static final String hostUrl = "https://spark-api.xf-yun.com/v3.5/chat";
//    public static final String appid = "c74de684";
//    public static final String apiSecret = "MDM3ZjRiNWFmZGQ0MjZmNWJiMDk5NDk5";
//    public static final String apiKey = "72e6f918b75ea451e572f3a0abb2d966";
//
//    public static List<RoleContent> historyList = new ArrayList<>();
//    public static String totalAnswer = "";
//    public static String NewQuestion = "";
//    public static final Gson gson = new Gson();
//    private String userId;
//    private Boolean wsCloseFlag;
//
//    private static Boolean totalFlag = true;
//
//    public BigModelNew(String userId, Boolean wsCloseFlag) {
//        this.userId = userId;
//        this.wsCloseFlag = wsCloseFlag;
//    }
//
//    public static void main(String[] args) throws Exception {
//        while (true) {
//            if (totalFlag) {
//                Scanner scanner = new Scanner(System.in);
//                System.out.print("我：");
//                totalFlag = false;
//                NewQuestion = scanner.nextLine();
//                String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
//                OkHttpClient client = new OkHttpClient.Builder().build();
//                Request request = new Request.Builder().url(authUrl).build();
//                EventSource.Factory factory = EventSources.createFactory(client);
//                EventSource eventSource = factory.newEventSource(request, new BigModelNew("", false));
//
//                // 等待完成
//                while (!totalFlag) {
//                    Thread.sleep(200);
//                }
//                eventSource.cancel();
//            } else {
//                Thread.sleep(200);
//            }
//        }
//    }
//
//    public static boolean canAddHistory() {
//        int history_length = 0;
//        for (RoleContent temp : historyList) {
//            history_length = history_length + temp.content.length();
//        }
//        if (history_length > 12000) {
//            historyList.remove(0);
//            historyList.remove(1);
//            historyList.remove(2);
//            historyList.remove(3);
//            historyList.remove(4);
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    @Override
//    public void onOpen(EventSource eventSource, Response response) {
//        log.info("EventSource opened: {}", response);
//        sendRequest();
//    }
//
//    @SneakyThrows
//    private void sendRequest() {
//        JSONObject requestJson = new JSONObject();
//        JSONObject header = new JSONObject();
//        header.put("app_id", appid);
//        header.put("uid", UUID.randomUUID().toString().substring(0, 10));
//
//        JSONObject parameter = new JSONObject();
//        JSONObject chat = new JSONObject();
//        chat.put("domain", "generalv3");
//        chat.put("temperature", 0.5);
//        chat.put("max_tokens", 4096);
//        parameter.put("chat", chat);
//
//        JSONObject payload = new JSONObject();
//        JSONObject message = new JSONObject();
//        JSONArray text = new JSONArray();
//
//        if (historyList.size() > 0) {
//            for (RoleContent tempRoleContent : historyList) {
//                text.add(JSON.toJSON(tempRoleContent));
//            }
//        }
//
//        RoleContent roleContent = new RoleContent();
//        roleContent.role = "user";
//        roleContent.content = NewQuestion;
//        text.add(JSON.toJSON(roleContent));
//        historyList.add(roleContent);
//
//        message.put("text", text);
//        payload.put("message", message);
//
//        requestJson.put("header", header);
//        requestJson.put("parameter", parameter);
//        requestJson.put("payload", payload);
//
//        OkHttpClient client = new OkHttpClient.Builder().build();
//        RequestBody body = RequestBody.create(requestJson.toString(), MediaType.get("application/json; charset=utf-8"));
//        Request request = new Request.Builder().url(hostUrl).post(body).build();
//        client.newCall(request).execute();
//
//        while (true) {
//            Thread.sleep(200);
//            if (wsCloseFlag) {
//                break;
//            }
//        }
//    }
//
//    @Override
//    public void onEvent(EventSource eventSource, String id, String type, String data) {
//        log.info("Event received: {}", data);
//        JsonParse myJsonParse = gson.fromJson(data, JsonParse.class);
//        if (myJsonParse.header.code != 0) {
//            log.error("Error code: {}, SID: {}", myJsonParse.header.code, myJsonParse.header.sid);
//            eventSource.cancel();
//            return;
//        }
//        List<Text> textList = myJsonParse.payload.choices.text;
//        for (Text temp : textList) {
//            log.info(temp.content);
//            totalAnswer = totalAnswer + temp.content;
//        }
//        if (myJsonParse.header.status == 2) {
//            if (canAddHistory()) {
//                RoleContent roleContent = new RoleContent();
//                roleContent.setRole("assistant");
//                roleContent.setContent(totalAnswer);
//                historyList.add(roleContent);
//            } else {
//                historyList.remove(0);
//                RoleContent roleContent = new RoleContent();
//                roleContent.setRole("assistant");
//                roleContent.setContent(totalAnswer);
//                historyList.add(roleContent);
//            }
//            wsCloseFlag = true;
//            totalFlag = true;
//        }
//    }
//
//    @Override
//    public void onFailure(EventSource eventSource, Throwable t, Response response) {
//        log.error("SSE connection error", t);
//        if (response != null) {
//            try {
//                log.error("Response code: {}", response.code());
//                log.error("Response body: {}", response.body().string());
//            } catch (IOException e) {
//                log.error("Error reading response body", e);
//            }
//        }
//        eventSource.cancel();
//    }
//
//    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
//        URL url = new URL(hostUrl);
//        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
//        format.setTimeZone(TimeZone.getTimeZone("GMT"));
//        String date = format.format(new Date());
//        String preStr = "host: " + url.getHost() + "\n" +
//                "date: " + date + "\n" +
//                "GET " + url.getPath() + " HTTP/1.1";
//        Mac mac = Mac.getInstance("hmacsha256");
//        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
//        mac.init(spec);
//        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
//        String sha = Base64.getEncoder().encodeToString(hexDigits);
//        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
//        HttpUrl httpUrl = HttpUrl.parse("https://" + url.getHost() + url.getPath()).newBuilder()
//                .addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)))
//                .addQueryParameter("date", date)
//                .addQueryParameter("host", url.getHost())
//                .build();
//        return httpUrl.toString();
//    }
//
//    class JsonParse {
//        Header header;
//        Payload payload;
//    }
//
//    class Header {
//        int code;
//        int status;
//        String sid;
//    }
//
//    class Payload {
//        Choices choices;
//    }
//
//    class Choices {
//        List<Text> text;
//    }
//
//    class Text {
//        String role;
//        String content;
//    }
//
//    class RoleContent {
//        String role;
//        String content;
//
//        public String getRole() {
//            return role;
//        }
//
//        public void setRole(String role) {
//            this.role = role;
//        }
//
//        public String getContent() {
//            return content;
//        }
//
//        public void setContent(String content) {
//            this.content = content;
//        }
//    }
//}

