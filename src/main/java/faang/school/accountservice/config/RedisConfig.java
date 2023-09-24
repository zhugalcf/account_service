package faang.school.accountservice.config;

import faang.school.accountservice.listener.PendingOperationEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.pending_operation_channel.name}")
    private String pendingOperationChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(host, port);
        log.info("Created new redis connection factory with host: {}, port: {}", host, port);
        return new JedisConnectionFactory(redisConfiguration);
    }

    @Bean(name = "pendingOperationAdapter")
    public MessageListenerAdapter pendingOperationAdapter(PendingOperationEventListener pendingOperationEventListener) {
        return new MessageListenerAdapter(pendingOperationEventListener);
    }

    @Bean
    public ChannelTopic pendingOperationTopic() {
        return new ChannelTopic(pendingOperationChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            @Qualifier("pendingOperationAdapter") MessageListenerAdapter pendingOperationAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(pendingOperationAdapter, pendingOperationTopic());
        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
