# 智能AI点餐助手
基于 SpringBoot + Spring AI + MyBatis-Plus 开发的对话式点餐后台项目。
用户通过自然语言对话的方式完成点餐，利用大模型识别用户意图，支持记忆对话上下文，实现订单的增删改查。

## 技术栈
- 后端：SpringBoot、Spring AI、MyBatis-Plus
- 数据库：MySQL
- 大模型：通义千问 qwen3.7-flash
- 构建工具：Maven

## 功能介绍
1. 自然语言对话点餐，大模型识别用户点餐需求
2. 对话记忆功能，保存用户上下文聊天记录
3. 订单管理：新增、查询、修改、删除订单
4. MyBatis-Plus 操作数据库，简化CRUD开发

## 环境要求
- JDK 17
- MySQL 8.0
- Maven 3.6+

## 项目启动步骤
1. 在MySQL中创建数据库 `fooddelivery_assistant`
2. 执行项目根目录下 `sql/init.sql` 初始化脚本，创建业务订单表
> 项目启动后，Spring AI会自动创建对话记忆表 `spring_ai_chat_memory`，用于存储聊天上下文
3. 修改 `resources/application.properties` 配置文件：
    - 将 `你的dashscope密钥` 替换成自己的Dashscope API-key
    - 将 `你的mysql密码` 替换成自己本地MySQL密码
4. 启动SpringBoot项目，服务端口：9091
5. 访问地址：http://localhost:9091

## 项目配置说明
核心配置文件：`application.properties`
> 仓库中配置使用占位符，使用者需要自行填入密钥与数据库密码，保证密钥安全。

## 项目亮点
1. 整合Spring AI对接通义千问大模型，解析自然语言点餐请求
2. 使用JDBC持久化对话记忆，保留多轮聊天上下文
3. 基于MyBatis-Plus快速开发订单相关CRUD接口
4. 分层开发，Controller接收请求，Service处理业务逻辑