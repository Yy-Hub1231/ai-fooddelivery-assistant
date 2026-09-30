// ChatClient、Function工具注册配置
// @Configuration：Spring 配置类，项目启动执行
// @Bean：将对象放入 Spring IoC 容器，其他地方@Autowired直接注入

package com.student.fooddelivery.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Student
 * @version 1.0.0
 * @date 2026/9/16
 */
@Configuration
public class ChatClientConfiguration {

    // 注入DashScope大模型对象（application.properties配置阿里云通义千问key）
    @Autowired
    DashScopeChatModel dashScopeChatModel;

    // 读取resources/files/systemPrompt.st 文件，加载系统提示词（给AI设定角色：外卖客服）
    @Value("classpath:files/systemPrompt.st")
    Resource systemPrompt;

    // 注入聊天记忆Bean，用来保存用户对话历史
    @Autowired
    ChatMemory chatMemory;

    // @Bean：交给Spring容器管理，创建ChatClient实例，全局唯一
    @Bean
    public ChatClient chatClient() {
        List words = List.of("装逼", "草泥马", "特么的", "撕逼", "玛拉戈壁", "爆菊",
                "JB", "呆逼", "本屌", "法克鱿", "丢你老母", "达菲鸡", "装13", "逼格", "蛋疼", "傻逼", "绿茶婊",
                "你妈的", "表砸", "屌爆了", "买了个婊", "已撸", "妈蛋", "逗比", "我靠", "碧莲", "碧池",
                "然并卵", "日了狗", "屁民", "吃翔", "淫家", "你妹", "滚粗", "性生活", "性交", "生殖器",
                "ISIS", "基地组织", "赌球", "赌博平台", "冰毒", "海洛因", "吸食毒品", "注射毒品", "壮阳",
                "丰胸", "根治", "药到病除", "100%有效", "最好", "顶级", "史上第一", "官方指定", "国家认证",
                "稳赚不赔", "保本保息", "数字货币", "炒币", "第一", "中国第一", "全网第一", "销量第一", "排名第一",
                "唯一", "第一品牌", "NO.1", "TOP.1", "独一无二", "全国第一", "全球第一", "国家级",
                "世界级", "顶级享受", "极品", "极致", "招财进宝", "旺人旺财", "首个", "首选", "独家", "全网首发",
                "全国销量冠军", "国家免检", "世界领先", "遥遥领先", "领导者", "至尊", "巅峰", "王牌",
                "冠军", "特供", "专供", "专家推荐", "史无前例", "前无古人", "永久", "万能", "祖传", "特效",
                "无敌", "纯天然", "100%", "点击领奖", "恭喜获奖", "全民免单", "万人疯抢", "全民疯抢", "卖疯了",
                "秒杀", "抢爆", "再不抢就没了");

        // 构建敏感词拦截器SafeGuardAdvisor
        SafeGuardAdvisor safeGuardAdvisor = SafeGuardAdvisor.builder()
                .sensitiveWords(words)    // 设置敏感词库
                .failureResponse("你好，这个问题我暂时无法回答，让我们换个话题再聊聊吧")
                .build();

        // 聊天记忆增强器：自动读取/写入对话历史，同一个sessionId可以记住上下文
        PromptChatMemoryAdvisor promptChatMemoryAdvisor =
                PromptChatMemoryAdvisor.builder(chatMemory)
                        .build();

        // 构建ChatClient，SpringAI核心对话客户端
        ChatClient chatClient = ChatClient.builder(dashScopeChatModel)
                // 默认系统提示词，读取外部st文件，定义AI角色（外卖客服）
                .defaultSystem(systemPrompt)
                // 给prompt模板传入参数currentTime，每次请求携带当前系统时间给AI
                .defaultSystem(s -> s.param("currentTime", LocalDateTime.now()))
                // 挂载两个增强器：敏感词过滤 + 对话记忆
                .defaultAdvisors(safeGuardAdvisor, promptChatMemoryAdvisor)
                .build();
        return chatClient;
    }
}
