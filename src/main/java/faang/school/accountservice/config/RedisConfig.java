package faang.school.accountservice.config;

import faang.school.accountservice.message.listener.PaymentListener;
import faang.school.accountservice.message.listener.RequestListener;
import faang.school.accountservice.model.Request;
import faang.school.accountservice.repository.RequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
@Slf4j
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.create-request}")
    private String createRequestChannelName;
    @Value("${spring.data.redis.channels.payment-request}")
    private String paymentRequestChannelName;

    @Bean
    public ChannelTopic createRequestTopic() {
        return new ChannelTopic(createRequestChannelName);
    }

    @Bean
    public ChannelTopic paymentRequestTopic() {
        return new ChannelTopic(paymentRequestChannelName);
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        log.info("port - {}", port);
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter(RequestListener requestListener) {
        return new MessageListenerAdapter(requestListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RequestListener requestListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.setTopicSerializer(new StringRedisSerializer());
        container.addMessageListener(messageListenerAdapter(requestListener), createRequestTopic());
        return container;
    }

    @Bean
    MessageListenerAdapter messagePaymentListenerAdapter(PaymentListener paymentListener) {
        return new MessageListenerAdapter(paymentListener);
    }

    @Bean
    RedisMessageListenerContainer redisPaymentContainer(PaymentListener paymentListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.setTopicSerializer(new StringRedisSerializer());
        container.addMessageListener(messagePaymentListenerAdapter(paymentListener), paymentRequestTopic());
        return container;
    }
}
