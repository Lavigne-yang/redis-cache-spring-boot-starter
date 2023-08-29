## 说明：

整合spring-boot-starter-data-redis缓存

1. 支持配置统一前缀, 键值序列化定义
    1. inge.redis.prefix=INGE-REDIS
2. 整合Redisson实现分布式锁机制
3. 整合SpringSession实现分布式session解决分布式session共享问题

## 问题：

1. 存在循环问题待解决，使用此依赖需要开启循环依赖 (解决-分析bean注入)
2.