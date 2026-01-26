//package com.sipc.hospitalalarmsystem.service.impl;
//
//import com.plexpt.chatgpt.ChatGPT;
//import com.plexpt.chatgpt.ChatGPTStream;
//import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
//import com.plexpt.chatgpt.entity.chat.Message;
//import com.sipc.hospitalalarmsystem.model.dto.param.gpt.ChatParam;
//import com.sipc.hospitalalarmsystem.service.GptService;
//import com.sipc.hospitalalarmsystem.util.KeyUtils.KeyManager;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.net.InetSocketAddress;
//import java.net.Proxy;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author CZCZCZ
// * &#064;date 2023-09-13 21:08
// */
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class GptServiceImpl implements GptService {
//
//    final KeyManager keyManager;
//    //   哈希表   id和对话一一对应
//    private static Map<String, List<Message>> context = new HashMap<>();
//
//    static Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//
//    public List<Message> get(String id) {
//        List<Message> messages = context.get(id);
//        if (messages == null) {
//            messages = new ArrayList<>();
//            context.put(id, messages);
//        }
//        return messages;
//    }
//
//    public void add(String id, Message message) {
//        List<Message> messages = context.get(id);
//        if (messages == null) {
//            messages = new ArrayList<>();
//            context.put(id, messages);
//        }
//        messages.add(message);
//    }
//
//    @Override
//    public Message getText(ChatParam param) {
//
//        String prompt = param.buildPrompt();
//
//        String APIKEY = keyManager.getKey();
//        log.error("APIKEY: " + APIKEY);
//
//        ChatGPT chatGPT = ChatGPT.builder()
//                .apiKey(APIKEY)
//                .timeout(50)
//                .proxy(proxy)
//                .build()
//                .init();
//
//        try {
//            //建立message对象
//            Message message = Message.of(prompt);
//            //获取先前的对话
//            List<Message> messages = get(param.getId());
//            //加入对话
//            messages.add(message);
//            //传入一个数组过于无用，接口问题还是什么？？
//            ChatCompletionResponse completion = chatGPT.chatCompletion(messages);
//            Message message1 = completion.getChoices().get(0).getMessage();
//
//            // 将问题和回答加入
//            add(param.getId(), message);
//            add(param.getId(), message1);
//
//            return message1;
//
//        } catch (Exception e) {
//            log.error("API调用出错：{}", e);
//            throw new RuntimeException("服务器挤爆了，请检查KEY， 网络。请输入你的APIKEY后试用: " + e.getMessage());
//        }
//    }
//
//
//}
