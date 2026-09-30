// 对话记忆持久化JDBC配置

package com.student.fooddelivery.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryConfiguration {

    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository chatMemoryRepository) {
        // 数据库持久化对话记录，消息窗口记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory
                .builder()
                // 使用JDBC存储，对话历史保存到数据库，重启项目不会丢失聊天记录
                .chatMemoryRepository(chatMemoryRepository)
                // 最多保留30条消息（用户+AI来回算消息），超过自动删除旧消息，防止token太长
                .maxMessages(30)
                .build();
        return chatMemory;
    }
}
