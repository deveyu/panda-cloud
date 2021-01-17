封装生产者，打成jar，业务方引入直接使用

支持可靠性投递消，保障消息100%不丢失：消息要落库存储    最主要 
可靠性消息投递存储器  MessageStoreService
实现：存储消息需要单独建一个库；业务和消息（存入消息已发送，超时时间）同时入库；发送消息投递到broker,broker发送confirm ；
组件接收应答,得到消息id,并根据id更改消息库的消息状态；如果
消息状态：1发送 2确认应答 ；如果发送异常，回送ack异常，比如一条消息已经投递10min,消息还没有确认，需要做补偿机制 ，通过分布式定时任务抓取出来，重新发送，重试大于3次
抓取出来，设为3发送失败

生产者缓存容器   RabbitTemplateContainer:疑问：RabbitTemplate一个实例不够使用吗？

生产者确认组件   RabbitTemplateConfirmCallback

消息序列化解析器 RabbitMsgConverter

消息发送客户端 ProducerClient
生产者发送处理器 RabbitBrokerProvider 底层存储db



数据库异步存储器 AsyncQueue 异步发送消息 异步加入队列

消息临时等待发送器 MessageHolder

迅速消息设置器 RapidMsgSetter

批量消息发送 BatchMsgSetter
业务入库；消息入库，记录一条放sessionId
sessionId/消息放threadLocal/MessageHolder list集合，存放消息 /size消息条数

延迟消息设置器 DelayMsgSetter
使用延迟插件即可；添加delay属性
应用场景：电商平台买到商品签收后，不惦记确认支付，那么系统自动在7天后进行支付
自动超时作废：优惠券/红包有使用时间限制

顺序消息设置器 OrderlyMsgSetter
必须要保证：消息到同一个队列，队列消费者只能有一个（独占exclusive）
属性：序号order,size条数
生产者不一定要批量发，最好分开发
消费者拿到消息直接入库不消费，收到顺序消息的第一条就发送一条延迟消息给自己（带上size,sessionId）：5min中后处理消息，目的是为了让后续消息都能入库
收到延迟消息后，根据sessionId,size抽取数据库消息处理
异常情况：缺少条数



重试策略器 RetryMsgHandler

定时抓取处理器 FetchMsgHandler 抓取中间状态的消息

失败处理器 FailMsgHandler 



消费者设计 打成jar供业务直接引用
消费者监听容器 RabbitListener
消费者幂等性保障拦截器 IdempotentMsgHandler
 首先从外部服务获取全局唯一的ID（本地有一个ID生成服务备用）
 拦截器拦截消息，把消息入库，利用主键唯一约束保障（最好有多个库，因为一个库压力太大，通过hash映射到不同的库），为了保障幂等性又入一次库？
 


消息存储路由选择器 DBRoutingSelector 分库分表
消息异步处理器 AsyncMsgHandler  不重要的消息异步消费
消息存储存储幂等服务 IdempotentMsgService
消息消费异常处理器 FailConsumeHandler 



支持迅速消息发送模式，在一些日志收集/统计分析等需求下可以保证高性能，超高吞吐量

支持延迟消息模式，消息可以延迟发送，指定延迟时间，用于某些延迟检查、服务限流场景

支持事务消息，且100%保障可靠性投递，在金融行业单笔大金额操作时会有此类需求


支持消息高性能的序列化转换、异步发送消息

支持消息生产实例与消费实例的链接池化缓存化，提升性能





 支持消息等幂性操作，避免消费端重复消费问题息，



 

支持顺序消息，保证消息送达消费端的前后顺序，例如下订单等符合性操作

支持消息补偿，重试，以及快速定位异常/失败消息

支持集群消息负载均衡，保障消息落到具体SET集群的负载均衡

支持消息路由策略，指定某些消息路由到指定的SET集群