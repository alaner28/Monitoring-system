//package com.sipc.hospitalalarmsystem;
//
//import com.sipc.hospitalalarmsystem.sparkdesk.WebSocketClient;
//import jakarta.websocket.ContainerProvider;
//import jakarta.websocket.Session;
//import jakarta.websocket.WebSocketContainer;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//import java.net.URI;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@SpringJUnitConfig
//public class BigModelTest {
//
//    private MockWebServer mockWebServer;
//
//    @BeforeEach
//    public void setUp() throws Exception {
//        // 初始化 MockWebServer
//        mockWebServer = new MockWebServer();
//        mockWebServer.start();
//    }
//
//    @AfterEach
//    public void tearDown() throws Exception {
//        // 关闭 MockWebServer
//        mockWebServer.shutdown();
//    }
//
//    @Test
//    public void testWebSocketConnection() throws Exception {
//        // 设置 MockWebServer 响应
//        mockWebServer.enqueue(new MockResponse().withWebSocketUpgrade(new WebSocketClientTestListener()));
//
//        // WebSocket URI
//        String wsUrl = mockWebServer.url("spark-api.xf-yun.com/v3.5/chat/websocketClient/testUser").toString().replace("http", "wss");
//
//        // 创建 WebSocketContainer
//        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//
//        // 连接 WebSocket
//        Session session = container.connectToServer(WebSocketClient.class, URI.create(wsUrl));
//
//        // 发送消息
//        WebSocketClient webSocketClient = new WebSocketClient();
//        webSocketClient.onOpen(session, "testUser");
//
//        // 发送测试消息
//        webSocketClient.sendMessage("ping");
//
//        // 等待响应
//        CountDownLatch latch = new CountDownLatch(1);
//        latch.await(5, TimeUnit.SECONDS);
//
//        // 验证响应
//        // Here you can assert received messages or WebSocket state
//        // Example:
//        // assertEquals("pong", receivedMessage);
//
//        // 断开连接
//        webSocketClient.onClose();
//    }
//
//    private static class WebSocketClientTestListener extends okhttp3.WebSocketListener {
//        @Override
//        public void onMessage(okhttp3.WebSocket webSocket, String text) {
//            // 验证接收到的消息
//            assertEquals("ping", text);
//            webSocket.send("pong"); // 回复 "pong"
//        }
//    }
//}
