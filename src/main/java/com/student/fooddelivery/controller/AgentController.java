// 前端聊天接口，接收用户消息
package com.student.fooddelivery.controller;

import com.student.fooddelivery.service.FoodOrderService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/agent")
@CrossOrigin    // 允许跨域，前端网页访问后端接口
public class AgentController {
    // 注入AI客户端
    @Autowired
    ChatClient chatClient;

    // 注入订单Service，作为AI可调用的工具函数
    @Autowired
    FoodOrderService foodOrderService;

    // GET接口：/agent/chat
    // produces = "text/stream;charset=utf-8" 流式输出，SSE，打字机效果一条一条返回文字
    @GetMapping(value = "/chat", produces = "text/stream;charset=utf-8")
    public Flux<String> chat(
            // message 用户提问内容，默认值"你好"
            @RequestParam(value = "message", defaultValue = "你好") String message,
            // sessionId会话id，区分不同用户，默认user01
            @RequestParam(value = "sessionId", defaultValue = "user01") String sessionId
    ) {
        Flux<String> stream = chatClient.prompt()
                // 设置会话ID，ChatMemory用这个id区分不同用户对话
                .advisors(advisorSpec ->
                        advisorSpec.param(ChatMemory.CONVERSATION_ID, sessionId))
                // 挂载工具：foodOrderService，AI自动识别意图，自动调用里面的方法点餐，取消订单等等
                .tools(foodOrderService)
                // 用户输入消息
                .user(message)
                // 开启流式响应
                .stream()
                // 获取返回文本流
                .content();
        return stream;
    }
}
